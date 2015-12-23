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
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.response.BigDecimalResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.dialogs.ireport.IReport;
import tsutsumi.accounts.rcp.views.FilterView;
import tsutsumi.accounts.rcp.views.SummaryView;
import tsutsumi.accounts.rcp.views.SummaryView.SubListener;

public class SummaryTable implements AbstractTable {
	private HashMap<Method, HashMap<Account, Label>> labelMap;
	private HashMap<Method, Label> methodTotals;
	private HashMap<Account, Label> accountTotals;
	private Label total;
	private HashMap<Label, AbstractReferenceData[]> labelLinkMap;

	public SummaryTable(Composite container) {
		labelMap = new HashMap<Method, HashMap<Account, Label>>();
		labelLinkMap = new HashMap<Label, AbstractReferenceData[]>();
		
		accountTotals = new HashMap<Account, Label>();
		methodTotals = new HashMap<Method, Label>();
        BigDecimal totalTotal = new BigDecimal(0);
        SubListener mListen = SummaryView.getDefault().mListener;
        mListen.registerTable(this);
		
		ArrayList<Method> methodList = new ArrayList<Method>();
		methodList.addAll(AbstractReferenceData.getCashMethods());
		methodList.addAll(AbstractReferenceData.getBankMethods());
		methodList.addAll(AbstractReferenceData.getCreditCardMethods());
		ArrayList<Account> accountList = AbstractReferenceData.getAccounts();
		
		Composite sumContainer = new Composite(container, SWT.BORDER);
//		sumContainer.setBackground(bgColor);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing=1;
		layout.horizontalSpacing = 1;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = methodList.size() + 2;
		layout.makeColumnsEqualWidth=true;
		sumContainer.setLayout(layout);
		GridData containerData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		sumContainer.setLayoutData(containerData);
		
		GridData dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		GridData tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		GridLayout dataL = new GridLayout();
		dataL.marginHeight = 3;
		dataL.marginWidth = 3;
		dataL.horizontalSpacing = 0;
		dataL.verticalSpacing = 0;
		Composite labelComp = new Composite(sumContainer, SWT.NONE);
		labelComp.setLayout(dataL);
		labelComp.setLayoutData(tableLayout);
		labelComp.setBackground(Colors.PINKHEADER);
        Label defLabel = new Label(labelComp, SWT.LEFT);
        defLabel.setText("Account");
        defLabel.setBackground(Colors.PINKHEADER);
        defLabel.setLayoutData(dataLayout);
        defLabel.addMouseListener(mListen);
        labelLinkMap.put(defLabel, null);
        HashMap<Method, BigDecimal> methodTotal = new HashMap<Method, BigDecimal>();
        
		for (Method m : methodList) {
			Color headColor = null;
			if (m.getTypeId()==1 || m.getTypeId()==3) {
				headColor = Colors.BLUEHEADER;
			} else {
				headColor = Colors.PURPLEHEADER;
			}
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Composite methodComp = new Composite(sumContainer, SWT.NONE);
			methodComp.setLayout(dataL);
			methodComp.setLayoutData(tableLayout);
			methodComp.setBackground(headColor);
	        Label methLabel = new Label(methodComp, SWT.RIGHT);
	        methLabel.setText(m.getName());
	        methLabel.setBackground(headColor);
	        methLabel.addMouseListener(mListen);
//	        methLabel.setData(m);
	        methLabel.setLayoutData(dataLayout);
	        methodTotal.put(m, new BigDecimal(0));
	        labelLinkMap.put(methLabel, new AbstractReferenceData[]{m});
		}
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite sumComp = new Composite(sumContainer, SWT.NONE);
		sumComp.setLayout(dataL);
		sumComp.setLayoutData(tableLayout);
		sumComp.setBackground(Colors.PINKHEADER);
        Label sumLabel = new Label(sumComp, SWT.RIGHT);
        sumLabel.setText("Sum");
        sumLabel.setBackground(Colors.PINKHEADER);
        sumLabel.setLayoutData(dataLayout);
        sumLabel.addMouseListener(mListen);
        boolean colorSwitch = true;
        Color currentColor = Colors.WHITE;
		for (Account a : accountList) {
			colorSwitch = !colorSwitch;
			if (colorSwitch) {
				currentColor = Colors.PINK;
			} else {
				currentColor = Colors.WHITE;
			}
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			BigDecimal runningTotal = new BigDecimal(0);
			Composite accountComp = new Composite(sumContainer, SWT.NONE);
			accountComp.setLayout(dataL);
			accountComp.setLayoutData(tableLayout);
			accountComp.setBackground(currentColor);
	        Label accountLabel = new Label(accountComp, SWT.LEFT);
	        accountLabel.setText(a.getName());
	        accountLabel.setBackground(currentColor);
	        accountLabel.addMouseListener(mListen);
//	        accountLabel.setData(a);
	        labelLinkMap.put(accountLabel, new AbstractReferenceData[]{a});
	        accountLabel.setLayoutData(dataLayout);
			for (Method m : methodList) {
				if (colorSwitch) {
					if (m.getTypeId()==3 || m.getTypeId()==1) {
						currentColor = Colors.BLUE;
					} else {
						currentColor = Colors.PURPLE;
					}
				} else {
					currentColor = Colors.WHITE;
				}
				dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
				Composite amountComp = new Composite(sumContainer, SWT.NONE);
				amountComp.setLayout(dataL);
				amountComp.setLayoutData(tableLayout);
				amountComp.setBackground(currentColor);
		        Label amountLabel = new Label(amountComp, SWT.RIGHT);
		        amountLabel.setBackground(currentColor);
		        amountLabel.setLayoutData(dataLayout);
		        amountLabel.addMouseListener(mListen);
//		        amountLabel.setData(new AbstractReferenceData[] {m, a});
		        labelLinkMap.put(amountLabel, new AbstractReferenceData[]{m, a});
	    		if (!labelMap.containsKey(m)) {
	    			labelMap.put(m, new HashMap<Account, Label>());
	    		}
	    		labelMap.get(m).put(a, amountLabel);
	    		BigDecimal value = getAmount(m, a);
	    		amountLabel.setText(Utils.formatNumber(value));
	    		runningTotal = runningTotal.add(value);
	    		methodTotal.put(m, methodTotal.get(m).add(value));
	    		totalTotal = totalTotal.add(value);
	    		amountLabel.setData(value);
			}
			if (colorSwitch) {
				currentColor = Colors.PINK;
			} else {
				currentColor = Colors.WHITE;
			}
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Composite indivSumComp = new Composite(sumContainer, SWT.NONE);
			indivSumComp.setLayout(dataL);
			indivSumComp.setLayoutData(tableLayout);
			indivSumComp.setBackground(currentColor);
			
	        Label indivSumLabel = new Label(indivSumComp, SWT.RIGHT);
	        indivSumLabel.setBackground(currentColor);
	        indivSumLabel.setLayoutData(dataLayout);
	        indivSumLabel.setText(Utils.formatNumber(runningTotal));
	        indivSumLabel.setData(runningTotal);
	        indivSumLabel.addMouseListener(mListen);
	        labelLinkMap.put(indivSumLabel, new AbstractReferenceData[]{a});
	        accountTotals.put(a, indivSumLabel);
		}
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite totalComp = new Composite(sumContainer, SWT.NONE);
		totalComp.setLayout(dataL);
		totalComp.setLayoutData(tableLayout);
		totalComp.setBackground(Colors.PINKHEADER);
        Label totalDefLabel = new Label(totalComp, SWT.LEFT);
        totalDefLabel.setText("Totals");
        totalDefLabel.setLayoutData(dataLayout);
        totalDefLabel.setBackground(Colors.PINKHEADER);
        totalDefLabel.addMouseListener(mListen);
		for (Method m : methodList) {
			Color headColor = null;
			if (m.getTypeId()==1 || m.getTypeId()==3) {
				headColor = Colors.BLUEHEADER;
			} else {
				headColor = Colors.PURPLEHEADER;
			}

			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Composite methodComp = new Composite(sumContainer, SWT.NONE);
			methodComp.setLayout(dataL);
			methodComp.setLayoutData(tableLayout);
			methodComp.setBackground(headColor);
	        Label methLabel = new Label(methodComp, SWT.RIGHT);
	        methLabel.setBackground(headColor);
	        methLabel.setLayoutData(dataLayout);
	        methLabel.setText(Utils.formatNumber(methodTotal.get(m)));
	        methLabel.setData(methodTotal.get(m));
	        methLabel.addMouseListener(mListen);
	        labelLinkMap.put(methLabel, new AbstractReferenceData[]{m});
	        methodTotals.put(m, methLabel);
		}
		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Composite sumTotalComp = new Composite(sumContainer, SWT.NONE);
		sumTotalComp.setLayout(dataL);
		sumTotalComp.setLayoutData(tableLayout);
		sumTotalComp.setBackground(Colors.PINKHEADER);
        total = new Label(sumTotalComp, SWT.RIGHT);
        total.setBackground(Colors.PINKHEADER);
        total.setLayoutData(dataLayout);
        total.setText(Utils.formatNumber(totalTotal));
        total.setData(totalTotal);
        total.addMouseListener(mListen);
	}
	
	private BigDecimal getAmount(Method m, Account a) {
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
    	xmlRequest.setCommand(CommandEnum.GET_INDIVIDUAL_SUMMARY);
    	xmlRequest.setCommandParams("ACCOUNT_ID",String.valueOf(a.getId()));
    	xmlRequest.setCommandParams("METHOD_ID",String.valueOf(m.getId()));
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
	
	public void updateAllAmount() {
		for (Method m : labelMap.keySet()) {
			for (Account a : labelMap.get(m).keySet()) {
				updateAmount(m, a);
			}
		}
	}
	
	public void updateAmount(final Method m, final Account a) {
		updateLocal(m, a);
	}
	
	private void updateLocal(Method m, Account a)  {
		Label label = labelMap.get(m).get(a);
		BigDecimal previousValue = (BigDecimal)label.getData(); 
		BigDecimal value = getAmount(m, a);
		label.setText(Utils.formatNumber(value));
		label.setData(value);
		
		Label methodTotal = methodTotals.get(m);
		BigDecimal previousMethodTotal = (BigDecimal)methodTotal.getData();
		BigDecimal newMethodTotal = previousMethodTotal.subtract(previousValue).add(value);
		methodTotal.setData(newMethodTotal);
		methodTotal.setText(Utils.formatNumber(newMethodTotal));
		
		Label accountTotal = accountTotals.get(a);
		BigDecimal previousAccountTotal = (BigDecimal)accountTotal.getData();
		BigDecimal newAccountTotal = previousAccountTotal.subtract(previousValue).add(value);
		accountTotal.setData(newAccountTotal);
		accountTotal.setText(Utils.formatNumber(newAccountTotal));
		
		BigDecimal previousTotal = (BigDecimal)total.getData();
		BigDecimal newTotal = previousTotal.subtract(previousValue).add(value);
		total.setData(newTotal);
		total.setText(Utils.formatNumber(newTotal));
	}
	
	public void processDoubleClick(Label e) {
		AbstractReferenceData[] refData = labelLinkMap.get(e);
		Method m = null;
		Account a = null;
		for (int i=0; refData != null && i < refData.length; i++) {
			if (refData[i] instanceof Method) {
				m = (Method)refData[i];
//				System.out.println("Method clicked");
			} else if (refData[i] instanceof Account) {
				a = (Account)refData[i];
//				System.out.println("Account clicked");
			}
		}
		IReport iReport = new IReport();
		String startDate = FilterView.getDefault().getStartDateString();
		String endDate = FilterView.getDefault().getEndDateString();
		iReport.setStartDate(startDate);
		iReport.setEndDate(endDate);
		if (m != null)
			iReport.setMethod(m);
		if (a != null)
			iReport.setAccount(a);
		iReport.open();
	}
	
	public boolean hasLabel(Label e) {
		return labelLinkMap.containsKey(e);
	}

	
}

