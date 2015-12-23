package tsutsumi.accounts.server.commands;

import java.math.BigDecimal;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.DefaultResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.server.db.AddTransactionDbHandler;

public class AddTransactionCommand extends Command {
	public Response process() throws AccountsException {
		String date = getParam("DATE", "");
		int fromAccountId = getParam("FROM_ACCOUNT_ID", 0);
		int toAccountId = getParam("TO_ACCOUNT_ID", 0);
		int categoryId = getParam("CATEGORY_ID", 0);
		int fromMethodId = getParam("FROM_METHOD_ID", 0);
		int toMethodId = getParam("TO_METHOD_ID", 0);
		BigDecimal amount = getParam("AMOUNT", new BigDecimal(0));
		String description = getParam("DESCRIPTION", "");
		int[] ccTransactionIds = getParam("CC_TRANSACTION_ID_ARRAY", new int[0]);
		
		AddTransactionDbHandler sh = new AddTransactionDbHandler();
		DefaultResponse response = new DefaultResponse();
		try {
			int returnCode = sh.addTransaction(date, categoryId, description, fromAccountId, toAccountId, fromMethodId, toMethodId, amount, ccTransactionIds);
			if (returnCode == 1) {
				response.setStatus(Response.SUCCESS);
				response.setMessage("Successfully added transaction.");
			} else {
				response.setStatus(Response.ABORT);
				response.setMessage("Error adding transaction");
			}
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
