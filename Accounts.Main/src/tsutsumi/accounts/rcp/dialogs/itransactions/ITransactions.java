package tsutsumi.accounts.rcp.dialogs.itransactions;

import java.util.ArrayList;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.TransactionRecord;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.common.response.TransactionsResponse;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.dialogs.ireport.FilterPart;

public class ITransactions {
//	private static ArrayList<ITransactions> openReports = new ArrayList<ITransactions>();
//	private IReport iReport;
	private FilterPart fp;
	private String startDate;
	private String endDate;
	private int m;
	private int a;
	private int c;
	private int t;
	private Shell thisShell;
	private boolean creditShown=true;
	private boolean debitShown=true;
	private Table databaseTable;
	private Composite summaryPart;
	private TableViewer tableView;
	
	public ITransactions() {
    	thisShell = new Shell(Display.getDefault(), SWT.BORDER| SWT.TITLE|SWT.CLOSE|SWT.MODELESS|SWT.RESIZE);
    	startDate = "1900/01/01";
    	endDate = Utils.getToday();
    	
    	GridLayout shellLayout = new GridLayout();
    	shellLayout.marginHeight = 2;
    	shellLayout.marginTop = 3;
    	shellLayout.marginWidth = 2;
    	shellLayout.horizontalSpacing = 0;
    	shellLayout.verticalSpacing = 3;

    	thisShell.setLayout(shellLayout);
    	thisShell.setText("Transactions");
    	thisShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
//				openReports.remove(ITransactions.this);
				close();
			}
    	});
		Point currentLoc = thisShell.getLocation();
		thisShell.setBounds(currentLoc.x, currentLoc.y, 802, 600);
		thisShell.setMinimumSize(662, 350);
    	
		createPartControl(thisShell);
	}
	
	public Shell getShell() {
		return thisShell;
	}
	
	private void createPartControl(Shell parent){
		fp = new FilterPart(this);
		
    	GridLayout layoutData = new GridLayout();
    	layoutData.marginHeight = 0;
    	layoutData.marginTop = 0;
    	layoutData.marginWidth = 0;
    	layoutData.horizontalSpacing = 0;
    	layoutData.verticalSpacing = 3;
    	summaryPart = new Composite(thisShell, SWT.NONE);
    	summaryPart.setLayout(layoutData);
    	summaryPart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite container = new Composite(summaryPart, SWT.NONE);
    	GridLayout shellLayout = new GridLayout();
    	shellLayout.marginHeight = 2;
    	shellLayout.marginWidth = 50;
    	shellLayout.horizontalSpacing = 5;
    	shellLayout.verticalSpacing = 6;
    	shellLayout.numColumns = 2;
    	container.setLayout(shellLayout);
    	container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

		
		Label searchLabel = new Label(container, SWT.NONE);
		searchLabel.setText("Search Within Results: ");
		final Text searchText = new Text(container, SWT.BORDER | SWT.SEARCH);
		searchText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		tableView = new TableViewer(summaryPart, SWT.BORDER | SWT.FULL_SELECTION);
		tableView.setUseHashlookup(true);
		tableView.setLabelProvider(new NpsRecordTableLabelProvider());
		tableView.setContentProvider(new StructuredContentProvider());
		
		GridData borderData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tableView.getTable().setLayoutData(borderData);
		
        databaseTable = tableView.getTable();
        databaseTable.setHeaderVisible(true);
        databaseTable.setLinesVisible(true);
        
		final PersonFilter filter = new PersonFilter();
		tableView.addFilter(filter);
		searchText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				filter.setSearchText(searchText.getText());
				tableView.refresh();
			}
		});
		
		//add double click 2014/01/15
		tableView.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				TransactionRecord record = (TransactionRecord) sel.getFirstElement();
				if(record != null){
					System.out.println("record clicked" + record.getTransactionId());
					IEditor tp = new IEditor();
					tp.open();

				}
			}
		});

        
        final TableColumn column1 = new TableColumn(databaseTable, SWT.LEFT);
        column1.setWidth(90);
        final TableColumn column6 = new TableColumn(databaseTable, SWT.LEFT);
        column6.setWidth(75);
        final TableColumn column7 = new TableColumn(databaseTable, SWT.LEFT);
        column7.setWidth(100);
        final TableColumn column8 = new TableColumn(databaseTable, SWT.LEFT);
        column8.setWidth(100);
        final TableColumn column2 = new TableColumn(databaseTable, SWT.LEFT);
        column2.setWidth(200);
        final TableColumn column3 = new TableColumn(databaseTable, SWT.RIGHT);
        column3.setWidth(95);
        final TableColumn column4 = new TableColumn(databaseTable, SWT.RIGHT);
        column4.setWidth(95);
//        final TableColumn column5 = new TableColumn(databaseTable, SWT.RIGHT);
//        column5.setWidth(75);

        column1.setText("Date");
        column6.setText("Account");
        column7.setText("Category");
        column8.setText("Method");
        column2.setText("Description");
        column3.setText("Credit");
        column4.setText("Debit");
        //column5.setText("Balance");
        
        column1.setMoveable(true);
        column2.setMoveable(true);
		column3.setMoveable(true);
		column4.setMoveable(true);
		column6.setMoveable(true);
		column7.setMoveable(true);
		column8.setMoveable(true);

		//nothing to add here		
	}
	
	private void populate() {
		XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.GET_TRANSACTIONS);
    	xmlRequest.setCommandParams("TYPE_ID",String.valueOf(t));
    	xmlRequest.setCommandParams("METHOD_ID",String.valueOf(m));
    	xmlRequest.setCommandParams("ACCOUNT_ID",String.valueOf(a));
    	xmlRequest.setCommandParams("CATEGORY_ID",String.valueOf(c));
    	xmlRequest.setCommandParams("TO_DATE",endDate);
    	xmlRequest.setCommandParams("FROM_DATE",startDate);
    	xmlRequest.setCommandParams("SHOWCREDITS",String.valueOf(creditShown));
    	xmlRequest.setCommandParams("SHOWDEBITS",String.valueOf(debitShown));
    	
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
    		TransactionsResponse transResponse = (TransactionsResponse)rr;
    		ArrayList<TransactionRecord> transRecs = transResponse.getTransactions();
    		tableView.setInput(transRecs);
    	}
	}
	
	public void refresh() {
		populate();
	}
	
	public void setStartDate(String startDate) {
		fp.setStartDate(startDate);
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate) {
		fp.setEndDate(endDate);
		this.endDate = endDate;
	}
	
	public void setMethod(int m) {
		fp.setMethod(AbstractReferenceData.resolveMethodFromId(m));
		this.m = m;
	}
	
	public void setAccount(int a) {
		fp.setAccount(AbstractReferenceData.resolveAccountFromId(a));
		this.a = a;
	}

	public void setCategory(int c) {
		fp.setCategory(AbstractReferenceData.resolveCategoryFromId(c));
		this.c = c;
	}
	
	public void setType(int t) {
		fp.setType(AbstractReferenceData.resolveTypeFromId(t));
		this.t = t;
	}

	public void setCreditShown(boolean b) {
		fp.setShowCredit(b);
		creditShown = b;
	}
	
	public void setDebitShown(boolean b) {
		fp.setShowDebit(b);
		debitShown = b;
	}
	
	public void open() {
		populate();
		thisShell.open();
	}
	
	public void close() {
//		iReport.removeChild(this);
		thisShell.dispose();
	}
	
	private class NpsRecordTableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider  {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			TransactionRecord npsRecord = (TransactionRecord)element;
			switch(columnIndex) {
				case 0:
					return npsRecord.getTransactionDate();
				case 1:
					return AbstractReferenceData.resolveAccountFromId(npsRecord.getAccountId()).getName();
				case 2:
					return AbstractReferenceData.resolveCategoryFromId(npsRecord.getCategoryId()).getName();
				case 3:
					return AbstractReferenceData.resolveMethodFromId(npsRecord.getMethodId()).getName();
				case 4:
					return npsRecord.getDescription();
				case 5:
					return Utils.formatNumber(npsRecord.getCredit());
				case 6:
					return Utils.formatNumber(npsRecord.getDebit());
				default:
					return null;
			}
		}

		public Color getForeground(Object element, int columnIndex) {
			return null;
		}

		public Color getBackground(Object element, int columnIndex) {
			TransactionRecord npsRecord = (TransactionRecord)element;
			if ( (npsRecord.getRownumber() % 2) == 0) {
			    return Colors.PINK;
			}else{
				return Colors.WHITE;
			}
		}
	}
	
	private class StructuredContentProvider implements IStructuredContentProvider {
		public void dispose() {}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		public Object[] getElements(Object inputElement) {
			return ((ArrayList<?>)inputElement).toArray();				
		}
	}

	public class PersonFilter extends ViewerFilter {

		private String searchString;

		public void setSearchText(String s) {
			this.searchString = ".*" + s.toLowerCase() + ".*";
		}

		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if (searchString == null || searchString.length() == 0) {
				return true;
			}
			TransactionRecord p = (TransactionRecord) element;
			if (p.getDescription().toLowerCase().matches(searchString)) {
				return true;
			}
			return false;
		}
	}


}
