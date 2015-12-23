package tsutsumi.accounts.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import tsutsumi.accounts.rcp.dialogs.itransactions.ITransactions;

public class OpenTransactionsWindowHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ITransactions rep = new ITransactions();
		rep.open();
		return null;
	}

}
