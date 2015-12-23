package tsutsumi.accounts.common;

import java.util.HashMap;

public enum CommandEnum {
	ADD("tsutsumi.accounts.server.commands.AddTransactionCommand"),
	TEST("tsutsumi.accounts.server.commands.TestCommand"),
	REFERENCEDATA("tsutsumi.accounts.server.commands.ReferenceDataCommand"),
	GET_EXPENDITURE_SUMMARY("tsutsumi.accounts.server.commands.GetExpenditureSummaryCommand"),
	GET_INDIVIDUAL_SUMMARY("tsutsumi.accounts.server.commands.GetIndividualSummaryCommand"),
	GET_REPORT_SUMMARY("tsutsumi.accounts.server.commands.GetReportSummaryCommand"),
	GET_TRANSACTIONS("tsutsumi.accounts.server.commands.GetTransactionsCommand"),
	MONTHLY_SUMMARY("tsutsumi.accounts.server.commands.MonthlySummaryCommand")
	;
	
	

	private static HashMap<String, CommandEnum> StringToCommandMap;
	private static HashMap<CommandEnum, String> CommandToStringMap;
	static {
		StringToCommandMap = new HashMap<String, CommandEnum>();
		StringToCommandMap.put("ADD", ADD);
		StringToCommandMap.put("TEST", TEST);
		StringToCommandMap.put("REFERENCEDATA", REFERENCEDATA);
		StringToCommandMap.put("GET_EXPENDITURE_SUMMARY", GET_EXPENDITURE_SUMMARY);
		StringToCommandMap.put("GET_INDIVIDUAL_SUMMARY", GET_INDIVIDUAL_SUMMARY);
		StringToCommandMap.put("GET_REPORT_SUMMARY", GET_REPORT_SUMMARY);
		StringToCommandMap.put("GET_TRANSACTIONS", GET_TRANSACTIONS);
		StringToCommandMap.put("MONTHLY_SUMMARY", MONTHLY_SUMMARY);
		
		CommandToStringMap = new HashMap<CommandEnum, String>();
		CommandToStringMap.put(ADD, "ADD");
		CommandToStringMap.put(TEST, "TEST");
		CommandToStringMap.put(REFERENCEDATA, "REFERENCEDATA");
		CommandToStringMap.put(GET_EXPENDITURE_SUMMARY, "GET_EXPENDITURE_SUMMARY");
		CommandToStringMap.put(GET_INDIVIDUAL_SUMMARY, "GET_INDIVIDUAL_SUMMARY");
		CommandToStringMap.put(GET_REPORT_SUMMARY, "GET_REPORT_SUMMARY");
		CommandToStringMap.put(GET_TRANSACTIONS, "GET_TRANSACTIONS");
		CommandToStringMap.put(MONTHLY_SUMMARY, "MONTHLY_SUMMARY");
	}
	
	private final String commandId;
	private CommandEnum(String commandId) {
		this.commandId = commandId;
	}
	
	public static CommandEnum parseString(String command) {
		return StringToCommandMap.get(command);
	}
	
	public String getCommandId() {
		return commandId;
	}
	
	public String toString() {
		return CommandToStringMap.get(this);
	}
	
}
