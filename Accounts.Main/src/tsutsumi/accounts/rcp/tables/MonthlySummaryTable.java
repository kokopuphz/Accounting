package tsutsumi.accounts.rcp.tables;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.MonthlySummaryTotals;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.response.MonthlySummaryResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.dialogs.ireport.IReport;
import tsutsumi.accounts.rcp.views.MonthlyReportFilterView;

public class MonthlySummaryTable {
//	private HashMap<Label, AbstractReferenceData[]> labelLinkMap;
	private Composite container;
	private ArrayList<String> rowMap = new ArrayList<String>();
	private ArrayList<Integer> columnMap = new ArrayList<Integer>();
	private BigDecimal zeroDecimal = new BigDecimal(0);
	private Label currentHightlightedLabel;
	private Color previousColor;

	private ArrayList<ArrayList<Label>> rowArray = new ArrayList<ArrayList<Label>>();
	private ArrayList<Label> currentSelectedRow;
	
	public MonthlySummaryTable(Composite container) {
//		labelLinkMap = new HashMap<Label, AbstractReferenceData[]>();
		this.container = container;
	}
	
	public void updateAllAmount() {
//		labelLinkMap.clear();
		rowMap.clear();
		columnMap.clear();
		rowArray.clear();
		
		currentHightlightedLabel=null;
		previousColor = null;

		for (Control c : container.getChildren()) {
			c.dispose();
		}
		SubListener mListen = new SubListener();

		String groupBy = "ACCOUNT";
		String showBy="BALANCE";
		if (MonthlyReportFilterView.getDefault() != null) {
			groupBy = MonthlyReportFilterView.getDefault().getCurrentGroupBy();
			showBy = MonthlyReportFilterView.getDefault().getCurrentShowBy();
		}
		int typeId = 1;
		if (showBy.equals("BALANCE")) 
			typeId = 0;
		if (showBy.equals("CREDIT")) 
			typeId = 2;
		if (showBy.equals("DEBIT")) 
			typeId = 1;
		
		
		XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.MONTHLY_SUMMARY);
    	xmlRequest.setCommandParams("GROUP_BY",groupBy);
//    	xmlRequest.setCommandParams("TYPE_ID", String.valueOf(typeId));
    	xmlRequest.setCommandParams("SHOW_BY", showBy);
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
    		MonthlySummaryResponse msr = (MonthlySummaryResponse)rr;
    		MonthlySummaryTotals mst = msr.getMonthlySummaryTotals();
    		ArrayList<AbstractReferenceData> headerList = new ArrayList<AbstractReferenceData>();
    		if (groupBy.equals("ACCOUNT"))
    			headerList.addAll(AbstractReferenceData.getAccounts());
    		if (groupBy.equals("METHOD")) {
    			headerList.addAll(AbstractReferenceData.getCashMethods());
    			headerList.addAll(AbstractReferenceData.getBankMethods());
    			headerList.addAll(AbstractReferenceData.getCreditCardMethods());
    		}
    		if (groupBy.equals("CATEGORY"))
    			headerList.addAll(AbstractReferenceData.getCategories(typeId));
    		
    		Composite sumContainer = new Composite(container, SWT.BORDER);
//    		sumContainer.setBackground(bgColor);
    		GridLayout layout = new GridLayout();
    		layout.verticalSpacing=1;
    		layout.horizontalSpacing = 1;
    		layout.marginHeight = 0;
    		layout.marginWidth = 0;
    		layout.numColumns = (headerList.size()*2) + 3;
    		layout.makeColumnsEqualWidth=true;
    		sumContainer.setLayout(layout);
    		GridData containerData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
    		sumContainer.setLayoutData(containerData);
    		
    		GridData dataLayout = new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1);
    		GridData tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
    		GridLayout dataL = new GridLayout();
    		dataL.marginHeight = 3;
    		dataL.marginWidth = 3;
    		dataL.horizontalSpacing = 0;
    		dataL.verticalSpacing = 0;
    		Composite labelComp = new Composite(sumContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(Colors.PINKHEADER);
            Label defLabel = new Label(labelComp, SWT.CENTER);
            defLabel.setText("Year/Month");
            defLabel.setBackground(Colors.PINKHEADER);
            defLabel.setLayoutData(dataLayout);
            defLabel.addMouseListener(mListen);
            defLabel.setData(new int[]{0,0});
            rowMap.add("");
            columnMap.add(0);
            boolean colorSwitch = true;
            int column = 1;
    		for (AbstractReferenceData m : headerList) {
    			Color currentHeaderColor=null;
    			if (colorSwitch) {
    				currentHeaderColor = Colors.BLUEHEADER;
    			} else {
    				currentHeaderColor = Colors.PURPLEHEADER;
    			}
    			colorSwitch = !colorSwitch;
    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
    			Composite methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(currentHeaderColor);
    	        Label methLabel = new Label(methodComp, SWT.CENTER);
    	        methLabel.setText(m.getName());
    	        methLabel.setBackground(currentHeaderColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        methLabel.setData(new int[] {0, column});
    	        columnMap.add(m.getId());
    	        columnMap.add(m.getId());
    	        column = column + 2;
//    	        labelLinkMap.put(methLabel, new AbstractReferenceData[]{m});

    		}
    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
    		labelComp = new Composite(sumContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(Colors.PINKHEADER);
            defLabel = new Label(labelComp, SWT.CENTER);
            defLabel.setText("Totals");
            defLabel.setBackground(Colors.PINKHEADER);
            defLabel.setLayoutData(dataLayout);
            defLabel.addMouseListener(mListen);
            columnMap.add(0);
            columnMap.add(0);
            colorSwitch = true;
            column = 1;
    		for (int i = 0; i < headerList.size(); i++ ) {
    			Color currentHeaderColor=null;
    			if (colorSwitch) {
    				currentHeaderColor = Colors.BLUEHEADER;
    			} else {
    				currentHeaderColor = Colors.PURPLEHEADER;
    			}
    			colorSwitch = !colorSwitch;

    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			Composite methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(currentHeaderColor);
    	        Label methLabel = new Label(methodComp, SWT.CENTER);
    	        if (showBy.equals("BALANCE")) 
    	        	methLabel.setText("Balance");
    	        if (showBy.equals("CREDIT"))
    	        	methLabel.setText("Credit");
    	        if (showBy.equals("DEBIT"))
    	        	methLabel.setText("Debit");

    	        methLabel.setBackground(currentHeaderColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        methLabel.setData(new int[]{0,column});
    	        column++;
    	        
    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(currentHeaderColor);
    	        methLabel = new Label(methodComp, SWT.CENTER);
    	        methLabel.setText("Total");
    	        methLabel.setBackground(currentHeaderColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        methLabel.setData(new int[]{0,column});
    	        column++;
    		}
            
    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    		labelComp = new Composite(sumContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(Colors.PINKHEADER);
            defLabel = new Label(labelComp, SWT.CENTER);
            defLabel.setText("Month");
            defLabel.setBackground(Colors.PINKHEADER);
            defLabel.setLayoutData(dataLayout);
            defLabel.addMouseListener(mListen);
            defLabel.setData(new int[]{0,column});
	        column++;

    		dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    		tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    		labelComp = new Composite(sumContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(Colors.PINKHEADER);
            defLabel = new Label(labelComp, SWT.CENTER);
            defLabel.setText("Total");
            defLabel.setBackground(Colors.PINKHEADER);
            defLabel.setLayoutData(dataLayout);
            defLabel.addMouseListener(mListen);
            defLabel.setData(new int[]{0,column});
	        column++;
            
            BigDecimal totalRunning = new BigDecimal(0);
            boolean firstRow = true;
            HashMap<Integer, BigDecimal> runningTotals = mst.getTotals();
            boolean rowSwitch = true;
        	Color blueLocal = Colors.BLUE;
        	Color purpleLocal = Colors.PURPLE;
        	Color rowColor = Colors.PINK;
        	int rownum = 0;
        	
        	rowArray.add(null);

        	
            for (String mmyy : mst.getYYYYMMList()) {
            	column = 0;
            	ArrayList<Label> rowLabels = new ArrayList<Label>();
            	rowArray.add(rowLabels);
    	        rownum++;
    	        rowMap.add(mmyy);
    			if (rowSwitch) {
    				blueLocal = Colors.WHITE;
    				purpleLocal = Colors.WHITE;
    				rowColor = Colors.WHITE;
    			} else {
    				blueLocal = Colors.BLUE;
    				purpleLocal = Colors.PURPLE;
    				rowColor = Colors.WHITE;
    			}
    			rowSwitch = !rowSwitch;

    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			Composite methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(rowColor);
    	        Label methLabel = new Label(methodComp, SWT.CENTER);
    	        methLabel.setText(mmyy);
    	        methLabel.setBackground(rowColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        methLabel.setData(new int[]{rownum, column});
    	        rowLabels.add(methLabel);
	        	column++;
	        	
    	        BigDecimal monthlyTots = new BigDecimal(0);
    	        colorSwitch = true;
    	        for (AbstractReferenceData m : headerList) {
        			Color currentColor=null;
        			if (colorSwitch) {
        				currentColor = blueLocal;
        			} else {
        				currentColor = purpleLocal;
        			}
        			colorSwitch = !colorSwitch;
        			BigDecimal monthTot = new BigDecimal(0);
        			monthTot = mst.getCredit(mmyy, m.getId()).subtract(mst.getDebit(mmyy, m.getId()));
    	        	
        			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        			methodComp = new Composite(sumContainer, SWT.NONE);
        			methodComp.setLayout(dataL);
        			methodComp.setLayoutData(tableLayout);
        			methodComp.setBackground(currentColor);
        	        methLabel = new Label(methodComp, SWT.RIGHT);
        	        methLabel.setText(Utils.formatNumber(monthTot));
        	        methLabel.setBackground(currentColor);
        	        methLabel.addMouseListener(mListen);
        	        methLabel.setLayoutData(dataLayout);
        	        if (monthTot.compareTo(zeroDecimal) == -1) {
        	        	methLabel.setForeground(Colors.RED);
        	        }
        	        methLabel.setData(new int[]{rownum, column});
        	        rowLabels.add(methLabel);
        	        column++;

        			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        			methodComp = new Composite(sumContainer, SWT.NONE);
        			methodComp.setLayout(dataL);
        			methodComp.setLayoutData(tableLayout);
        			methodComp.setBackground(currentColor);
        	        methLabel = new Label(methodComp, SWT.RIGHT);
        	        methLabel.setText(Utils.formatNumber(runningTotals.get(m.getId())));
        	        methLabel.setBackground(currentColor);
        	        methLabel.addMouseListener(mListen);
        	        methLabel.setLayoutData(dataLayout);
        	        if ((runningTotals.get(m.getId())==null ? zeroDecimal: runningTotals.get(m.getId())).compareTo(zeroDecimal) == -1) {
        	        	methLabel.setForeground(Colors.RED);
        	        }
        	        methLabel.setData(new int[]{rownum, column});
        	        rowLabels.add(methLabel);
        	        column++;

        	        monthlyTots = monthlyTots.add(monthTot);
        	        if (firstRow) {
        	        	totalRunning = totalRunning.add(runningTotals.get(m.getId())==null ? zeroDecimal: runningTotals.get(m.getId()));
        	        }
        	        runningTotals.put(m.getId(), (runningTotals.get(m.getId())==null ? zeroDecimal: runningTotals.get(m.getId()).subtract(monthTot)));
        	        
    	        }
    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(rowColor);
    	        methLabel = new Label(methodComp, SWT.RIGHT);
    	        methLabel.setText(Utils.formatNumber(monthlyTots));
    	        methLabel.setBackground(rowColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        if (monthlyTots.compareTo(zeroDecimal) == -1) {
    	        	methLabel.setForeground(Colors.RED);
    	        }
    	        methLabel.setData(new int[]{rownum, column});
    	        rowLabels.add(methLabel);
    	        column++;


    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(rowColor);
    	        methLabel = new Label(methodComp, SWT.RIGHT);
    	        methLabel.setText(Utils.formatNumber(totalRunning));
    	        methLabel.setBackground(rowColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        if (totalRunning.compareTo(zeroDecimal) == -1) {
    	        	methLabel.setForeground(Colors.RED);
    	        }
    	        methLabel.setData(new int[]{rownum, column});
    	        rowLabels.add(methLabel);
    	        column++;

    	        totalRunning = totalRunning.subtract(monthlyTots);
    	        firstRow = false;
            }
    		rownum++;
    		rowMap.add("");
    		column = 0;
    		labelComp = new Composite(sumContainer, SWT.NONE);
    		labelComp.setLayout(dataL);
    		labelComp.setLayoutData(tableLayout);
    		labelComp.setBackground(Colors.PINKHEADER);
            defLabel = new Label(labelComp, SWT.CENTER);
            defLabel.setText("Totals");
            defLabel.setBackground(Colors.PINKHEADER);
            defLabel.setLayoutData(dataLayout);
            defLabel.addMouseListener(mListen);
            defLabel.setData(new int[]{rownum, column});
            rowArray.add(null);
	        column++;

            colorSwitch = true;
            BigDecimal totTotal = new BigDecimal(0);
    		for (AbstractReferenceData m : headerList) {
    			Color currentHeaderColor=null;
    			if (colorSwitch) {
    				currentHeaderColor = Colors.BLUEHEADER;
    			} else {
    				currentHeaderColor = Colors.PURPLEHEADER;
    			}
    			colorSwitch = !colorSwitch;
    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			Composite methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(currentHeaderColor);
    	        Label methLabel = new Label(methodComp, SWT.RIGHT);
    	        methLabel.setText("----");
    	        methLabel.setBackground(currentHeaderColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        methLabel.setData(new int[]{rownum, column});
    	        column++;

    	        BigDecimal totAmount = mst.getTotal(m.getId());
    			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
    			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    			methodComp = new Composite(sumContainer, SWT.NONE);
    			methodComp.setLayout(dataL);
    			methodComp.setLayoutData(tableLayout);
    			methodComp.setBackground(currentHeaderColor);
    	        methLabel = new Label(methodComp, SWT.RIGHT);
    	        methLabel.setText(Utils.formatNumber(totAmount));
    	        methLabel.setBackground(currentHeaderColor);
    	        methLabel.addMouseListener(mListen);
    	        methLabel.setLayoutData(dataLayout);
    	        methLabel.setData(new int[]{rownum, column});
    	        column++;
    	        
    	        if (totAmount.compareTo(zeroDecimal) == -1) {
    	        	methLabel.setForeground(Colors.RED);
    	        }

    	        totTotal = totTotal.add(totAmount);

    		}
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			Composite methodComp = new Composite(sumContainer, SWT.NONE);
			methodComp.setLayout(dataL);
			methodComp.setLayoutData(tableLayout);
			methodComp.setBackground(Colors.PINKHEADER);
	        Label methLabel = new Label(methodComp, SWT.RIGHT);
	        methLabel.setText("----");
	        methLabel.setBackground(Colors.PINKHEADER);
	        methLabel.addMouseListener(mListen);
	        methLabel.setLayoutData(dataLayout);
	        methLabel.setData(new int[]{rownum, column});
	        column++;
	        
			dataLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			tableLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			methodComp = new Composite(sumContainer, SWT.NONE);
			methodComp.setLayout(dataL);
			methodComp.setLayoutData(tableLayout);
			methodComp.setBackground(Colors.PINKHEADER);
	        methLabel = new Label(methodComp, SWT.RIGHT);
	        methLabel.setText(Utils.formatNumber(totTotal));
	        methLabel.setBackground(Colors.PINKHEADER);
	        methLabel.addMouseListener(mListen);
	        methLabel.setLayoutData(dataLayout);
	        methLabel.setData(new int[]{rownum, column});
	        column++;


	        if (totTotal.compareTo(zeroDecimal) == -1) {
	        	methLabel.setForeground(Colors.RED);
	        }
    	}
	}
	
	private class SubListener implements MouseListener {
		public void mouseDoubleClick(MouseEvent e) {
			Label clickedLabel = (Label)e.widget;
			int[] rowcolumn = (int[])clickedLabel.getData();
			
			String YYYYMM = rowMap.get(rowcolumn[0]);
			int id = columnMap.get(rowcolumn[1]);
			String startDate = "1900/01/01";
			String endDate = Utils.getToday();
			if (YYYYMM.length()>0) {
				if(rowcolumn[1] > 0 && rowcolumn[1]%2==0) {
					//totals clicked
					startDate = "1900/01/01";
					endDate = Utils.convAccToEndString(YYYYMM);
				} else {
					startDate = Utils.convAccToStartString(YYYYMM);
					endDate = Utils.convAccToEndString(YYYYMM);
				}
			}
			IReport iReport = new IReport();
			iReport.setStartDate(startDate);
			iReport.setEndDate(endDate);

			String groupBy = MonthlyReportFilterView.getDefault().getCurrentGroupBy();
			if (groupBy.equals("ACCOUNT")) {
				iReport.setAccount(AbstractReferenceData.resolveAccountFromId(id));
			} else if (groupBy.equals("METHOD")) {
				iReport.setMethod(AbstractReferenceData.resolveMethodFromId(id));
			} else if (groupBy.equals("CATEGORY")) {
				iReport.setCategory(AbstractReferenceData.resolveCategoryFromId(id));
			}
			iReport.open();
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

			ArrayList<Label> row = rowArray.get(((int[])((Label)e.widget).getData())[0]);
			if (currentSelectedRow != row) {
				if (currentSelectedRow != null) {
					for (Label l : currentSelectedRow) {
						if (l.getText().startsWith("-")) {
							l.setForeground(Colors.RED);
						} else {
							l.setForeground(Colors.BLACK);	
						}
					}
				}
				currentSelectedRow = row;
				if (currentSelectedRow != null) {
					for (Label l : currentSelectedRow) {
						l.setForeground(Colors.CCBG);
					}
				}
			}
		}
		public void mouseUp(MouseEvent e) {}
    }
	
}

