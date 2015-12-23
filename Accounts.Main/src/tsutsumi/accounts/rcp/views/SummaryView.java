package tsutsumi.accounts.rcp.views;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.rcp.Colors;
import tsutsumi.accounts.rcp.tables.AbstractTable;
import tsutsumi.accounts.rcp.tables.ExpenditureTable;
import tsutsumi.accounts.rcp.tables.SummaryTable;

public class SummaryView extends AbstractViewPart implements ChangeAffectable {
	public final static String ID = "Accounts.Main.SummaryView";
	private SummaryTable sumTable;
	private ExpenditureTable categorySummary;
	private ExpenditureTable accountSummary;
	private static SummaryView defaultView;
	private UpdateAllJob upd;
	private Thread uiThread;
	
	private Label currentHightlightedLabel;
	private Color previousColor;

	
	public class SubListener implements MouseListener {
		private ArrayList<AbstractTable> tables = new ArrayList<AbstractTable>();
		public void registerTable(AbstractTable t) {
			tables.add(t);
		}
		public void mouseDoubleClick(MouseEvent e) {
			for (AbstractTable table : tables) {
				if (table.hasLabel((Label)e.widget)) {
					table.processDoubleClick((Label)e.widget);
					break;
				}
			}
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
		public void mouseUp(MouseEvent e) {}
    }
	public SubListener mListener = new SubListener();
	
	public void createPartControl(Composite parent) {
		upd = new UpdateAllJob();
		uiThread = Thread.currentThread();
		defaultView = this;
		final ScrolledComposite sc1 = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		sc1.setExpandHorizontal(true);
		sc1.setExpandVertical(true);
		sc1.getVerticalBar().setIncrement(sc1.getVerticalBar().getIncrement()*3);
		sc1.addListener(SWT.Activate, new Listener() {
		    public void handleEvent(Event e) {
		        sc1.setFocus();
		    }
		}); 
		
		Composite container = new Composite(sc1, SWT.NONE);
		
		GridLayout mainLayout = new GridLayout();
		mainLayout.horizontalSpacing = 0;
		mainLayout.verticalSpacing = 3;
		mainLayout.marginHeight = 3;
		mainLayout.marginWidth = 3;
		mainLayout.numColumns = 1;
		container.setLayout(mainLayout);
		GridData overallData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		container.setLayoutData(overallData);

		
		GridData containerData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		Label balancesLabel = new Label(container, SWT.LEFT);
		balancesLabel.setText("Balances:");
		
		sumTable = new SummaryTable(container);

//		Label dummyLabel = new Label(container, SWT.NONE);
//		dummyLabel.setText("");
		
		Composite expendContainer = new Composite(container, SWT.NONE);
		GridLayout expendLayout = new GridLayout();
		expendLayout.horizontalSpacing = 5;
		expendLayout.verticalSpacing = 3;
		expendLayout.marginHeight = 0;
		expendLayout.marginWidth = 0;
		expendLayout.numColumns = 3;
		expendLayout.makeColumnsEqualWidth=true;
		expendContainer.setLayout(expendLayout);
		expendContainer.setLayoutData(containerData);
		
		Label expLabel = new Label(expendContainer, SWT.LEFT);
		expLabel.setText("Expenditures By Category:");

		Label exp2Label = new Label(expendContainer, SWT.LEFT);
		exp2Label.setText("Expenditures By Account:");

		Label exp3Label = new Label(expendContainer, SWT.LEFT);
		exp3Label.setText("");

		categorySummary = new ExpenditureTable(expendContainer, ExpenditureTable.BY_CATEGORY);
		accountSummary = new ExpenditureTable(expendContainer, ExpenditureTable.BY_ACCOUNT);		

//		Composite comp3 = new Composite(expendContainer, SWT.BORDER);
//		comp3.setLayoutData(containerData);
//
		sc1.setContent(container);
		sc1.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	public void dispose() {
		defaultView = null;
	}
	
	public static SummaryView getDefault() {
		return defaultView;
	}
	
	
	public void updateAmount(final int methodId, final int accountId, final int categoryId) {
		final Method m = AbstractReferenceData.resolveMethodFromId(methodId);
		final Account a = AbstractReferenceData.resolveAccountFromId(accountId);
		final Category c = AbstractReferenceData.resolveCategoryFromId(categoryId);
		if (Thread.currentThread()==uiThread) {
			sumTable.updateAmount(m, a);
			categorySummary.update(c);
			accountSummary.update(a);
		} else {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					if (!Display.getDefault().getActiveShell().isDisposed()) {
						sumTable.updateAmount(m, a);
						categorySummary.update(c);
						accountSummary.update(a);
					}
				}
			});
		}
	}
	
	public void updateAllAmount() {
		upd.schedule();
	}
	
	private class UpdateAllJob extends Job {
		public UpdateAllJob() {
			super("Applying filter to summary...");
		}
		protected IStatus run(IProgressMonitor monitor) {
			if (Thread.currentThread()==uiThread) {
				sumTable.updateAllAmount();
				categorySummary.updateAll();
				accountSummary.updateAll();
			} else {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						if (!Display.getDefault().getActiveShell().isDisposed()) {
							sumTable.updateAllAmount();
							categorySummary.updateAll();
							accountSummary.updateAll();
						}
					}
				});
			}
			return Status.OK_STATUS;
		}
		
	}

	
}
