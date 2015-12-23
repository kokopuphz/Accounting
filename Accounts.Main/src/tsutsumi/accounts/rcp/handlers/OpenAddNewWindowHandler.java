package tsutsumi.accounts.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import tsutsumi.accounts.rcp.dialogs.AddNewDialog;

public class OpenAddNewWindowHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AddNewDialog and = AddNewDialog.getDefault(Display.getCurrent().getActiveShell());
		and.open();
		return null;
	}

}
