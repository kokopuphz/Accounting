package tsutsumi.accounts.rcp.dialogs.itransactions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import tsutsumi.accounts.rcp.dialogs.AddNewDialog;

public class IEditor {
	private Shell thisShell;
	private Composite summaryPart;
	
	public IEditor() {
    	thisShell = new Shell(Display.getDefault(), SWT.BORDER| SWT.TITLE|SWT.CLOSE|SWT.MODELESS|SWT.RESIZE);
    	
    	GridLayout shellLayout = new GridLayout();
    	shellLayout.marginHeight = 2;
    	shellLayout.marginTop = 3;
    	shellLayout.marginWidth = 2;
    	shellLayout.horizontalSpacing = 0;
    	shellLayout.verticalSpacing = 3;

    	thisShell.setLayout(shellLayout);
    	thisShell.setText("Transaction Details");
    	thisShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
//				openReports.remove(ITransactions.this);
				close();
			}
    	});
		Point currentLoc = thisShell.getLocation();
		thisShell.setBounds(currentLoc.x, currentLoc.y, 660, 600);
		thisShell.setMinimumSize(662, 350);
    	
		createPartControl(thisShell);
	}
	
	public Shell getShell() {
		return thisShell;
	}
	
	private void createPartControl(Shell parent){
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		
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

		// Custom Action for the View's Menu   
//		ToolBar toolbar = parent.getToolBar();


		ToolItem itemPush = new ToolItem(toolBar, SWT.PUSH);
	    itemPush.setText("PUSH item");
//	    itemPush.set
	    Image icon = new Image(parent.getDisplay(), IEditor.class.getResourceAsStream("cashicon.gif"));
	    itemPush.setImage(icon);

//		CustomAction lCustomAction = new CustomAction();   
//		lCustomAction.setText("Open Dialog Box");   
//		lCustomAction.setImageDescriptor(Activator.getImageDescriptor("icons/bomb.png"));   
//		getViewSite().getActionBars().getToolBarManager().add(lCustomAction);  
	}
	
	private void populate() {
//		XmlInterface xmlRequest = new XmlInterface();
//    	xmlRequest.setCommand(CommandEnum.GET_TRANSACTIONS);
//    	xmlRequest.setCommandParams("TYPE_ID",String.valueOf(t));
//    	xmlRequest.setCommandParams("METHOD_ID",String.valueOf(m));
//    	xmlRequest.setCommandParams("ACCOUNT_ID",String.valueOf(a));
//    	xmlRequest.setCommandParams("CATEGORY_ID",String.valueOf(c));
//    	xmlRequest.setCommandParams("TO_DATE",endDate);
//    	xmlRequest.setCommandParams("FROM_DATE",startDate);
//    	xmlRequest.setCommandParams("SHOWCREDITS",String.valueOf(creditShown));
//    	xmlRequest.setCommandParams("SHOWDEBITS",String.valueOf(debitShown));
//    	
//    	AccountsClient client = AccountsClient.getDefault();
//    	XmlInterface response = client.post(xmlRequest);
//    	Response rr = response.getResponse();
//    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
//    		TransactionsResponse transResponse = (TransactionsResponse)rr;
//    		ArrayList<TransactionRecord> transRecs = transResponse.getTransactions();
//    		tableView.setInput(transRecs);
//    	}
	}
	
	public void refresh() {
		populate();
	}
	
	public void open() {
		populate();
		thisShell.open();
	}
	
	public void close() {
//		iReport.removeChild(this);
		thisShell.dispose();
	}
	
}
