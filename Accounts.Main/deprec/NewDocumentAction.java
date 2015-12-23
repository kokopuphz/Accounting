package aiu.jp.nps.admi.tools.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import aiu.jp.nps.admi.tools.rcp.Activator;
import aiu.jp.nps.admi.tools.rcp.views.DocumentFolderView;

public class NewDocumentAction extends Action implements IViewActionDelegate  {
	
	private String ID = "NpsPatchProgram.new";
	private final IWorkbenchWindow window;
	private int instanceNum = 0;
	private final String viewId;
	private IViewPart view;
	
	public NewDocumentAction(IWorkbenchWindow window, String label, String viewId) {
		this.window = window;
		this.viewId = viewId;
        setText(label);
        // The id is used to refer to the action in a menu or toolbar
		setId(ICommandIds.CMD_NEW);
        // Associate the action with a pre-defined command, to allow key bindings.
		setActionDefinitionId(ICommandIds.CMD_NEW);
		setImageDescriptor(Activator.getImageDescriptor("/icons/sample2.gif"));
	}
	
	public void run() {
		if(window != null) {
			try {
				window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}

	public void run(IAction action) {
		if(window != null) {
			try {
				((DocumentFolderView)view).openDocument(null);
				//window.getActivePage().showView(viewId, Integer.toString(instanceNum++), IWorkbenchPage.VIEW_ACTIVATE);
			} catch (Exception e) {
				MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	public void init(IViewPart view) {
		// TODO Auto-generated method stub
		this.view = view;
	}
}
