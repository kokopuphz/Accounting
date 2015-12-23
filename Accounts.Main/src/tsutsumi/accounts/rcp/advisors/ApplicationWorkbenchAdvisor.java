package tsutsumi.accounts.rcp.advisors;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import tsutsumi.accounts.rcp.perspectives.SummaryPerspective;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
//	private static final String JUSTUPDATED = "justUpdated";
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return SummaryPerspective.ID;
	} 

    public void initialize(IWorkbenchConfigurer configurer) {
        configurer.setSaveAndRestore(true);
        super.initialize(configurer);
    }
    
    public void postStartup() {
//        PreferenceManager pm = PlatformUI.getWorkbench().getPreferenceManager( );
//        pm.remove("org.eclipse.equinox.security.ui.category");
//        pm.remove("org.eclipse.ui.preferencePages.Workbench");
    }
    
}
