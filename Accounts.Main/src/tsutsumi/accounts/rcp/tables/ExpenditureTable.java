package tsutsumi.accounts.rcp.tables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.response.BigDecimalResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.dialogs.ireport.IReport;
import tsutsumi.accounts.rcp.views.FilterView;
import tsutsumi.accounts.rcp.views.SummaryView;
import tsutsumi.accounts.rcp.views.SummaryView.SubListener;

public class ExpenditureTable implements AbstractTable {
	public static final int BY_CATEGORY = 1;
	public static final int BY_ACCOUNT = 2;
	private HashMap<Label, AbstractReferenceData> labelLinkMap;
	
	private static HashMap<Integer, String> labelMap = new HashMap<Integer, String>();
	static {
		labelMap.put(BY_CATEGORY, "Category");
		labelMap.put(BY_ACCOUNT, "Account");
	}
	
	private HashMap<AbstractReferenceData, Label> refDataMap;
	private Label total;
	private BigDecimal runningTotal = new BigDecimal(0);
	private int type;
	private SubListener mListen;
	
	public ExpenditureTable(Composite container, int type) {
		refDataMap = new HashMap<AbstractReferenceData, Label>();
		labelLinkMap = new HashMap<Label, AbstractReferenceData>();
		this.type = type;
        mListen = SummaryView.getDefault().mListener;
        mListen.registerTable(this);

		GridData containerData = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		Composite largeContainer = new Composite(container, SWT.BORDER);
		GridLayout largeLout = new GridLayout();
		largeLout.verticalSpacing=1;
		largeLout.horizontalSpacing = 1;
		largeLout.marginHeight = 0;
		largeLout.marginWidth = 0;
		largeLout.numColumns = 1;
		largeContainer.setLayout(largeLout);
		largeContainer.setLayoutData(containerData);
		
		Composite sumContainer = new Composite(largeContainer, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing=1;
		layout.horizontalSpacing = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth=true;
		sumContainer.setLayout(layout);
		sumContainer.setLayoutData(containerData);
		
		GridData dataLayout = null;
		
		GridLayout dataL = new GridLayout();
		dataL.marginHeight = 3;
		dataL.marginWidth = 3;
		dataL.horizontalSpacing = 0;
		dataL.verticalSpacing = 0;
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite labelComp = new Composite(sumContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(dataLayout);
		labelComp.setBackground(Colors.PINKHEADER);
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        Label defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText(labelMap.get(type));
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);
        defLabel.addMouseListener(mListen);
        labelLinkMap.put(defLabel, null);
        
        dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite amtComp = new Composite(sumContainer, SWT.NONE);
		amtComp.setLayout(dataL);
		amtComp.setLayoutData(dataLayout);
		amtComp.setBackground(Colors.PINKHEADER);
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        Label amtLabel = new Label(amtComp, SWT.RIGHT);
        amtLabel.setText("Amount");
        amtLabel.setBackground(Colors.PINKHEADER);
        amtLabel.setLayoutData(dataLayout);
        amtLabel.addMouseListener(mListen);
        labelLinkMap.put(amtLabel, null);
        
        GridData itemListData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
        Composite itemListComp = new Composite(sumContainer, SWT.NONE);
        itemListComp.setLayout(largeLout);
        itemListComp.setLayoutData(itemListData);
        createList(itemListComp);

        dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totalComp = new Composite(sumContainer, SWT.NONE);
		totalComp.setLayout(dataL);
		totalComp.setLayoutData(dataLayout);
		totalComp.setBackground(Colors.PINKHEADER);
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        Label totLabel = new Label(totalComp, SWT.LEFT);
        totLabel.setText("Total");
        totLabel.setBackground(Colors.PINKHEADER);
        totLabel.setLayoutData(dataLayout);
        totLabel.addMouseListener(mListen);
        labelLinkMap.put(totLabel, null);

        dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totAmtComp = new Composite(sumContainer, SWT.NONE);
		totAmtComp.setLayout(dataL);
		totAmtComp.setLayoutData(dataLayout);
		totAmtComp.setBackground(Colors.PINKHEADER);
		
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        total = new Label(totAmtComp, SWT.RIGHT);
        total.setText(Utils.formatNumber(runningTotal));
        total.setBackground(Colors.PINKHEADER);
        total.setLayoutData(dataLayout);
        total.addMouseListener(mListen);
        labelLinkMap.put(total, null);
	}
	
	public void updateAll() {
		ArrayList<? extends AbstractReferenceData> objList = null;
		if (type==BY_CATEGORY) {
			objList = AbstractReferenceData.getCategories(1);
		} else if (type== BY_ACCOUNT){
			objList = AbstractReferenceData.getAccounts();
		}
		for (AbstractReferenceData r : objList) {
			update(r);
		}
	}
	
	public void update(AbstractReferenceData r) {
		if (refDataMap.containsKey(r)) {
			Label labe = refDataMap.get(r);
			BigDecimal prevAmt = (BigDecimal)labe.getData();
			BigDecimal newAmt = this.getValue(r.getId());
			labe.setData(newAmt);
			labe.setText(Utils.formatNumber(newAmt));
			runningTotal = runningTotal.subtract(prevAmt).add(newAmt);
			total.setText(Utils.formatNumber(runningTotal));
		}
			
	}
	
	private void createList(Composite parent) {
		ArrayList<? extends AbstractReferenceData> objList = null;
		if (type==BY_CATEGORY) {
			objList = AbstractReferenceData.getCategories(1);
		} else if (type== BY_ACCOUNT){
			objList = AbstractReferenceData.getAccounts();
		}

		GridLayout dataL = new GridLayout();
		dataL.marginHeight = 3;
		dataL.marginWidth = 3;
		dataL.horizontalSpacing = 0;
		dataL.verticalSpacing = 0;
        runningTotal = new BigDecimal(0);
		Composite sumContainer = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing=1;
		layout.horizontalSpacing = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth=true;
		sumContainer.setLayout(layout);
		GridData containerData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		sumContainer.setLayoutData(containerData);
        boolean colorSwitch = true;
        Color currentColor = Colors.WHITE;
        GridData dataLayout = null;
		for (AbstractReferenceData obj : objList) {
			colorSwitch = !colorSwitch;
			if (colorSwitch) {
				currentColor = Colors.PINK;
			} else {
				currentColor = Colors.WHITE;
			}
			int typeId = obj.getId();
			BigDecimal amount = this.getValue(typeId);
			
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Composite labelComp = new Composite(sumContainer, SWT.NONE);
			labelComp.setLayout(dataL);
			labelComp.setLayoutData(dataLayout);
			labelComp.setBackground(currentColor);
			
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
	        Label defLabel = new Label(labelComp, SWT.LEFT);
	        defLabel.setText(obj.getName());
	        defLabel.setBackground(currentColor);
	        defLabel.setLayoutData(dataLayout);
	        defLabel.setData(obj);
	        defLabel.addMouseListener(mListen);
	        labelLinkMap.put(defLabel, obj);
	
	        dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Composite amtComp = new Composite(sumContainer, SWT.NONE);
			amtComp.setLayout(dataL);
			amtComp.setLayoutData(dataLayout);
			amtComp.setBackground(currentColor);
			
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
	        Label amtLabel = new Label(amtComp, SWT.RIGHT);
	        amtLabel.setText(Utils.formatNumber(amount));
	        amtLabel.setBackground(currentColor);
	        amtLabel.setLayoutData(dataLayout);
	        amtLabel.setData(amount);
	        amtLabel.addMouseListener(mListen);
	        labelLinkMap.put(amtLabel, obj);
	        refDataMap.put(obj, amtLabel);
	        runningTotal = runningTotal.add(amount);
		}
	}
	
	private BigDecimal getValue(int id) {
		FilterView curView = FilterView.getDefault();
		String sDate;
		String eDate;
		if (curView != null) {
			sDate = FilterView.getDefault().getStartDateString();
			eDate = FilterView.getDefault().getEndDateString();
		} else {
			sDate = "1900/01/01";
			eDate = Utils.getToday();
		}
		
		BigDecimal returnValue = new BigDecimal(0);
		XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.GET_EXPENDITURE_SUMMARY);
    	xmlRequest.setCommandParams("TYPE",String.valueOf(type));
    	xmlRequest.setCommandParams("ID",String.valueOf(id));
    	xmlRequest.setCommandParams("TO_DATE",eDate);
    	xmlRequest.setCommandParams("FROM_DATE",sDate);
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
    		BigDecimalResponse sumresponse = (BigDecimalResponse)rr;
    		return sumresponse.getValue();
    	}
    	return returnValue;
	}
	
	public void processDoubleClick(Label e) {
		IReport iReport = new IReport();
		String startDate = FilterView.getDefault().getStartDateString();
		String endDate = FilterView.getDefault().getEndDateString();
		iReport.setStartDate(startDate);
		iReport.setEndDate(endDate);
		AbstractReferenceData refData = labelLinkMap.get(e);
		if (refData instanceof Method) {
			iReport.setMethod((Method)refData);
		} else if (refData instanceof Account) {
			iReport.setAccount((Account)refData);
		} else if (refData instanceof Category) {
			iReport.setCategory((Category)refData);
		}
		iReport.open();
	}
	
	public boolean hasLabel(Label e) {
		return labelLinkMap.containsKey(e);
	}
}

