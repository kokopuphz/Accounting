package tsutsumi.accounts.server.commands;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.BigDecimalResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.server.db.GetSummaryDbHandler;

public class GetExpenditureSummaryCommand extends Command {
	public static final int BY_CATEGORY = 1;
	public static final int BY_ACCOUNT = 2;
	
	public Response process() throws AccountsException {
		int type =getParam("TYPE", BY_CATEGORY);
		int id = getParam("ID",0);
		String toDate = getParam("TO_DATE","3999/01/01");
		String fromDate = getParam("FROM_DATE","1900/01/01");
		
		GetSummaryDbHandler sh = new GetSummaryDbHandler();
		BigDecimalResponse response = new BigDecimalResponse();
		try {
			response.setValue(sh.getExpenditureSummary(type, id, fromDate, toDate));
			response.setStatus(Response.SUCCESS);
			response.setMessage("Successfully grabbed summary");
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
