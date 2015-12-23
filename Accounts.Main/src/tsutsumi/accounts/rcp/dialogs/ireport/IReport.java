package tsutsumi.accounts.rcp.dialogs.ireport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.reference.Type;

public class IReport {
//	private static ArrayList<IReport> openReports = new ArrayList<IReport>();
	private Shell thisShell;
	private FilterPart fp;
	private SummaryPart sp;
//	private boolean isPinned = false;
//	private ITransactions childs;
	private String startDate;
	private String endDate;
	private Method method;
	private Category category;
	private Type type;
	private Account account;
	
	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Method getMethod() {
		return method;
	}

	public Category getCategory() {
		return category;
	}

	public Type getType() {
		return type;
	}

	public Account getAccount() {
		return account;
	}

//	public static void focusLostEvent() {
////		IReport[] reports = openReports.toArray(new IReport[openReports.size()]);
//		for (IReport rep : reports) {
////			if (!rep.isPinned && !rep.hasOpenChild()){
//				rep.close();
////			}
//		}
//	}
	
//	public boolean hasOpenChild() {
//		return childs != null;
//	}
	
//	public void addChild(ITransactions tp) {
//		childs = tp;
//	}
	
	
//	public void removeChild(ITransactions tp) {
//		childs = null;
//	}
	
//	public void closeChild() {
//		if (childs != null)
//			childs.close();
//	}
	
	public IReport() {
		startDate = "1900/01/01";
		endDate = Utils.getToday();
    	thisShell = new Shell(Display.getDefault(), SWT.BORDER| SWT.TITLE|SWT.CLOSE|SWT.MODELESS|SWT.RESIZE);
    	thisShell.setMinimumSize(545, 350);
//    	childs = new ArrayList<TransactionPart>();
    	GridLayout shellLayout = new GridLayout();
    	shellLayout.marginHeight = 3;
    	shellLayout.marginWidth = 3;
    	shellLayout.horizontalSpacing = 3;
    	shellLayout.verticalSpacing = 3;
    	thisShell.setLayout(shellLayout);
    	thisShell.setText("Details");
//    	thisShell.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(DisposeEvent e) {
//				openReports.remove(IReport.this);
//			}
//    	});
    	thisShell.addShellListener(new ShellListener() {
			public void shellActivated(ShellEvent e) {
//				Point p = thisShell.getSize();
//				System.out.println(p.x);
//				System.out.println(p.y);
			}
			public void shellClosed(ShellEvent e) {}
			public void shellDeactivated(ShellEvent e) {
//				IReport.focusLostEvent();
//				if (!isPinned && !hasOpenChild()){
//					close();
//				}
			}
			public void shellDeiconified(ShellEvent e) {}
			public void shellIconified(ShellEvent e) {}
        });

    	createPartControl(thisShell);
//    	openReports.add(this);
	}

	public Shell getShell() {
		return thisShell;
	}
	
//	public FilterPart getFilterPart() {
//		return fp;
//	}
	
//	public SummaryPart getSummaryPart() {
//		return sp;
//	}
	
	public void setStartDate(String startDate) {
		fp.setStartDate(startDate);
		this.startDate = startDate;
	}
	
	public void setEndDate(String startDate) {
		fp.setEndDate(startDate);
		this.endDate = startDate;
	}
	
	public void setMethod(Method m) {
		fp.setMethod(m);
		this.method = m;
	}
	
	public void setAccount(Account a) {
		fp.setAccount(a);
		this.account = a;
	}

	public void setCategory(Category c) {
		fp.setCategory(c);
		this.category = c;
	}
	
	public void setType(Type t) {
		fp.setType(t);
		this.type = t;
	}
	
	public void createPartControl(Shell parent) {
		fp = new FilterPart(this);	
		sp = new SummaryPart(this);
	}
	
	public void refresh() {
		sp.populate();
	}
	
	public void open() {
		sp.populate();
		thisShell.pack();
		Point p = thisShell.getSize();
		Point loc = thisShell.getLocation();
//		Point mouseLoc = Display.getCurrent().getCursorLocation();
		if (p.y > 600)
			p.y = 600;
		thisShell.setBounds(loc.x, 0, p.x, p.y);
//		thisShell.setMinimumSize(p.x, 200);
		thisShell.open();
	}
	
	public void close() {
//		openReports.remove(this);
		thisShell.dispose();
	}


}
