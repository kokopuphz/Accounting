package tsutsumi.accounts.rcp.dialogs;

import java.util.ArrayList;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.reference.Type;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.contributions.StatusBarDbControl;

public class AddNewDialog {
	private Text descriptionText;
	private Text amountText;
	
	private Combo categoryCombo;
	private Combo fromAccountCombo;
	private Combo toAccountCombo;
	private Combo fromMethodCombo;
	private Combo toMethodCombo;
	private Combo transTypeCombo;
	
	private Button addButton;
	private DateTime date; 
    private AutoCompleteField autoComplete;
    private Shell thisShell;
    private static AddNewDialog defaultDialog = null;
    private static Rectangle currentPosition;
    private Composite flexContainer;
    private TextFocusListener textFocusListener;
    private DefaultSelectionListener defaultSelectionListener;
    private static String currentTransType = null;

    public static AddNewDialog getDefault(Shell parent) {
    	if (defaultDialog == null || defaultDialog.isDisposed()) {
    		defaultDialog = new AddNewDialog(parent);
    	}
    	return defaultDialog;
    }
    
    private AddNewDialog(Shell parent) {
		textFocusListener = new TextFocusListener();
	    defaultSelectionListener = new DefaultSelectionListener();

    	thisShell = new Shell(parent, SWT.SHELL_TRIM & (~SWT.RESIZE) | SWT.TOOL);
    	GridLayout shellLayout = new GridLayout();
    	shellLayout.marginHeight = 0;
    	shellLayout.marginWidth = 0;
    	shellLayout.horizontalSpacing = 0;
    	shellLayout.verticalSpacing = 0;
    	
    	thisShell.setLayout(shellLayout);
    	createPartControl(thisShell);
    	thisShell.setText("Transactions");
    	
    	if (currentTransType == null) {
    		currentTransType = transTypeCombo.getText();
    	} else {
    		if (currentTransType.equals("Expense")) {
    			transTypeCombo.select(0);
    		} else if (currentTransType.equals("Income")) {
    			transTypeCombo.select(1);
    		} else if (currentTransType.equals("ATM")) {
    			transTypeCombo.select(5);
    		} else if (currentTransType.equals("Transfer: Account")) {
    			transTypeCombo.select(2);
    		} else if (currentTransType.equals("Transfer: Bank")) {
    			transTypeCombo.select(3);
    		} else {
    			transTypeCombo.select(4);
    		}
    	}
    	updateFlex();
//    	resizeShell();
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
		if (currentTransType.equals("Expense")) {
			showExpense();	
		} else if (currentTransType.equals("Income")) {
			showIncome();
		} else if (currentTransType.equals("ATM")) {
			showAtm();
		} else {
			showTransfer();
		}
		resizeShell();
    }
    
    private void resizeShell() {
    	flexContainer.layout(true);
    	thisShell.pack();
    	if (currentPosition != null) {
    		Point p = thisShell.getSize();
    		currentPosition.width = p.x;
    		currentPosition.height = p.y;
    		thisShell.setBounds(currentPosition);
//    		thisShell.layout(true);
    	} else {
	    	Rectangle bds = thisShell.getDisplay().getBounds();
	    	Point p = thisShell.getSize();
	    	int nLeft = (bds.width - p.x) / 2;
	    	int nTop = (bds.height - p.y) / 2;
	    	thisShell.setBounds(nLeft, nTop, p.x, p.y);
	    	currentPosition = thisShell.getBounds();
    	}

    }
    
    public boolean isDisposed() {
    	return thisShell.isDisposed();
    }
    
    public void open() {
    	if (thisShell != null && !thisShell.isDisposed()) {
    		thisShell.open();
    	}
    }
    
    
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.verticalSpacing=10;
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth=true;

		GridData containerGrid = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		Composite container = new Composite(parent, SWT.BORDER);
		container.setBackground(Colors.ADDNEWBG);
		container.setLayout(layout);
		container.setLayoutData(containerGrid);

		GridData labelData = null;
		GridData gridData = null;
		
		labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		Label transactionTypeLabel = new Label(container, SWT.LEFT);
        transactionTypeLabel.setText("&Transaction Type");
        transactionTypeLabel.setBackground(Colors.ADDNEWBG);
        transactionTypeLabel.setLayoutData(labelData);
        
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        transTypeCombo = new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Type[] type = AbstractReferenceData.getTypes().toArray(new Type[AbstractReferenceData.getTypes().size()]);
        ArrayList<Type> typeList = new ArrayList<Type>();
        typeList.addAll(AbstractReferenceData.getTypes());
        for (Type s : type) {
        	if (s.getId()==3) {
        		transTypeCombo.add("Transfer: Account");
        		transTypeCombo.add("Transfer: Bank");
        		transTypeCombo.add("Transfer: Cash");
        		typeList.add(s);
        		typeList.add(s);
        		typeList.add(s);
        	} else {
        		transTypeCombo.add(s.getName());
        		typeList.add(s);
        	}
        }
        transTypeCombo.setData(typeList.toArray(new Type[typeList.size()]));
        transTypeCombo.addSelectionListener(defaultSelectionListener);
        transTypeCombo.select(0);
        transTypeCombo.setLayoutData(gridData);

        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label dateLabel = new Label(container, SWT.LEFT);
        dateLabel.setText("&Date");
        dateLabel.setBackground(Colors.ADDNEWBG);
        dateLabel.setLayoutData(labelData);
        
        GridLayout dateContainerLayout = new GridLayout();
        dateContainerLayout.marginHeight = 0;
        dateContainerLayout.marginWidth = 0;
        dateContainerLayout.horizontalSpacing = 3;
        dateContainerLayout.verticalSpacing = 0;
        dateContainerLayout.numColumns = 2;
        
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        Composite dateContainer = new Composite(container, SWT.NONE);
        dateContainer.setLayout(dateContainerLayout);
        dateContainer.setLayoutData(gridData);
        dateContainer.setBackground(Colors.ADDNEWBG);
        
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
	    
        GridLayout flexContainerLayout = new GridLayout();
        flexContainerLayout.marginHeight = 0;
        flexContainerLayout.marginWidth = 0;
        flexContainerLayout.horizontalSpacing = 0;
        flexContainerLayout.verticalSpacing = 0;
        flexContainerLayout.numColumns = 1;

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
        flexContainer = new Composite(container, SWT.NONE);
        flexContainer.setLayout(flexContainerLayout);
        flexContainer.setLayoutData(gridData);
        flexContainer.setBackground(Colors.ADDNEWBG);

	    labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label amountLabel = new Label(container, SWT.LEFT);
        amountLabel.setText("A&mount");
        amountLabel.setBackground(Colors.ADDNEWBG);
        amountLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        amountText = new Text(container, SWT.BORDER);
        amountText.addSelectionListener(defaultSelectionListener);
        amountText.addFocusListener(textFocusListener);
        amountText.setLayoutData(gridData);
        amountText.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				addButton.setEnabled(Utils.isNumeric(amountText.getText()));
			}
        });

		GridData gridData2 = new GridData(SWT.FILL, SWT.CENTER, true, true);
	    gridData2.horizontalSpan = 3;

        addButton = new Button(container, SWT.PUSH);
        addButton.setLayoutData(gridData2);
    	addButton.setText("&Insert Record");
        addButton.setEnabled(false);
        addButton.addSelectionListener(defaultSelectionListener);
        
//        container.setTabList(new Control[] {date, accountCombo, categoryCombo, methodCombo, descriptionText, amountText, addButton});
	}

	private void resetValues() {
		if (descriptionText != null && !descriptionText.isDisposed()) {
			descriptionText.setText("");
		}
		amountText.setText("");
		if (autoComplete != null) {
			autoComplete.setProposals(AbstractReferenceData.getDescriptionProposalTextArray());
		}
		transTypeCombo.setFocus();
		date.setFocus();
	}
    
    public void addNew() {
    	String dateValue = date.getYear()+"/"+ (date.getMonth()+1) +"/"+date.getDay();
    	String transType = transTypeCombo.getText();
    	int fromMethodId = 0;
    	int toMethodId = 0;
    	int categoryId = 0;
    	int fromAccountId = 0;
    	int toAccountId = 0;
    	String description = "";
    	ArrayList<int[]> updatedRefs = new ArrayList<int[]>();

    	if (transType.equals("Expense")) {
	    	Method m = ((Method[])fromMethodCombo.getData())[fromMethodCombo.getSelectionIndex()];
	    	Account a = ((Account[])fromAccountCombo.getData())[fromAccountCombo.getSelectionIndex()];
	    	Category c = ((Category[])categoryCombo.getData())[categoryCombo.getSelectionIndex()];
	    	fromMethodId = m.getId();
	    	toMethodId = 8;
	    	categoryId = c.getId();
	    	fromAccountId = a.getId();
	    	toAccountId = fromAccountId;
	    	description = descriptionText.getText();
	    	updatedRefs.add(new int[]{m.getId(), a.getId(), c.getId()});
    	} else if (transType.equals("Income")) {
	    	Method m = ((Method[])fromMethodCombo.getData())[fromMethodCombo.getSelectionIndex()];
	    	Account a = ((Account[])toAccountCombo.getData())[toAccountCombo.getSelectionIndex()];
	    	Category c = ((Category[])categoryCombo.getData())[categoryCombo.getSelectionIndex()];
	    	fromMethodId = 9;
	    	toMethodId = m.getId();
	    	categoryId = c.getId();
	    	fromAccountId = a.getId();
	    	toAccountId = a.getId();
	    	description = descriptionText.getText();
	    	updatedRefs.add(new int[]{m.getId(), a.getId(), c.getId()});
    	} else if (transType.equals("Transfer: Account")) {
	    	Method fromM = ((Method[])fromMethodCombo.getData())[fromMethodCombo.getSelectionIndex()];
	    	Method toM = fromM;
	    	Account fromAccount = ((Account[])fromAccountCombo.getData())[fromAccountCombo.getSelectionIndex()];
	    	Account toAccount = ((Account[])toAccountCombo.getData())[toAccountCombo.getSelectionIndex()];
	    	Category c = AbstractReferenceData.resolveCategoryFromId(17);
	    	fromMethodId = fromM.getId();
	    	toMethodId = toM.getId();
	    	categoryId = c.getId();
	    	fromAccountId = fromAccount.getId();
	    	toAccountId = toAccount.getId();
	    	description = "Trns: " + fromM.getName() + ":" + fromAccount.getName() + " > " + toM.getName() + ":" + toAccount.getName();
	    	updatedRefs.add(new int[]{fromM.getId(), fromAccount.getId(), c.getId()});
	    	updatedRefs.add(new int[]{toM.getId(), toAccount.getId(), c.getId()});
    	} else if (transType.equals("Transfer: Bank")) {
	    	Method fromM = ((Method[])fromMethodCombo.getData())[fromMethodCombo.getSelectionIndex()];
	    	Method toM = ((Method[])toMethodCombo.getData())[toMethodCombo.getSelectionIndex()];
	    	Account fromAccount = ((Account[])fromAccountCombo.getData())[fromAccountCombo.getSelectionIndex()];
	    	Account toAccount = fromAccount;
	    	Category c = AbstractReferenceData.resolveCategoryFromId(17);
	    	fromMethodId = fromM.getId();
	    	toMethodId = toM.getId();
	    	categoryId = c.getId();
	    	fromAccountId = fromAccount.getId();
	    	toAccountId = toAccount.getId();
	    	description = "Trns: " + fromM.getName() + ":" + fromAccount.getName() + " > " + toM.getName() + ":" + toAccount.getName();
	    	updatedRefs.add(new int[]{fromM.getId(), fromAccount.getId(), c.getId()});
	    	updatedRefs.add(new int[]{toM.getId(), toAccount.getId(), c.getId()});
    	} else if (transType.equals("Transfer: Cash")) {
	    	Method fromM = ((Method[])fromMethodCombo.getData())[fromMethodCombo.getSelectionIndex()];
	    	Method toM = ((Method[])toMethodCombo.getData())[toMethodCombo.getSelectionIndex()];
	    	Account fromAccount = ((Account[])fromAccountCombo.getData())[fromAccountCombo.getSelectionIndex()];
	    	Account toAccount = fromAccount;
	    	Category c = AbstractReferenceData.resolveCategoryFromId(17);
	    	fromMethodId = fromM.getId();
	    	toMethodId = toM.getId();
	    	categoryId = c.getId();
	    	fromAccountId = fromAccount.getId();
	    	toAccountId = toAccount.getId();
	    	description = "Trns: " + fromM.getName() + ":" + fromAccount.getName() + " > " + toM.getName() + ":" + toAccount.getName();
	    	updatedRefs.add(new int[]{fromM.getId(), fromAccount.getId(), c.getId()});
	    	updatedRefs.add(new int[]{toM.getId(), toAccount.getId(), c.getId()});
    	} else if (transType.equals("ATM")) {
	    	Method m = ((Method[])fromMethodCombo.getData())[fromMethodCombo.getSelectionIndex()];
	    	Method cashm = ((Method[])toMethodCombo.getData())[toMethodCombo.getSelectionIndex()];
	    	Account fromAccount = ((Account[])fromAccountCombo.getData())[fromAccountCombo.getSelectionIndex()];
	    	Category c = ((Category[])categoryCombo.getData())[categoryCombo.getSelectionIndex()];
	    	categoryId = c.getId();
	    	if (c.getId()==9) {
	    		//deposit
	    		fromMethodId = cashm.getId();
	    		toMethodId = m.getId();
	    	} else {
	    		toMethodId = cashm.getId();
	    		fromMethodId = m.getId();
	    	}
	    	fromAccountId = fromAccount.getId();
	    	toAccountId = fromAccount.getId();
	    	description = "ATM " + c.getName() + " :" + m.getName() + ":" + fromAccount.getName()+":"+cashm.getName();
	    	updatedRefs.add(new int[]{m.getId(), fromAccount.getId(), c.getId()});
	    	updatedRefs.add(new int[]{cashm.getId(), fromAccount.getId(), c.getId()});
    	}
    	XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.ADD);
    	xmlRequest.setCommandParams("DATE", dateValue);
    	xmlRequest.setCommandParams("FROM_ACCOUNT_ID", String.valueOf(fromAccountId));
    	xmlRequest.setCommandParams("TO_ACCOUNT_ID", String.valueOf(toAccountId));
    	xmlRequest.setCommandParams("CATEGORY_ID", String.valueOf(categoryId));
    	xmlRequest.setCommandParams("FROM_METHOD_ID", String.valueOf(fromMethodId));
    	xmlRequest.setCommandParams("TO_METHOD_ID", String.valueOf(toMethodId));
    	xmlRequest.setCommandParams("AMOUNT", amountText.getText());
    	xmlRequest.setCommandParams("DESCRIPTION", description);
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	if (rr.getStatus() == Response.SUCCESS) {
    		StatusBarDbControl.setText("Successfully inserted new record");
    		if (descriptionText != null && !descriptionText.isDisposed()) {
    			AbstractReferenceData.addDescriptionProposal(descriptionText.getText());
    		}
    	} else {
    		StatusBarDbControl.setText("Error whle processing: " + rr.getMessage());
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
    
    private class TextFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			if (e.widget instanceof Text) {
				((Text)e.widget).selectAll();
			}
		}
		public void focusLost(FocusEvent e) {}
    }
    
    private class DefaultSelectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e) {
			if (e.widget == transTypeCombo) {
				if (!transTypeCombo.getText().equals(currentTransType)) {
					currentTransType = transTypeCombo.getText();
					updateFlex();
				}
			}
			if (e.widget == addButton) {
				if (Utils.isNumeric(amountText.getText())) {
					addNew();
				}
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {
			if (Utils.isNumeric(amountText.getText())) {
				addNew();
			}
		}
    }
    
    private void showIncome() {
		GridLayout sectionLayout = new GridLayout();
		sectionLayout.verticalSpacing = 10;
		sectionLayout.marginHeight = 0;
		sectionLayout.marginWidth = 0;
		sectionLayout.numColumns = 2;
		sectionLayout.makeColumnsEqualWidth=true;

		GridData sectionData = null;
		sectionData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		Composite expenseContainer = new Composite(flexContainer, SWT.NONE);
		expenseContainer.setLayout(sectionLayout);
		expenseContainer.setLayoutData(sectionData);
		expenseContainer.setBackground(Colors.ADDNEWBG);

		GridData labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label accountLabel = new Label(expenseContainer, SWT.LEFT);
        accountLabel.setText("&To Account");
        accountLabel.setBackground(Colors.ADDNEWBG);
        accountLabel.setLayoutData(labelData);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        toAccountCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Account[] acct = AbstractReferenceData.getAccounts().toArray(new Account[AbstractReferenceData.getAccounts().size()]);
        toAccountCombo.setData(acct);
        for (Account s : acct) {
        	toAccountCombo.add(s.getName());
        }
        toAccountCombo.addSelectionListener(defaultSelectionListener);
        toAccountCombo.select(0);
        toAccountCombo.setLayoutData(gridData);
        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label categoryLabel = new Label(expenseContainer, SWT.LEFT);
        categoryLabel.setText("&Category");
        categoryLabel.setBackground(Colors.ADDNEWBG);
        categoryLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        categoryCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Category[] cat = AbstractReferenceData.getCategories(2).toArray(new Category[AbstractReferenceData.getCategories(2).size()]);
        categoryCombo.setData(cat);
        for (Category s : cat) {
        	categoryCombo.add(s.getName());
        }
        categoryCombo.addSelectionListener(defaultSelectionListener);
        categoryCombo.select(0);
        categoryCombo.setLayoutData(gridData);

        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label methodLabel = new Label(expenseContainer, SWT.LEFT);
        methodLabel.setText("&Income Method");
        methodLabel.setBackground(Colors.ADDNEWBG);
        methodLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        fromMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        
        ArrayList<Method> methFiltered = new ArrayList<Method>();
        methFiltered.addAll(AbstractReferenceData.getCashMethods());
        methFiltered.addAll(AbstractReferenceData.getBankMethods());
        Method[] meth = methFiltered.toArray(new Method[methFiltered.size()]);
        fromMethodCombo.setData(meth);
        for (Method s : meth) {
        	fromMethodCombo.add(s.getName());
        }
        fromMethodCombo.addSelectionListener(defaultSelectionListener);
        fromMethodCombo.select(0);
        fromMethodCombo.setLayoutData(gridData);
        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label descLabel = new Label(expenseContainer, SWT.LEFT);
        descLabel.setText("D&escription");
        descLabel.setBackground(Colors.ADDNEWBG);
        descLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        descriptionText = new Text(expenseContainer, SWT.BORDER);
        descriptionText.setLayoutData(gridData);
        descriptionText.addSelectionListener(defaultSelectionListener);
        descriptionText.addFocusListener(textFocusListener);
        autoComplete = new AutoCompleteField(
        		descriptionText, 
        		new TextContentAdapter(), 
        		AbstractReferenceData.getDescriptionProposalTextArray()
        		);

    }
    
    private void showTransfer() {
		GridLayout sectionLayout = new GridLayout();
		sectionLayout.verticalSpacing = 10;
		sectionLayout.marginHeight = 0;
		sectionLayout.marginWidth = 0;
		sectionLayout.numColumns = 2;
		sectionLayout.makeColumnsEqualWidth=true;

		GridData sectionData = null;
		sectionData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		Composite expenseContainer = new Composite(flexContainer, SWT.NONE);
		expenseContainer.setLayout(sectionLayout);
		expenseContainer.setLayoutData(sectionData);
		expenseContainer.setBackground(Colors.ADDNEWBG);

		GridData labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label accountLabel = new Label(expenseContainer, SWT.LEFT);
        accountLabel.setText("&From Account");
        accountLabel.setBackground(Colors.ADDNEWBG);
        accountLabel.setLayoutData(labelData);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        fromAccountCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Account[] acct = AbstractReferenceData.getAccounts().toArray(new Account[AbstractReferenceData.getAccounts().size()]);
        fromAccountCombo.setData(acct);
        for (Account s : acct) {
        	fromAccountCombo.add(s.getName());
        }
        fromAccountCombo.addSelectionListener(defaultSelectionListener);
        fromAccountCombo.select(0);
        fromAccountCombo.setLayoutData(gridData);
        
		if (currentTransType.equals("Transfer: Account")) {
	        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	        Label toAccountLabel = new Label(expenseContainer, SWT.LEFT);
	        toAccountLabel.setText("To &Account");
	        toAccountLabel.setBackground(Colors.ADDNEWBG);
	        toAccountLabel.setLayoutData(labelData);
	
	        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	        toAccountCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
	        toAccountCombo.setData(acct);
	        for (Account s : acct) {
	        	toAccountCombo.add(s.getName());
	        }
	        toAccountCombo.addSelectionListener(defaultSelectionListener);
	        toAccountCombo.select(0);
	        toAccountCombo.setLayoutData(gridData);
		}
        if (currentTransType.equals("Transfer: Cash")) {        
	        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	        Label methodLabel = new Label(expenseContainer, SWT.LEFT);
	        methodLabel.setText("From &Method");
	        methodLabel.setBackground(Colors.ADDNEWBG);
	        methodLabel.setLayoutData(labelData);
	
	        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	        fromMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
	        ArrayList<Method> methFiltered = new ArrayList<Method>();
	        methFiltered.addAll(AbstractReferenceData.getCashMethods());
//	        methFiltered.addAll(AbstractReferenceData.getBankMethods());
	        Method[] meth = methFiltered.toArray(new Method[methFiltered.size()]);
	        fromMethodCombo.setData(meth);
	        for (Method s : meth) {
	        	fromMethodCombo.add(s.getName());
	        }
	        fromMethodCombo.addSelectionListener(defaultSelectionListener);
	        fromMethodCombo.select(0);
	        fromMethodCombo.setLayoutData(gridData);

	        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	        methodLabel = new Label(expenseContainer, SWT.LEFT);
	        methodLabel.setText("To M&ethod");
	        methodLabel.setBackground(Colors.ADDNEWBG);
	        methodLabel.setLayoutData(labelData);
	
	        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	        toMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
	        toMethodCombo.setData(meth);
	        for (Method s : meth) {
	        	toMethodCombo.add(s.getName());
	        }
	        toMethodCombo.addSelectionListener(defaultSelectionListener);
	        toMethodCombo.select(0);
	        toMethodCombo.setLayoutData(gridData);
        } else if (currentTransType.equals("Transfer: Bank")) {
	        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	        Label methodLabel = new Label(expenseContainer, SWT.LEFT);
	        methodLabel.setText("From &Method");
	        methodLabel.setBackground(Colors.ADDNEWBG);
	        methodLabel.setLayoutData(labelData);
	
	        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	        fromMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
	        ArrayList<Method> methFiltered = new ArrayList<Method>();
//	        methFiltered.addAll(AbstractReferenceData.getCashMethods());
	        methFiltered.addAll(AbstractReferenceData.getBankMethods());
	        Method[] meth = methFiltered.toArray(new Method[methFiltered.size()]);
	        fromMethodCombo.setData(meth);
	        for (Method s : meth) {
	        	fromMethodCombo.add(s.getName());
	        }
	        fromMethodCombo.addSelectionListener(defaultSelectionListener);
	        fromMethodCombo.select(0);
	        fromMethodCombo.setLayoutData(gridData);

	        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	        methodLabel = new Label(expenseContainer, SWT.LEFT);
	        methodLabel.setText("To M&ethod");
	        methodLabel.setBackground(Colors.ADDNEWBG);
	        methodLabel.setLayoutData(labelData);
	
	        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	        toMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
	        toMethodCombo.setData(meth);
	        for (Method s : meth) {
	        	toMethodCombo.add(s.getName());
	        }
	        toMethodCombo.addSelectionListener(defaultSelectionListener);
	        toMethodCombo.select(0);
	        toMethodCombo.setLayoutData(gridData);
        } else {
	        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
	        Label methodLabel = new Label(expenseContainer, SWT.LEFT);
	        methodLabel.setText("From &Method");
	        methodLabel.setBackground(Colors.ADDNEWBG);
	        methodLabel.setLayoutData(labelData);
	
	        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
	        fromMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
	        ArrayList<Method> methFiltered = new ArrayList<Method>();
	        methFiltered.addAll(AbstractReferenceData.getCashMethods());
	        methFiltered.addAll(AbstractReferenceData.getBankMethods());
	        Method[] meth = methFiltered.toArray(new Method[methFiltered.size()]);
	        fromMethodCombo.setData(meth);
	        for (Method s : meth) {
	        	fromMethodCombo.add(s.getName());
	        }
	        fromMethodCombo.addSelectionListener(defaultSelectionListener);
	        fromMethodCombo.select(0);
	        fromMethodCombo.setLayoutData(gridData);
        }
    }
    
    private void showAtm() {
		GridLayout sectionLayout = new GridLayout();
		sectionLayout.verticalSpacing = 10;
		sectionLayout.marginHeight = 0;
		sectionLayout.marginWidth = 0;
		sectionLayout.numColumns = 2;
		sectionLayout.makeColumnsEqualWidth=true;

		GridData sectionData = null;
		sectionData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		Composite expenseContainer = new Composite(flexContainer, SWT.NONE);
		expenseContainer.setLayout(sectionLayout);
		expenseContainer.setLayoutData(sectionData);
		expenseContainer.setBackground(Colors.ADDNEWBG);

		GridData labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label accountLabel = new Label(expenseContainer, SWT.LEFT);
        accountLabel.setText("&Account");
        accountLabel.setBackground(Colors.ADDNEWBG);
        accountLabel.setLayoutData(labelData);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        fromAccountCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Account[] acct = AbstractReferenceData.getAccounts().toArray(new Account[AbstractReferenceData.getAccounts().size()]);
        fromAccountCombo.setData(acct);
        for (Account s : acct) {
        	fromAccountCombo.add(s.getName());
        }
        fromAccountCombo.addSelectionListener(defaultSelectionListener);
        fromAccountCombo.select(0);
        fromAccountCombo.setLayoutData(gridData);
        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label categoryLabel = new Label(expenseContainer, SWT.LEFT);
        categoryLabel.setText("&Atm Type");
        categoryLabel.setBackground(Colors.ADDNEWBG);
        categoryLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        categoryCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Category[] cat = AbstractReferenceData.getCategories(4).toArray(new Category[AbstractReferenceData.getCategories(4).size()]);
        categoryCombo.setData(cat);
        for (Category s : cat) {
        	categoryCombo.add(s.getName());
        }
        categoryCombo.addSelectionListener(defaultSelectionListener);
        categoryCombo.select(0);
        categoryCombo.setLayoutData(gridData);

        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label methodLabel = new Label(expenseContainer, SWT.LEFT);
        methodLabel.setText("&Bank");
        methodLabel.setBackground(Colors.ADDNEWBG);
        methodLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        fromMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Method[] meth = AbstractReferenceData.getBankMethods().toArray(new Method[AbstractReferenceData.getBankMethods().size()]);
        fromMethodCombo.setData(meth);
        for (Method s : meth) {
        	fromMethodCombo.add(s.getName());
        }
        fromMethodCombo.addSelectionListener(defaultSelectionListener);
        fromMethodCombo.select(0);
        fromMethodCombo.setLayoutData(gridData);
        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        methodLabel = new Label(expenseContainer, SWT.LEFT);
        methodLabel.setText("&Cash");
        methodLabel.setBackground(Colors.ADDNEWBG);
        methodLabel.setLayoutData(labelData);

        meth = AbstractReferenceData.getCashMethods().toArray(new Method[AbstractReferenceData.getCashMethods().size()]);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        toMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        toMethodCombo.setData(meth);
        for (Method s : meth) {
        	toMethodCombo.add(s.getName());
        }
        toMethodCombo.addSelectionListener(defaultSelectionListener);
        toMethodCombo.select(0);
        toMethodCombo.setLayoutData(gridData);

    }
    
    private void showExpense() {
		GridLayout sectionLayout = new GridLayout();
		sectionLayout.verticalSpacing = 10;
		sectionLayout.marginHeight = 0;
		sectionLayout.marginWidth = 0;
		sectionLayout.numColumns = 2;
		sectionLayout.makeColumnsEqualWidth=true;

		GridData sectionData = null;
		sectionData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		Composite expenseContainer = new Composite(flexContainer, SWT.NONE);
		expenseContainer.setLayout(sectionLayout);
		expenseContainer.setLayoutData(sectionData);
		expenseContainer.setBackground(Colors.ADDNEWBG);

		GridData labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label accountLabel = new Label(expenseContainer, SWT.LEFT);
        accountLabel.setText("&Account");
        accountLabel.setBackground(Colors.ADDNEWBG);
        accountLabel.setLayoutData(labelData);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        fromAccountCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Account[] acct = AbstractReferenceData.getAccounts().toArray(new Account[AbstractReferenceData.getAccounts().size()]);
        fromAccountCombo.setData(acct);
        for (Account s : acct) {
        	fromAccountCombo.add(s.getName());
        }
        fromAccountCombo.addSelectionListener(defaultSelectionListener);
        fromAccountCombo.select(0);
        fromAccountCombo.setLayoutData(gridData);
        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label categoryLabel = new Label(expenseContainer, SWT.LEFT);
        categoryLabel.setText("&Category");
        categoryLabel.setBackground(Colors.ADDNEWBG);
        categoryLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        categoryCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        Category[] cat = AbstractReferenceData.getCategories(1).toArray(new Category[AbstractReferenceData.getCategories(1).size()]);
        categoryCombo.setData(cat);
        for (Category s : cat) {
        	categoryCombo.add(s.getName());
        }
        categoryCombo.addSelectionListener(defaultSelectionListener);
        categoryCombo.select(0);
        categoryCombo.setLayoutData(gridData);

        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label methodLabel = new Label(expenseContainer, SWT.LEFT);
        methodLabel.setText("&Payment Method");
        methodLabel.setBackground(Colors.ADDNEWBG);
        methodLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        fromMethodCombo = new Combo(expenseContainer, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        ArrayList<Method> methFiltered = new ArrayList<Method>();
        methFiltered.addAll(AbstractReferenceData.getCashMethods());
        methFiltered.addAll(AbstractReferenceData.getBankMethods());
        methFiltered.addAll(AbstractReferenceData.getCreditCardMethods());
        Method[] meth = methFiltered.toArray(new Method[methFiltered.size()]);
        fromMethodCombo.setData(meth);
        for (Method s : meth) {
        	fromMethodCombo.add(s.getName());
        }
        fromMethodCombo.addSelectionListener(defaultSelectionListener);
        fromMethodCombo.select(0);
        fromMethodCombo.setLayoutData(gridData);
        
        labelData = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
        Label descLabel = new Label(expenseContainer, SWT.LEFT);
        descLabel.setText("D&escription");
        descLabel.setBackground(Colors.ADDNEWBG);
        descLabel.setLayoutData(labelData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        descriptionText = new Text(expenseContainer, SWT.BORDER);
        descriptionText.setLayoutData(gridData);
        descriptionText.addSelectionListener(defaultSelectionListener);
        descriptionText.addFocusListener(textFocusListener);
        autoComplete = new AutoCompleteField(
        		descriptionText, 
        		new TextContentAdapter(), 
        		AbstractReferenceData.getDescriptionProposalTextArray()
        		);
    }
}