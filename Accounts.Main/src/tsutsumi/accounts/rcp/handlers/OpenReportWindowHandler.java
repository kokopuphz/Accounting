package tsutsumi.accounts.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import tsutsumi.accounts.rcp.dialogs.ireport.IReport;

public class OpenReportWindowHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IReport rep = new IReport();
		rep.open();
		return null;
	}

}
