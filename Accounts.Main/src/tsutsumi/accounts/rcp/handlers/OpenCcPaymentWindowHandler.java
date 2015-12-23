package tsutsumi.accounts.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import tsutsumi.accounts.rcp.dialogs.CreditCardDialog;

public class OpenCcPaymentWindowHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		CreditCardDialog and = CreditCardDialog.getDefault(Display.getCurrent().getActiveShell());
		and.open();
		return null;
	}

}
