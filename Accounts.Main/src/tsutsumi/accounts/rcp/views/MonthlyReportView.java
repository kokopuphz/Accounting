package tsutsumi.accounts.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import tsutsumi.accounts.rcp.tables.MonthlySummaryTable;

public class MonthlyReportView extends AbstractViewPart implements ChangeAffectable {
	public final static String ID = "Accounts.Main.MonthlyReportView";
	private MonthlySummaryTable sumTable;
	private static MonthlyReportView defaultView;
	private ScrolledComposite sc1;
	private Composite container;

	public void createPartControl(Composite parent) {
		defaultView = this;
//		Color bgColor = new Color (Display.getCurrent() , 230, 255, 255);
		
		sc1 = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		sc1.setExpandHorizontal(true);
		sc1.setExpandVertical(true);
		sc1.getVerticalBar().setIncrement(sc1.getVerticalBar().getIncrement()*3);
		sc1.addListener(SWT.Activate, new Listener() {
		    public void handleEvent(Event e) {
		        sc1.setFocus();
		    }
		}); 


		
		container = new Composite(sc1, SWT.NONE);
//		container.setBackground(bgColor);
		GridLayout mainLayout = new GridLayout();
		mainLayout.horizontalSpacing = 0;
		mainLayout.verticalSpacing = 3;
		mainLayout.marginHeight = 3;
		mainLayout.marginWidth = 3;
		mainLayout.numColumns = 1;
		container.setLayout(mainLayout);
		GridData containerData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		container.setLayoutData(containerData);
		
		sumTable = new MonthlySummaryTable(container);
		sumTable.updateAllAmount();
		
		sc1.setContent(container);
		sc1.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}
	
	public void updateAmount(final int m, final int a, final int c) {
		updateAllAmount();
	}
	
	
	public void dispose() {
		defaultView = null;
	}
	
	public static MonthlyReportView getDefault() {
		return defaultView;
	}
	
	
	public void updateAllAmount() {
		sumTable.updateAllAmount();
		container.layout(true);
		sc1.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
}
