package tsutsumi.accounts.server.commands;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.ReportSummaryResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.server.db.GetSummaryDbHandler;

public class GetReportSummaryCommand extends Command {
	public static final int BY_CATEGORY = 1;
	public static final int BY_ACCOUNT = 2;
	
	public Response process() throws AccountsException {
		int typeId =getParam("TYPE_ID", 0);
		int methodId = getParam("METHOD_ID",0);
		int accountId = getParam("ACCOUNT_ID",0);
		int categoryId = getParam("CATEGORY_ID",0);
		String fromDate = getParam("FROM_DATE","1900/01/01");
		String toDate = getParam("TO_DATE","3999/01/01");
		
		GetSummaryDbHandler sh = new GetSummaryDbHandler();
		ReportSummaryResponse response = new ReportSummaryResponse();
		try {
			response.addSummary(sh.getReportSummary(typeId, methodId, accountId, categoryId, fromDate, toDate));
			response.setStatus(Response.SUCCESS);
			response.setMessage("Successfully grabbed summary");
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			response.setMessage(e.getMessage());
		}
		return response;
	}
}
