package tsutsumi.accounts.server.commands;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.common.response.TransactionsResponse;
import tsutsumi.accounts.server.db.GetSummaryDbHandler;

public class GetTransactionsCommand extends Command {
	public Response process() throws AccountsException {
		int typeId =getParam("TYPE_ID", 0);
		int methodId = getParam("METHOD_ID",0);
		int accountId = getParam("ACCOUNT_ID",0);
		int categoryId = getParam("CATEGORY_ID",0);
		String fromDate = getParam("FROM_DATE","1900/01/01");
		String toDate = getParam("TO_DATE","3999/01/01");
		boolean showCredits = getParam("SHOWCREDITS", true);
		boolean showDebits = getParam("SHOWDEBITS", true);
		boolean creditCards = getParam("CREDITCARD", false);
		
		GetSummaryDbHandler sh = new GetSummaryDbHandler();
		TransactionsResponse response = new TransactionsResponse();
		try {
			response.addTransactions(sh.getTransactions(typeId, methodId, accountId, categoryId, fromDate, toDate, showCredits, showDebits, creditCards));
			response.setStatus(Response.SUCCESS);
			response.setMessage("Successfully grabbed summary");
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
