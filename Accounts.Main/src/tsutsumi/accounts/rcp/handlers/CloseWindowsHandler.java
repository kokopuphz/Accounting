package tsutsumi.accounts.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import tsutsumi.accounts.rcp.advisors.ApplicationWorkbenchWindowAdvisor;

public class CloseWindowsHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell[] shells = Display.getDefault().getShells();
		for (Shell shell : shells) {
			if (shell != ApplicationWorkbenchWindowAdvisor.mainShell && !shell.isDisposed()) {
				shell.close();
			}
		}
		return null;
	}

}
