package tsutsumi.accounts.rcp.advisors;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	public static Shell mainShell;
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        //configurer.setInitialSize(new Point(800, 500));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowProgressIndicator(true);
        configurer.setShowPerspectiveBar(true);
    }

    public void postWindowOpen() {
    	mainShell = getWindowConfigurer().getWindow().getShell();
//    	mainShell.addShellListener(new ShellListener() {
//			public void shellActivated(ShellEvent e) {
////				TransactionPart.focusLostEvent();
////				IReport.focusLostEvent();
//				e.doit=true;
//			}
//			public void shellClosed(ShellEvent e) {}
//			public void shellDeactivated(ShellEvent e) {}
//			public void shellDeiconified(ShellEvent e) {}
//			public void shellIconified(ShellEvent e) {}
//        });
        //shell.setMinimumSize(520, 350);
    }
   
}
