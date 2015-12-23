package tsutsumi.accounts.rcp.dialogs.ireport;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.ReportSummaryAnalyzer;
import tsutsumi.accounts.common.ReportSummaryRecord;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.response.ReportSummaryResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.dialogs.itransactions.ITransactions;

public class SummaryPart extends Composite {
	private IReport iReport;
	private Label currentHightlightedLabel;
	private Color previousColor;
	private HashMap<Label, int[]> labelLinkMap;
	private ScrolledComposite sc1;
	
	private Composite parent;
	private Composite reportContainer;
	private SubListener mListen;

	public SummaryPart(IReport iReport) {
		super(iReport.getShell(), SWT.NONE);
		this.iReport = iReport;
		this.parent = iReport.getShell();
		labelLinkMap = new HashMap<Label, int[]>();
		mListen = new SubListener();
		//labelLinkmap = type, category, account, method, creditshown, debitshown
		
    	GridLayout thisLayout = new GridLayout();
    	thisLayout.marginHeight = 0;
    	thisLayout.marginWidth = 0;
    	thisLayout.horizontalSpacing = 0;
    	thisLayout.verticalSpacing = 0;
    	this.setLayout(thisLayout);
    	
//    	
//    	
//		Composite darkBorderContainer = new Composite(parent, SWT.NONE);
//		GridLayout darkBorderLayout = new GridLayout();
//		darkBorderLayout.numColumns = 1;
//		darkBorderLayout.marginHeight = 1;
//		darkBorderLayout.marginWidth = 1;
//		darkBorderLayout.horizontalSpacing = 0;
//		darkBorderLayout.verticalSpacing = 0;
//		darkBorderContainer.setLayout(darkBorderLayout);
//		darkBorderContainer.setBackground(new Color(Display.getCurrent(), 130, 130, 130));
//		GridData borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
//		darkBorderContainer.setLayoutData(borderData);
//
//		Composite lightBorderContainer = new Composite(darkBorderContainer, SWT.NONE);
//		darkBorderLayout = new GridLayout();
//		darkBorderLayout.numColumns = 1;
//		darkBorderLayout.marginHeight = 0;
//		darkBorderLayout.marginWidth = 0;
//		darkBorderLayout.horizontalSpacing = 0;
//		darkBorderLayout.verticalSpacing = 0;
//		lightBorderContainer.setLayout(darkBorderLayout);
//		borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
//		lightBorderContainer.setLayoutData(borderData);
//		
//		Composite borderContainer = new Composite(lightBorderContainer, SWT.NONE);
//		GridLayout borderLayout = new GridLayout();
//		borderLayout.numColumns = 1;
//		borderLayout.marginHeight = 0;
//		borderLayout.marginWidth = 0;
//		borderLayout.horizontalSpacing = 0;
//		borderLayout.verticalSpacing = 0;
//		borderContainer.setLayout(borderLayout);
//		borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
//		borderContainer.setLayoutData(borderData);

    	
    	sc1 = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL |SWT.BORDER);
		sc1.setExpandHorizontal(true);
		sc1.setExpandVertical(true);
		sc1.getVerticalBar().setIncrement(sc1.getVerticalBar().getIncrement()*3);
		sc1.addListener(SWT.Activate, new Listener() {
		    public void handleEvent(Event e) {
		        sc1.setFocus();
		    }
		}); 
		GridData reportData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		sc1.setLayoutData(reportData);
		GridLayout borderLayout = new GridLayout();
		borderLayout.numColumns = 1;
		borderLayout.marginHeight = 0;
		borderLayout.marginWidth = 0;
		borderLayout.horizontalSpacing = 0;
		borderLayout.verticalSpacing = 0;
		sc1.setLayout(borderLayout);

		
		
		reportContainer = new Composite(sc1, SWT.NONE);
		GridLayout reportLayout = new GridLayout();
		reportLayout.numColumns = 1;
		reportLayout.marginHeight = 5;
		reportLayout.marginWidth = 5;
		reportLayout.horizontalSpacing = 0;
		reportLayout.verticalSpacing = 5;
		reportContainer.setLayout(reportLayout);
		reportData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		reportContainer.setLayoutData(reportData);
//		reportContainer.pack();
//		Point p = reportContainer.getSize();
//		populate();
		sc1.setContent(reportContainer);
		sc1.setMinSize(reportContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void populate() {
		for (Control c : reportContainer.getChildren()) {
			c.dispose();
		}
		labelLinkMap.clear();
		currentHightlightedLabel=null;
		previousColor = null;

		XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.GET_REPORT_SUMMARY);
    	//SWT.UP ? SWT.DOWN : SWT.UP
    	xmlRequest.setCommandParams("TYPE_ID",String.valueOf(iReport.getType()!=null ? iReport.getType().getId():0));
    	xmlRequest.setCommandParams("METHOD_ID",String.valueOf(iReport.getMethod()!=null ? iReport.getMethod().getId():0));
    	xmlRequest.setCommandParams("ACCOUNT_ID",String.valueOf(iReport.getAccount()!=null ? iReport.getAccount().getId():0));
    	xmlRequest.setCommandParams("CATEGORY_ID",String.valueOf(iReport.getCategory()!=null ? iReport.getCategory().getId():0));
    	xmlRequest.setCommandParams("TO_DATE",iReport.getEndDate());
    	xmlRequest.setCommandParams("FROM_DATE",iReport.getStartDate());
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
    		ReportSummaryResponse sumresponse = (ReportSummaryResponse)rr;
    		ArrayList<ReportSummaryRecord> rsr = sumresponse.getSummary();
    		ReportSummaryAnalyzer analyzer = new ReportSummaryAnalyzer();
    		analyzer.setRecords(rsr);
    		rsr = analyzer.getMethodResults();
    		createPortion("METHOD", rsr);
    		rsr = analyzer.getAccountResults();
    		createPortion("ACCOUNT", rsr);
    		rsr = analyzer.getCategoryResults();
    		createPortion("CATEGORY", rsr);
    		rsr = analyzer.getTypeResults();
    		createPortion("TYPE", rsr);

    	}
    	reportContainer.layout(true);
    	iReport.getShell().pack();
    	sc1.setMinSize(reportContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	
	private void createPortion(String type, ArrayList<ReportSummaryRecord> rsr) {
		String javaMethodName = "";
		String refDataMethodName = "";
		String labelString = "";
		
		int typeFg = 0;
		int methodFg = 0;
		int categoryFg = 0;
		int accountFg = 0;

		if (type.equals("METHOD")) {
			javaMethodName = "getMethodId";
			refDataMethodName = "resolveMethodFromId";
			labelString = "Payment Methods";
			methodFg = 1;
		} else if (type.equals("ACCOUNT")) {
			javaMethodName = "getAccountId";
			refDataMethodName = "resolveAccountFromId";
			labelString = "Accounts";
			accountFg = 1;
		} else if (type.equals("CATEGORY")) {
			javaMethodName = "getCategoryId";
			refDataMethodName = "resolveCategoryFromId";
			labelString = "Categories";
			categoryFg = 1;
		} else if (type.equals("TYPE")) {
			javaMethodName = "getTypeId";
			refDataMethodName = "resolveTypeFromId";
			labelString = "Types";
			typeFg = 1;
		}
		
		Composite darkBorderContainer = new Composite(reportContainer, SWT.NONE);
		GridLayout darkBorderLayout = new GridLayout();
		darkBorderLayout.numColumns = 1;
		darkBorderLayout.marginHeight = 1;
		darkBorderLayout.marginWidth = 1;
		darkBorderLayout.horizontalSpacing = 0;
		darkBorderLayout.verticalSpacing = 0;
		darkBorderContainer.setLayout(darkBorderLayout);
		darkBorderContainer.setBackground(Colors.BORDER);
		GridData borderData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		darkBorderContainer.setLayoutData(borderData);

		Composite borderContainer = new Composite(darkBorderContainer, SWT.NONE);
		GridLayout borderLayout = new GridLayout();
		borderLayout.numColumns = 1;
		borderLayout.marginHeight = 0;
		borderLayout.marginWidth = 0;
		borderLayout.horizontalSpacing = 1;
		borderLayout.verticalSpacing = 1;
		borderLayout.numColumns = 4;
		borderLayout.makeColumnsEqualWidth = true;
		borderContainer.setLayout(borderLayout);
		borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		borderContainer.setLayoutData(borderData);
		
		GridLayout dataL = new GridLayout();
		dataL.marginHeight = 3;
		dataL.marginWidth = 3;
		dataL.horizontalSpacing = 0;
		dataL.verticalSpacing = 0;

		GridData dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		GridData tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite labelComp = new Composite(borderContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(tableLayout);
		labelComp.setBackground(Colors.PINKHEADER);
        Label defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText(labelString);
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);
        defLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(defLabel, new int[]{0,0,0,0,1,1});

		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite creditComp = new Composite(borderContainer, SWT.NONE);
		creditComp.setLayout(dataL);
		creditComp.setLayoutData(tableLayout);
		creditComp.setBackground(Colors.PINKHEADER);
        Label creditLabel = new Label(creditComp, SWT.LEFT);
        creditLabel.setText("Credit");
        creditLabel.setBackground(Colors.PINKHEADER);
        creditLabel.setLayoutData(dataLayout);
        creditLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(creditLabel, new int[]{0,0,0,0,1,0});
        
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite debitComp = new Composite(borderContainer, SWT.NONE);
		debitComp.setLayout(dataL);
		debitComp.setLayoutData(tableLayout);
		debitComp.setBackground(Colors.PINKHEADER);
        Label debitLabel = new Label(debitComp, SWT.LEFT);
        debitLabel.setText("Debit");
        debitLabel.setBackground(Colors.PINKHEADER);
        debitLabel.setLayoutData(dataLayout);
        debitLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(debitLabel, new int[]{0,0,0,0,0,1});
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite balanceComp = new Composite(borderContainer, SWT.NONE);
		balanceComp.setLayout(dataL);
		balanceComp.setLayoutData(tableLayout);
		balanceComp.setBackground(Colors.PINKHEADER);
        Label balanceLabel = new Label(balanceComp, SWT.LEFT);
        balanceLabel.setText("Balance");
        balanceLabel.setBackground(Colors.PINKHEADER);
        balanceLabel.setLayoutData(dataLayout);
        balanceLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(balanceLabel, new int[]{0,0,0,0,1,1});
		
		GridLayout dataCompLayout = new GridLayout();
		dataCompLayout.marginHeight = 0;
		dataCompLayout.marginWidth = 0;
		dataCompLayout.horizontalSpacing = 1;
		dataCompLayout.verticalSpacing = 1;
		dataCompLayout.makeColumnsEqualWidth=true;
		dataCompLayout.numColumns=4;
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		Composite dataComp = new Composite(borderContainer, SWT.NONE);
		dataComp.setLayout(dataCompLayout);
		dataComp.setLayoutData(tableLayout);
        
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totalComp = new Composite(borderContainer, SWT.NONE);
		totalComp.setLayout(dataL);
		totalComp.setLayoutData(tableLayout);
		totalComp.setBackground(Colors.PINKHEADER);
        Label totalLabel = new Label(totalComp, SWT.LEFT);
        totalLabel.setText("Totals");
        totalLabel.setBackground(Colors.PINKHEADER);
        totalLabel.setLayoutData(dataLayout);
        totalLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(totalLabel, new int[]{0,0,0,0,1,1});

		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totalCreditComp = new Composite(borderContainer, SWT.NONE);
		totalCreditComp.setLayout(dataL);
		totalCreditComp.setLayoutData(tableLayout);
		totalCreditComp.setBackground(Colors.PINKHEADER);
		Label totalCreditLabel = new Label(totalCreditComp, SWT.RIGHT);
		totalCreditLabel.setText("");
        totalCreditLabel.setBackground(Colors.PINKHEADER);
        totalCreditLabel.setLayoutData(dataLayout);
        totalCreditLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(totalCreditLabel, new int[]{0,0,0,0,1,0});
        
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totalDebitComp = new Composite(borderContainer, SWT.NONE);
		totalDebitComp.setLayout(dataL);
		totalDebitComp.setLayoutData(tableLayout);
		totalDebitComp.setBackground(Colors.PINKHEADER);
		Label totalDebitLabel = new Label(totalDebitComp, SWT.RIGHT);
        totalDebitLabel.setText("Debit");
        totalDebitLabel.setBackground(Colors.PINKHEADER);
        totalDebitLabel.setLayoutData(dataLayout);
        totalDebitLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(totalDebitLabel, new int[]{0,0,0,0,0,1});
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totalBalanceComp = new Composite(borderContainer, SWT.NONE);
		totalBalanceComp.setLayout(dataL);
		totalBalanceComp.setLayoutData(tableLayout);
		totalBalanceComp.setBackground(Colors.PINKHEADER);
		Label totalBalanceLabel = new Label(totalBalanceComp, SWT.RIGHT);
        totalBalanceLabel.setText("Balance");
        totalBalanceLabel.setBackground(Colors.PINKHEADER);
        totalBalanceLabel.setLayoutData(dataLayout);
        totalBalanceLabel.addMouseListener(mListen);
		//labelLinkmap = type, category, account, method, creditshown, debitshown
        labelLinkMap.put(totalBalanceLabel, new int[]{0,0,0,0,1,1});

        boolean colorSwitch = true;
        Color currentColor = Colors.WHITE;
        BigDecimal runningCredit = new BigDecimal(0);
        BigDecimal runningDebit = new BigDecimal(0);
        if (rsr.size() > 0) {
			for (ReportSummaryRecord r : rsr) {
				java.lang.reflect.Method javaMethod = null;
				java.lang.reflect.Method refDataMethod = null;
				try {
					javaMethod = r.getClass().getMethod(javaMethodName);
					refDataMethod = AbstractReferenceData.class.getMethod(refDataMethodName, int.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
	
				colorSwitch = !colorSwitch;
				if (colorSwitch) {
					currentColor = Colors.PINK;
				} else {
					currentColor = Colors.WHITE;
				}
				dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				Composite labelComp2 = new Composite(dataComp, SWT.NONE);
				labelComp2.setLayout(dataL);
				labelComp2.setLayoutData(tableLayout);
				labelComp2.setBackground(currentColor);
		        Label defLabel2 = new Label(labelComp2, SWT.LEFT);
		        
		        AbstractReferenceData refData = null;
		        try {
		        	refData = (AbstractReferenceData)refDataMethod.invoke(AbstractReferenceData.class, javaMethod.invoke(r));
		        } catch (Exception e) {
		        	e.printStackTrace();
		        	return;
		        }
		        
		        defLabel2.setText(refData.getName());
		        defLabel2.setBackground(currentColor);
		        defLabel2.setLayoutData(dataLayout);
		        defLabel2.addMouseListener(mListen);
				//labelLinkmap = type, category, account, method, creditshown, debitshown
		        labelLinkMap.put(defLabel2, new int[]{
		        		mapInt(refData, typeFg),mapInt(refData, categoryFg),mapInt(refData, accountFg),mapInt(refData, methodFg),1,1});
	
				dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				Composite creditComp2 = new Composite(dataComp, SWT.NONE);
				creditComp2.setLayout(dataL);
				creditComp2.setLayoutData(tableLayout);
				creditComp2.setBackground(currentColor);
		        Label creditLabel2 = new Label(creditComp2, SWT.RIGHT);
		        creditLabel2.setText(Utils.formatNumber(r.getCredit()));
		        creditLabel2.setBackground(currentColor);
		        creditLabel2.setLayoutData(dataLayout);
		        creditLabel2.addMouseListener(mListen);
				//labelLinkmap = type, category, account, method, creditshown, debitshown
		        labelLinkMap.put(creditLabel2, new int[]{
		        		mapInt(refData, typeFg),mapInt(refData, categoryFg),mapInt(refData, accountFg),mapInt(refData, methodFg),1,0});
		        
				dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				Composite debitComp2 = new Composite(dataComp, SWT.NONE);
				debitComp2.setLayout(dataL);
				debitComp2.setLayoutData(tableLayout);
				debitComp2.setBackground(currentColor);
		        Label debitLabel2 = new Label(debitComp2, SWT.RIGHT);
		        debitLabel2.setText(Utils.formatNumber(r.getDebit()));
		        debitLabel2.setBackground(currentColor);
		        debitLabel2.setLayoutData(dataLayout);
		        debitLabel2.addMouseListener(mListen);
				//labelLinkmap = type, category, account, method, creditshown, debitshown
		        labelLinkMap.put(debitLabel2, new int[]{
		        		mapInt(refData, typeFg),mapInt(refData, categoryFg),mapInt(refData, accountFg),mapInt(refData, methodFg),0,1});
		        
				dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				Composite balanceComp2 = new Composite(dataComp, SWT.NONE);
				balanceComp2.setLayout(dataL);
				balanceComp2.setLayoutData(tableLayout);
				balanceComp2.setBackground(currentColor);
		        Label balanceLabel2 = new Label(balanceComp2, SWT.RIGHT);
		        balanceLabel2.setText(Utils.formatNumber(r.getCredit().subtract(r.getDebit())));
		        balanceLabel2.setBackground(currentColor);
		        balanceLabel2.setLayoutData(dataLayout);
		        balanceLabel2.addMouseListener(mListen);
				//labelLinkmap = type, category, account, method, creditshown, debitshown
		        labelLinkMap.put(balanceLabel2, new int[]{
		        		mapInt(refData, typeFg),mapInt(refData, categoryFg),mapInt(refData, accountFg),mapInt(refData, methodFg),1,1});
		        
		        runningCredit = runningCredit.add(r.getCredit());
		        runningDebit = runningDebit.add(r.getDebit());
			}
        } else {
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
			Composite labelComp2 = new Composite(dataComp, SWT.NONE);
			labelComp2.setLayout(dataL);
			labelComp2.setLayoutData(tableLayout);
			labelComp2.setBackground(currentColor);
	        Label defLabel2 = new Label(labelComp2, SWT.LEFT);
	        defLabel2.setLayoutData(dataLayout);
	        defLabel2.setBackground(currentColor);
	        defLabel2.setLayoutData(dataLayout);
	        defLabel2.setText("No data found");
        }
		totalCreditLabel.setText(Utils.formatNumber(runningCredit));
		totalDebitLabel.setText(Utils.formatNumber(runningDebit));
		totalBalanceLabel.setText(Utils.formatNumber(runningCredit.subtract(runningDebit)));

	}
	
	private static int mapInt(AbstractReferenceData refdata, int refFg) {
		if (refFg == 0) {
			return refFg;
		}
		return refdata.getId();
	}
	
	private class SubListener implements MouseListener {
		public void mouseDoubleClick(MouseEvent e) {
//			iReport.closeChild();
			int[] refData = labelLinkMap.get(e.widget);
			//labelLinkmap = type, category, account, method, creditshown, debitshown
			int t = refData[0];
			int c = refData[1];
			int a = refData[2];
			int m = refData[3];
			boolean creditShown = (refData[4]==1);
			boolean debitShown = (refData[5]==1);
			
			ITransactions tp = new ITransactions();
			tp.setAccount(iReport.getAccount()==null ? 0:iReport.getAccount().getId());
			tp.setMethod(iReport.getMethod()==null? 0:iReport.getMethod().getId());
			tp.setCategory(iReport.getCategory()==null ? 0: iReport.getCategory().getId());
			tp.setType(iReport.getType()==null ? 0:iReport.getType().getId());
			tp.setStartDate(iReport.getStartDate());
			tp.setEndDate(iReport.getEndDate());
			tp.setCreditShown(creditShown);
			tp.setDebitShown(debitShown);
			
//			System.out.println(a);
			
			if (t > 0)
				tp.setType(t);
			if (c > 0)
				tp.setCategory(c);
			if (a > 0) 
				tp.setAccount(a);
			if (m > 0)
				tp.setMethod(m);
			tp.open();
		}
		public void mouseDown(MouseEvent e) {
			if (currentHightlightedLabel!=null) {
				currentHightlightedLabel.setBackground(previousColor);
				currentHightlightedLabel.getParent().setBackground(previousColor);
			}
			currentHightlightedLabel= (Label)e.widget;
			previousColor = currentHightlightedLabel.getBackground();
			currentHightlightedLabel.setBackground(Colors.HIGHLIGHT);
			currentHightlightedLabel.getParent().setBackground(Colors.HIGHLIGHT);

		}
		public void mouseUp(MouseEvent e) {
		}
    }

}
