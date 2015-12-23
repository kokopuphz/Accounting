package tsutsumi.accounts.server.commands;

import java.math.BigDecimal;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.BigDecimalResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.server.db.GetSummaryDbHandler;

public class GetIndividualSummaryCommand extends Command {

	public Response process() throws AccountsException {
		int accountId = getParam("ACCOUNT_ID", 0);
		int methodId = getParam("METHOD_ID", 0);
		String toDate = getParam("TO_DATE","3999/01/01");
		String fromDate = getParam("FROM_DATE","1900/01/01");
		
		GetSummaryDbHandler sh = new GetSummaryDbHandler();
		BigDecimalResponse response = new BigDecimalResponse();
		
		try {
			BigDecimal amount = sh.getIndividualSummary(methodId, accountId, fromDate, toDate);
			response.setValue(amount);
			response.setStatus(Response.SUCCESS);
			response.setMessage("Successfully added transaction.");
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
