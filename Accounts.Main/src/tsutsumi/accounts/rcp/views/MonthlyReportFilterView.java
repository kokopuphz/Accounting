package tsutsumi.accounts.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MonthlyReportFilterView extends AbstractViewPart {
	public final static String ID = "Accounts.Main.MonthlyReportFilterView";
	private static MonthlyReportFilterView defaultView;
	private String currentGroupBy = "ACCOUNT";
	private int currentReportType = 1;
	private String currentShowBy = "BALANCE";
	private String groupBy = "ACCOUNT";
	private int reportType = 1;
	private String showBy = "BALANCE";

	public MonthlyReportFilterView() {
		super();
		this.minHeight = 53;
		this.maxHeight = 53;
	}
	public void createPartControl(Composite parent) {
		SelectListen listener = new SelectListen();
		SelectListenShowBy listener3 = new SelectListenShowBy();
		defaultView = this;
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.verticalSpacing = 6;
		gridLayout.horizontalSpacing = 6;
		gridLayout.marginWidth = 6;
		parent.setLayout(gridLayout);
		parent.setLayoutData(gridData);
		
		Composite labelComposite = new Composite(parent, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 6;
		labelComposite.setLayout(gridLayout);
		labelComposite.setLayoutData(gridData);
		
		gridData = new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1);
		Label label = new Label(labelComposite, SWT.RIGHT);
		label.setText("Show: ");
		label.setLayoutData(gridData);

		gridData = new GridData(SWT.FILL, SWT.CENTER, false, true, 1, 1);
		label = new Label(labelComposite, SWT.RIGHT);
		label.setText("Group by: ");
		label.setLayoutData(gridData);

		Composite radioComposite = new Composite(parent, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 6;
		gridLayout.numColumns = 1;
		radioComposite.setLayout(gridLayout);
		radioComposite.setLayoutData(gridData);

		Composite radioButtonComposite1 = new Composite(radioComposite, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.numColumns = 3;
		radioButtonComposite1.setLayout(gridLayout);
		radioButtonComposite1.setLayoutData(gridData);
		
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
	    Button button = new Button(radioButtonComposite1, SWT.RADIO);
	    button.setText("Balance");
	    button.addSelectionListener(listener3);
	    button.setData("BALANCE");
	    button.setSelection(true);
	    button.setLayoutData(gridData);

		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
	    button = new Button(radioButtonComposite1, SWT.RADIO);
	    button.setText("Credits");
	    button.addSelectionListener(listener3);
	    button.setData("CREDIT");
//	    button.setSelection(true);
	    button.setLayoutData(gridData);
	    
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
	    button = new Button(radioButtonComposite1, SWT.RADIO);
	    button.setText("Debits");
	    button.addSelectionListener(listener3);
	    button.setData("DEBIT");
//	    button.setSelection(true);
	    button.setLayoutData(gridData);
		
		Composite radioButtonComposite2 = new Composite(radioComposite, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 6;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 4;
		radioButtonComposite2.setLayout(gridLayout);
		radioButtonComposite2.setLayoutData(gridData);

		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
	    button = new Button(radioButtonComposite2, SWT.RADIO);
	    button.setText("Accounts");
	    button.addSelectionListener(listener);
	    button.setData("ACCOUNT");
	    button.setSelection(true);
	    button.setLayoutData(gridData);
	    
	    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
	    button = new Button(radioButtonComposite2, SWT.RADIO);
	    button.setText("Categories");
	    button.addSelectionListener(listener);
	    button.setData("CATEGORY");
	    button.setLayoutData(gridData);
	    
	    gridData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
	    button = new Button(radioButtonComposite2, SWT.RADIO);
	    button.setText("Payment Method");
	    button.addSelectionListener(listener);
	    button.setData("METHOD");
	    button.setLayoutData(gridData);
	    
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 6;
		buttonComposite.setLayout(gridLayout);
		buttonComposite.setLayoutData(gridData);
		
		gridData = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		button = new Button(buttonComposite, SWT.PUSH);
		button.setText("Generate Report");
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				currentGroupBy = groupBy;
				currentReportType = reportType;
				currentShowBy = showBy;
				MonthlyReportView.getDefault().updateAllAmount();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				currentGroupBy = groupBy;
				currentReportType = reportType;
				currentShowBy = showBy;
				MonthlyReportView.getDefault().updateAllAmount();
			}
		});
	}
	
	public void dispose() {
		defaultView = null;
	}
	
	public static MonthlyReportFilterView getDefault() {
		return defaultView;
	}
	
	private class SelectListen implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			if (((Button)event.widget).getSelection()) {
				Button button = (Button)event.widget;
				String data = (String)button.getData();
				groupBy = data;
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {}
	}

	private class SelectListenShowBy implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			if (((Button)event.widget).getSelection()) {
				Button button = (Button)event.widget;
				String data = (String)button.getData();
				showBy = data;
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {}
	}

	
	public String getCurrentGroupBy() {
		return currentGroupBy;
	}
	
	public int getCurrentReportTypeId() {
		return currentReportType;
	}
	
	public String getCurrentShowBy() {
		return currentShowBy;
	}
	
}
