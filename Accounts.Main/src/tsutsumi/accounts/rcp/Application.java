package tsutsumi.accounts.rcp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.response.ReferenceDataResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.rcp.advisors.ApplicationWorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {
	private Display display;
	private static Application current;

	public Object start(IApplicationContext context) {
//		try {
//			new Thread(            
//					new Runnable() {                
//						public void run() {                    
//							try {                        
//								new Controller();                    
//							} catch (Exception e) {                        
//								e.printStackTrace();                    
//							}                    
//							                
//						}            
//					}
//			).start();
//		} catch (Exception e) {
//			//simply tried
//		}
		
    	XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.REFERENCEDATA);
    	xmlRequest.setCommandParams("DATATYPE", "ALL");
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response r = response.getResponse();
//    	System.out.println(r.getStatus());
    	if (r.getStatus()==Response.SUCCESS && r instanceof ReferenceDataResponse){ 
	    	ReferenceDataResponse tr = (ReferenceDataResponse)r;
	    	AbstractReferenceData.setMethods(tr.getMethodArray());
	    	AbstractReferenceData.setCategories(tr.getCategoryArray());
	    	AbstractReferenceData.setAccounts(tr.getAccountArray());
	    	AbstractReferenceData.setDescriptionProposalText(tr.getDescriptionProposalText());
	    	AbstractReferenceData.setTypes(tr.getTypeArray());
    	}
    	
    	display = PlatformUI.createDisplay();
		current = this;
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}
	
	public static Application getDefault() {
		return current;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
}
