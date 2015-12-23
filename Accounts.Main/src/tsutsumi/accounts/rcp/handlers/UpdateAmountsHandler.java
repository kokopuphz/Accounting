package tsutsumi.accounts.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import tsutsumi.accounts.rcp.views.ChangeAffectable;

public class UpdateAmountsHandler extends AbstractHandler {
	
	
	
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
				for (IViewReference viewReference : views) {
					IViewPart view = viewReference.getView(true);
					if (view instanceof ChangeAffectable) {
						int methodId  = Integer.valueOf(event.getParameter("Accounts.Main.UpdateAmounts.MethodId"));
						int accountId  = Integer.valueOf(event.getParameter("Accounts.Main.UpdateAmounts.AccountId"));
						int categoryId  = Integer.valueOf(event.getParameter("Accounts.Main.UpdateAmounts.CategoryId"));
						((ChangeAffectable)view).updateAmount(methodId, accountId, categoryId);
					}
				}
			}
		}
		return null;
	}
}
