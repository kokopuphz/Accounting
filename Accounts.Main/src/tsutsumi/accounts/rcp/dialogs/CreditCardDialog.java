package tsutsumi.accounts.rcp.dialogs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.TransactionRecord;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.common.response.TransactionsResponse;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.contributions.StatusBarDbControl;

public class CreditCardDialog {
	private Label amountText;
	private Button addButton;
	private DateTime date; 
    private Shell thisShell;
    private static CreditCardDialog defaultDialog = null;
    private static Rectangle currentPosition;
    private Composite flexContainer;
    private Composite amountLabelComp;
//    private TextFocusListener textFocusListener;
    private DefaultSelectionListener defaultSelectionListener;
    private Combo methodCombo;

	private HashMap<Integer, TransactionRecord> transactionMap = new HashMap<Integer, TransactionRecord>();
	private ArrayList<Integer> selectedTransIds = new ArrayList<Integer>();

    public static CreditCardDialog getDefault(Shell parent) {
    	if (defaultDialog == null || defaultDialog.isDisposed()) {
    		defaultDialog = new CreditCardDialog(parent);
    	}
    	return defaultDialog;
    }
    
    private CreditCardDialog(Shell parent) {
//		textFocusListener = new TextFocusListener();
	    defaultSelectionListener = new DefaultSelectionListener();

//    	thisShell = new Shell(parent, SWT.SHELL_TRIM & (~SWT.RESIZE) | SWT.TOOL);
    	thisShell = new Shell(parent, SWT.SHELL_TRIM| SWT.TOOL);
    	GridLayout shellLayout = new GridLayout();
    	shellLayout.marginHeight = 0;
    	shellLayout.marginWidth = 0;
    	shellLayout.horizontalSpacing = 0;
    	shellLayout.verticalSpacing = 0;
    	thisShell.setLayout(shellLayout);
    	thisShell.setText("Credit Card Payments");

    	createPartControl(thisShell);
    	updateFlex();

    	thisShell.addControlListener(new ControlListener() {
			public void controlMoved(ControlEvent e) {
				Control eW = (Control)e.widget;
				currentPosition = eW.getBounds();
			}
			public void controlResized(ControlEvent e) {}
    	});
    }

    private void updateFlex() {
    	for (Control c : flexContainer.getChildren() ) {
    		c.dispose();
    	}
    	transactionMap.clear();
    	selectedTransIds.clear();

		final ScrolledComposite scrollContainer = new ScrolledComposite(flexContainer, SWT.V_SCROLL);
		GridData borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridLayout darkBorderLayout = new GridLayout();
		darkBorderLayout.numColumns = 1;
		darkBorderLayout.marginHeight = 0;
		darkBorderLayout.marginWidth = 0;
		darkBorderLayout.horizontalSpacing = 0;
		darkBorderLayout.verticalSpacing = 0;
		scrollContainer.setLayout(darkBorderLayout);
		scrollContainer.setExpandVertical(true);
		scrollContainer.setExpandHorizontal(true);
		scrollContainer.setAlwaysShowScrollBars(false);
		scrollContainer.getVerticalBar().setIncrement(scrollContainer.getVerticalBar().getIncrement()*3);
		scrollContainer.addListener(SWT.Activate, new Listener() {
		    public void handleEvent(Event e) {
		    	scrollContainer.setFocus();
		    }
		});
		
		Composite borderContainer = new Composite(scrollContainer, SWT.NONE);
		GridLayout borderLayout = new GridLayout();
		borderLayout.marginHeight = 0;
		borderLayout.marginWidth = 0;
		borderLayout.horizontalSpacing = 1;
		borderLayout.verticalSpacing = 1;
		borderLayout.numColumns = 5;
		borderContainer.setLayout(borderLayout);
		borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		borderContainer.setLayoutData(borderData);
		
		GridLayout dataL = new GridLayout();
		dataL.marginHeight = 3;
		dataL.marginWidth = 3;
		dataL.horizontalSpacing = 0;
		dataL.verticalSpacing = 0;

        CheckboxSelectionListener csl = new CheckboxSelectionListener();
        boolean colorSwitch = true;
        Color currentColor = Colors.WHITE;

		ArrayList<TransactionRecord> ccTransList = new ArrayList<TransactionRecord>();
		for (Method m : AbstractReferenceData.getCreditCardMethods()) {
			XmlInterface xmlRequest = new XmlInterface();
	    	xmlRequest.setCommand(CommandEnum.GET_TRANSACTIONS);
	    	xmlRequest.setCommandParams("METHOD_ID",String.valueOf(m.getId()));
	    	xmlRequest.setCommandParams("FROM_DATE","1900/01/01");
	    	xmlRequest.setCommandParams("TO_DATE",Utils.getToday());
	    	xmlRequest.setCommandParams("CREDITCARD",String.valueOf(true));
	    	
	    	AccountsClient client = AccountsClient.getDefault();
	    	XmlInterface response = client.post(xmlRequest);
	    	Response rr = response.getResponse();
	    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
	    		TransactionsResponse transResponse = (TransactionsResponse)rr;
	    		ccTransList.addAll(transResponse.getTransactions());
	    	}
		}
		if (ccTransList.size() == 0) {
			GridData dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1);
			GridData tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 5, 1);
    		
			Composite labelComp = new Composite(borderContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(currentColor);
    		Label defLabel = new Label(labelComp, SWT.LEFT);
            defLabel.setText("No unpaid credit card transactions found.");
            defLabel.setBackground(currentColor);
            defLabel.setLayoutData(dataLayout);

		}
        for (TransactionRecord tr : ccTransList) {
			colorSwitch = !colorSwitch;
			if (colorSwitch) {
				currentColor = Colors.PINK;
			} else {
				currentColor = Colors.WHITE;
			}

        	transactionMap.put(tr.getTransactionId(), tr);
        	GridData dataLayout = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
        	GridData tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    		tableLayout.widthHint = 40;
    		Composite labelComp = new Composite(borderContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(currentColor);
    		Button button = new Button(labelComp, SWT.CHECK | SWT.CENTER);
            button.setBackground(currentColor);
            button.setLayoutData(dataLayout);
            button.setData(tr.getTransactionId());
            button.addSelectionListener(csl);

    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    		tableLayout.widthHint = 60;
    		labelComp = new Composite(borderContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(currentColor);
    		Label defLabel = new Label(labelComp, SWT.LEFT);
            defLabel.setText(AbstractReferenceData.resolveMethodFromId(tr.getMethodId()).getName());
            defLabel.setBackground(currentColor);
            defLabel.setLayoutData(dataLayout);

    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    		tableLayout.widthHint = 100;
    		labelComp = new Composite(borderContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(currentColor);
            defLabel = new Label(labelComp, SWT.LEFT);
            defLabel.setText(tr.getTransactionDate());
            defLabel.setBackground(currentColor);
            defLabel.setLayoutData(dataLayout);

    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    		labelComp = new Composite(borderContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(currentColor);
            defLabel = new Label(labelComp, SWT.LEFT);
            defLabel.setText(tr.getDescription());
            defLabel.setBackground(currentColor);
            defLabel.setLayoutData(dataLayout);		
            
    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    		tableLayout.widthHint = 70;
    		labelComp = new Composite(borderContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(currentColor);
            defLabel = new Label(labelComp, SWT.RIGHT);
            defLabel.setText(Utils.formatNumber(tr.getDebit()));
            defLabel.setBackground(currentColor);
            defLabel.setLayoutData(dataLayout);
        }
        
        Point p = borderContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        if (20 + p.y > 400) {
        	GridData grid = (GridData)flexContainer.getLayoutData();
        	grid.heightHint = 350;
        	grid = (GridData)amountLabelComp.getLayoutData();
        	grid.widthHint = 86;
        }
        
		scrollContainer.setMinSize(borderContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		scrollContainer.setContent(borderContainer);
    	resizeShell();
    }
    
    private void resizeShell() {
    	flexContainer.layout(true);
    	thisShell.pack();
    	thisShell.setMinimumSize(thisShell.getSize());
    	if (currentPosition != null) {
    		Point p = thisShell.getSize();
    		currentPosition.width = 500;
    		currentPosition.height = p.y;
    		thisShell.setBounds(currentPosition);
    		thisShell.layout(true);
    	} else {
	    	Rectangle bds = thisShell.getDisplay().getBounds();
	    	Point p = thisShell.getSize();
	    	int nLeft = (bds.width - 500) / 2;
	    	int nTop = (bds.height - p.y) / 2;
	    	thisShell.setBounds(nLeft, nTop, 500, p.y);
	    	currentPosition = thisShell.getBounds();
    	}

    }
    
    public boolean isDisposed() {
    	return thisShell.isDisposed();
    }
    
    public void open() {
    	if (thisShell != null && !thisShell.isDisposed()) {
    		resetValues();
    		thisShell.open();
    	}
    }
    
    
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.verticalSpacing=10;
		layout.numColumns = 1;

		GridData containerGrid = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		Composite container = new Composite(parent, SWT.BORDER);
		container.setBackground(Colors.CCBG);
		container.setLayout(layout);
		container.setLayoutData(containerGrid);

		GridData labelData = null;
		GridData gridData = null;
		
		Composite darkBorderContainer = new Composite(container, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 1;
		gridLayout.marginWidth = 1;
		gridLayout.verticalSpacing=0;
		gridLayout.horizontalSpacing=0;
		darkBorderContainer.setLayout(gridLayout);
		darkBorderContainer.setBackground(Colors.BORDER);
		GridData borderData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		darkBorderContainer.setLayoutData(borderData);
		
		Composite headerContainer = new Composite(darkBorderContainer, SWT.NONE);
		GridLayout headerLayout = new GridLayout();
		headerLayout.marginTop = 1;
		headerLayout.marginBottom = 0;
		headerLayout.marginHeight = 0;
		headerLayout.marginWidth = 1;
		headerLayout.horizontalSpacing = 1;
		headerLayout.verticalSpacing = 1;
		headerLayout.numColumns = 5;
		headerContainer.setLayout(headerLayout);
		borderData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		headerContainer.setLayoutData(borderData);

		GridLayout dataL = new GridLayout();
		dataL.marginHeight = 3;
		dataL.marginWidth = 3;
		dataL.horizontalSpacing = 0;
		dataL.verticalSpacing = 0;

		GridData dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridData tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		tableLayout.widthHint = 40;
		Composite labelComp = new Composite(headerContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(tableLayout);
		labelComp.setBackground(Colors.PINKHEADER);
        Label defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText("Select");
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);

		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		tableLayout.widthHint = 60;
		labelComp = new Composite(headerContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(tableLayout);
		labelComp.setBackground(Colors.PINKHEADER);
        defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText("Card");
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);

		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		tableLayout.widthHint = 100;
		labelComp = new Composite(headerContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(tableLayout);
		labelComp.setBackground(Colors.PINKHEADER);
        defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText("Date");
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);

		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		labelComp = new Composite(headerContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(tableLayout);
		labelComp.setBackground(Colors.PINKHEADER);
        defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText("Description");
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);		
        
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		tableLayout.widthHint = 70;
		amountLabelComp = new Composite(headerContainer, SWT.NONE);
		amountLabelComp.setLayout(dataL);
		amountLabelComp.setLayoutData(tableLayout);
		amountLabelComp.setBackground(Colors.PINKHEADER);
        defLabel = new Label(amountLabelComp, SWT.CENTER);
        defLabel.setText("Amount");
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);

		
		flexContainer = new Composite(darkBorderContainer, SWT.NONE);
		FillLayout fill = new FillLayout();
		fill.marginHeight=1;
		fill.marginWidth=1;
		flexContainer.setLayout(fill);
		borderData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		flexContainer.setLayoutData(borderData);

        Composite bottomContainer = new Composite(container, SWT.NONE);
        GridLayout bottomLayout = new GridLayout();
        bottomLayout.marginHeight = 0;
        bottomLayout.marginWidth = 0;
        bottomLayout.horizontalSpacing = 8;
        bottomLayout.verticalSpacing = 8;
        bottomLayout.numColumns = 2;
        bottomContainer.setLayout(bottomLayout);
        bottomContainer.setBackground(Colors.CCBG);
        gridData = new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1);
        bottomContainer.setLayoutData(gridData);
        
	    labelData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        Label amountLabel = new Label(bottomContainer, SWT.RIGHT);
        amountLabel.setText("Selected Amount:");
        amountLabel.setBackground(Colors.CCBG);
        amountLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        amountText = new Label(bottomContainer, SWT.SHADOW_OUT);
        amountText.setLayoutData(gridData);
        amountText.setData(new BigDecimal(0));
        amountText.setText(Utils.formatNumber(new BigDecimal(0)));
        amountText.setBackground(Colors.CCBG);

        labelData = new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1);
        Label dateLabel = new Label(bottomContainer, SWT.RIGHT);
        dateLabel.setText("&Payment Date:");
        dateLabel.setBackground(Colors.CCBG);
        dateLabel.setLayoutData(labelData);
        
        GridLayout dateContainerLayout = new GridLayout();
        dateContainerLayout.marginHeight = 0;
        dateContainerLayout.marginWidth = 0;
        dateContainerLayout.horizontalSpacing = 3;
        dateContainerLayout.verticalSpacing = 0;
        dateContainerLayout.numColumns = 2;
        
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        Composite dateContainer = new Composite(bottomContainer, SWT.NONE);
        dateContainer.setLayout(dateContainerLayout);
        dateContainer.setLayoutData(gridData);
        dateContainer.setBackground(Colors.CCBG);
        
		date = new DateTime (dateContainer, SWT.DATE | SWT.MEDIUM| SWT.BORDER);
		final DatePickerDialog dateDialog = new DatePickerDialog(thisShell, date);
		Image image = new Image(Display.getCurrent(), AddNewDialog.class.getResourceAsStream("cal.gif"));
		
		labelData = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		Label imageLabel = new Label(dateContainer, SWT.LEFT);
		imageLabel.setImage(image);
		imageLabel.setLayoutData(labelData);
	    imageLabel.addMouseListener(new MouseListener() {
	    	private boolean mouseOver = false;;
			public void mouseDoubleClick(MouseEvent e) {}
			public void mouseDown(MouseEvent e) {
				mouseOver = true;
			}
			public void mouseUp(MouseEvent e) {
				if (mouseOver) {
					dateDialog.setLocation();
					dateDialog.setVisible(true);
				}
				mouseOver = false;
			}
	    });

        labelData = new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1);
        dateLabel = new Label(bottomContainer, SWT.RIGHT);
        dateLabel.setText("Payment &Method:");
        dateLabel.setBackground(Colors.CCBG);
        dateLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
        methodCombo = new Combo(bottomContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Method[] meth = AbstractReferenceData.getBankMethods().toArray(new Method[AbstractReferenceData.getBankMethods().size()]);
        methodCombo.setData(meth);
        for (Method s : meth) {
        	methodCombo.add(s.getName());
        }
        methodCombo.addSelectionListener(defaultSelectionListener);
        methodCombo.select(0);
        methodCombo.setLayoutData(gridData);

        
		GridData gridData2 = new GridData(SWT.FILL, SWT.CENTER, true, true);
	    gridData2.horizontalSpan = 3;

        addButton = new Button(container, SWT.PUSH);
        addButton.setLayoutData(gridData2);
    	addButton.setText("&Process Payment");
        addButton.setEnabled(false);
        addButton.addSelectionListener(defaultSelectionListener);
	}

	private void resetValues() {
		updateFlex();
		addButton.setEnabled(false);
		
		//fixed bug 2014/01/15
		BigDecimal currVal = new BigDecimal(0);
		amountText.setData(currVal);
		amountText.setText(Utils.formatNumber(currVal));
		selectedTransIds.clear();
	}
    
	private class CheckboxSelectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e) {
			if (((Button)e.widget).getSelection()) {
				BigDecimal currVal = (BigDecimal)amountText.getData();
				int transId = (Integer)((Button)e.widget).getData();
				if (!selectedTransIds.contains(transId)) {
					currVal = currVal.add(transactionMap.get(transId).getDebit());
					amountText.setData(currVal);
					amountText.setText(Utils.formatNumber(currVal));
					selectedTransIds.add(transId);
				}
			} else {
				BigDecimal currVal = (BigDecimal)amountText.getData();
				int transId = (Integer)((Button)e.widget).getData();
				if (selectedTransIds.contains(transId)) {
					currVal = currVal.subtract(transactionMap.get(transId).getDebit());
					amountText.setData(currVal);
					amountText.setText(Utils.formatNumber(currVal));
//					selectedTransIds.remove(transId);
					selectedTransIds.remove((Integer)transId);
				}
			}
			addButton.setEnabled(selectedTransIds.size() > 0);
		}
		public void widgetDefaultSelected(SelectionEvent e) {}
	}
	
	
    public void addNew() {
    	String dateValue = date.getYear()+"/"+ (date.getMonth()+1) +"/"+date.getDay();
    	String description = "Credit Card Payment";
    	ArrayList<int[]> updatedRefs = new ArrayList<int[]>();
    	Method m = ((Method[])methodCombo.getData())[methodCombo.getSelectionIndex()];
    	int fromMethodId = m.getId();
    	int categoryId = 21;
    	
    	//grab distinct account, method combinations.
    	HashMap<List<Integer>, ArrayList<TransactionRecord>> distinctPayments = new HashMap<List<Integer>, ArrayList<TransactionRecord>>();
    	for (Integer transId : selectedTransIds) {
    		TransactionRecord tran = transactionMap.get(transId);
    		int methodId = tran.getMethodId();
    		int accountId = tran.getAccountId();
    		List<Integer> key = new ArrayList<Integer>();
    		key.add(methodId);
    		key.add(accountId);
    		if (!distinctPayments.containsKey(key)) {
    			distinctPayments.put(key,  new ArrayList<TransactionRecord>());
    		}
    		distinctPayments.get(key).add(tran);
    	}

    	for (List<Integer> key : distinctPayments.keySet()) {
    		TransactionRecord[] trans = distinctPayments.get(key).toArray(new TransactionRecord[distinctPayments.get(key).size()]);
    		String transIdString = "";
        	BigDecimal totAmount = new BigDecimal(0);
        	int toMethodId = 0;
        	int fromAccountId = 0;
        	int toAccountId = 0;
        	for (int i =0; i < trans.length; i++) {
        		TransactionRecord tran = trans[i];
        		if (i == trans.length -1) {
        			transIdString += String.valueOf(tran.getTransactionId());
        		} else {
        			transIdString += String.valueOf(tran.getTransactionId()) + ",";
        		}
        		toMethodId = tran.getMethodId();
        		fromAccountId = tran.getAccountId();
        		toAccountId = tran.getAccountId();
        		totAmount = totAmount.add(tran.getDebit());
            	updatedRefs.add(new int[]{toMethodId, toAccountId, categoryId});
            	updatedRefs.add(new int[]{fromMethodId, fromAccountId, categoryId});
        	}
        	XmlInterface xmlRequest = new XmlInterface();
        	xmlRequest.setCommand(CommandEnum.ADD);
        	xmlRequest.setCommandParams("DATE", dateValue);
        	xmlRequest.setCommandParams("FROM_ACCOUNT_ID", String.valueOf(fromAccountId));
        	xmlRequest.setCommandParams("TO_ACCOUNT_ID", String.valueOf(toAccountId));
        	xmlRequest.setCommandParams("CATEGORY_ID", String.valueOf(categoryId));
        	xmlRequest.setCommandParams("FROM_METHOD_ID", String.valueOf(fromMethodId));
        	xmlRequest.setCommandParams("TO_METHOD_ID", String.valueOf(toMethodId));
        	xmlRequest.setCommandParams("AMOUNT", totAmount.toString());
        	xmlRequest.setCommandParams("DESCRIPTION", description);
        	xmlRequest.setCommandParams("CC_TRANSACTION_ID_ARRAY", transIdString);
        	AccountsClient client = AccountsClient.getDefault();
        	XmlInterface response = client.post(xmlRequest);
        	Response rr = response.getResponse();
        	if (rr.getStatus() == Response.SUCCESS) {
        		StatusBarDbControl.setText("Successfully inserted new record");
        	} else {
        		StatusBarDbControl.setText("Error whle processing: " + rr.getMessage());
        	}
    	}
    	for (int[] refs : updatedRefs) {
    		ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
    		IParameter iparam;
    		Parameterization params;
    		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    		ICommandService cmdService = (ICommandService)window.getService(ICommandService.class);
    		Command cmd = cmdService.getCommand("Accounts.Main.UpdateAmounts");
    		try {
	    		iparam = cmd.getParameter("Accounts.Main.UpdateAmounts.MethodId");
	    		params = new Parameterization(iparam, String.valueOf(refs[0]));
	    		parameters.add(params);
	
	    		iparam = cmd.getParameter("Accounts.Main.UpdateAmounts.AccountId");
	    		params = new Parameterization(iparam, String.valueOf(refs[1]));
	    		parameters.add(params);
	
	    		iparam = cmd.getParameter("Accounts.Main.UpdateAmounts.CategoryId");
	    		params = new Parameterization(iparam, String.valueOf(refs[2]));
	    		parameters.add(params);
    		} catch (NotDefinedException e) {
    			e.printStackTrace();
    		}
    		//build the parameterized command
    		ParameterizedCommand pc = new ParameterizedCommand(cmd, parameters.toArray(new Parameterization[parameters.size()]));
    		//execute the command
    		IHandlerService handlerService = (IHandlerService)window.getService(IHandlerService.class);
    		try {
    			handlerService.executeCommand(pc, null);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	resetValues();
    }
    
    private class DefaultSelectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e) {
			if (e.widget == addButton) {
				addNew();
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {
			addNew();
		}
    }
    
}