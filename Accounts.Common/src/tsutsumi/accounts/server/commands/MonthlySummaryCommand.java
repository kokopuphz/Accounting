package tsutsumi.accounts.server.commands;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.MonthlySummaryResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.server.db.GetSummaryDbHandler;

public class MonthlySummaryCommand extends Command {
	public static final int BY_CATEGORY = 1;
	public static final int BY_ACCOUNT = 2;
	
	public Response process() throws AccountsException {
		String groupBy = getParam("GROUP_BY", "ACCOUNT");
//		int typeId = getParam("TYPE_ID", 1);
		String showBy = getParam("SHOW_BY", "BALANCE");
		
		GetSummaryDbHandler sh = new GetSummaryDbHandler();
		MonthlySummaryResponse response = new MonthlySummaryResponse();
		try {
			response.setMonthlySummaryTotals(sh.getMonthlySummary(groupBy, showBy));
			response.setStatus(Response.SUCCESS);
			response.setMessage("Successfully grabbed summary");
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
