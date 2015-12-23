package tsutsumi.accounts.server.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.MonthlySummaryTotals;
import tsutsumi.accounts.common.ReportSummaryRecord;
import tsutsumi.accounts.common.TransactionRecord;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.server.commands.GetExpenditureSummaryCommand;

public class GetSummaryDbHandler {
	private PreparedStatement stmt;
	private ResultSet oraResultSet;
	
	
	public BigDecimal getExpenditureSummary(int type, int typeId, String startDate, String endDate) throws AccountsException {
		java.util.Date toDate = Utils.convStringToDate(endDate);
		java.util.Date fromDate = Utils.convStringToDate(startDate);
		Date sqlToDate = new Date(toDate.getTime());
		Date sqlFromDate = new Date(fromDate.getTime());

		BigDecimal returnValue = new BigDecimal(0);
		String subPart = "";
		if (type == GetExpenditureSummaryCommand.BY_ACCOUNT)
			subPart = "ACCOUNT_ID ";
		if (type == GetExpenditureSummaryCommand.BY_CATEGORY)
			subPart = "CATEGORY_ID ";
		
		String SQLQUERY = "SELECT " + subPart + ", sum(credit) " +
				"FROM transaction_main a, transaction_sub b " +
				"where a.transaction_id = b.transaction_id " +
				"and METHOD_ID = 8 " +
				"and transaction_date >= ? " +
				"and transaction_date <= ? " +
				"and " + subPart + "= ? " +
				"GROUP BY " + subPart;
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			stmt = DatabaseController.getConnection().prepareStatement(SQLQUERY);
			stmt.setDate(1,sqlFromDate);
			stmt.setDate(2,sqlToDate);
			stmt.setInt(3, typeId);

			oraResultSet = stmt.executeQuery();
			if (oraResultSet.next()) {
				returnValue = oraResultSet.getBigDecimal(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				if (oraResultSet != null) {
					oraResultSet.close();
					oraResultSet = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new AccountsException(e.getMessage());
			}
		}
		return returnValue; 
	}
	
	public ArrayList<TransactionRecord> getTransactions(int typeId, int methodId, int accountId, int categoryId, String startDate, String endDate, boolean showCredits, boolean showDebits, boolean creditCards) throws AccountsException {
		ArrayList<TransactionRecord> transactions = new ArrayList<TransactionRecord>();
		java.util.Date toDate = Utils.convStringToDate(endDate);
		java.util.Date fromDate = Utils.convStringToDate(startDate);
		Date sqlToDate = new Date(toDate.getTime());
		Date sqlFromDate = new Date(fromDate.getTime());
//		System.out.println(accountId);
		String SELECT = "" +
				"SELECT sub.method_id, sub.account_id, main.category_id, c.type_id, " +
				"credit, debit, transaction_date, description, sub.transaction_id, sub.sub_transaction_id ";
		String FROM = "from transaction_sub sub, transaction_main main, category_master c, method_master m ";
		String WHERE = "where sub.transaction_id = main.transaction_id " +
				"and sub.method_id not in (8,9) " +
				"and main.category_id = c.category_id " +
				"and sub.method_id = m.method_id " +
				"and main.transaction_date >= ? " +
				"and main.transaction_date <= ? ";
		if (typeId > 0)
			WHERE += "and c.type_id = " + typeId + " ";
		if (methodId > 0)
			WHERE += "and sub.method_id = " + methodId + " ";
		if (accountId > 0)
			WHERE += "and sub.account_id = " + accountId + " ";
		if (categoryId > 0)
			WHERE += "and main.category_id = " + categoryId + " ";
		if (!showCredits)
			WHERE += "and credit = 0 ";
		if (!showDebits)
			WHERE += "and debit = 0 ";
		if (creditCards) {
			FROM += ", cc_clearing ";
			WHERE += "and cc_clearing.transaction_id = main.transaction_id and cc_clearing.clearing_transaction_id = 0 ";
		}
		String ORDER = "order by transaction_date desc, sub.transaction_id desc, sub_transaction_id ";
		
		String SQL = SELECT + FROM + WHERE + ORDER;
//		System.out.println(SQL);
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			stmt = DatabaseController.getConnection().prepareStatement(SQL);
			stmt.setDate(1,sqlFromDate);
			stmt.setDate(2,sqlToDate);
			oraResultSet = stmt.executeQuery();
			int row = 0;
			while (oraResultSet.next()) {
				row++;
				int m = oraResultSet.getInt(1);
				int a = oraResultSet.getInt(2);
				int c = oraResultSet.getInt(3);
				int t = oraResultSet.getInt(4);
				BigDecimal credit = oraResultSet.getBigDecimal(5);
				BigDecimal debit = oraResultSet.getBigDecimal(6);
				Date transactionDate = oraResultSet.getDate(7);
				String formattedDate = Utils.convDateToString(transactionDate);
				String description = oraResultSet.getString(8);
				int transactionId = oraResultSet.getInt(9);
				int subTransactionId = oraResultSet.getInt(10);
				TransactionRecord r = new TransactionRecord(row, t, c, a, m, credit, debit, formattedDate, description, transactionId, subTransactionId);
				transactions.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				if (oraResultSet != null) {
					oraResultSet.close();
					oraResultSet = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new AccountsException(e.getMessage());
//				e.printStackTrace();
			}
		}
		return transactions;
	}
	
	
	public MonthlySummaryTotals getMonthlySummary(String groupBy, String showBy) throws AccountsException {
		MonthlySummaryTotals msr = new MonthlySummaryTotals();
		String SQL = "";
		String idString = "";
		String masterTableSQL = "";
		String joinSql = "";
		String showBySQL = "";
		if (showBy.equals("CREDIT"))
			showBySQL = "and (sub.credit > 0 or main.category_id in (17, 9, 10, 21)) ";
		if (showBy.equals("DEBIT"))
			showBySQL = "and (sub.debit > 0 and main.category_id not in (17, 9, 10, 21)) ";

		if (groupBy.equals("ACCOUNT")) {
			idString = "ACCOUNT_ID ";
			joinSql = "sub.account_id = master.account_id ";
//					"and not exists (select 1 from transaction_sub sub2 " +
//					"where sub2.transaction_id = sub.transaction_id " +
//					"and sub2.account_id = sub.account_id " +
//					"and sub2.sub_transaction_id <> sub.sub_transaction_id) ";
			masterTableSQL = "ACCOUNT_MASTER master ";
		} else if (groupBy.equals("METHOD")){
			idString = "METHOD_ID ";
			joinSql = "sub.method_id = master.method_id ";
			masterTableSQL = "METHOD_MASTER master ";
			
//		} else if (groupBy.equals("TYPE")){
//			idString = "TYPE_ID ";
//			joinSql = "master.type_id = category.type_id and category.category_id = main.category_id and method_id not in (8,9) ";
//			masterTableSQL = "TYPE_MASTER master, CATEGORY_MASTER category ";
		} else if (groupBy.equals("CATEGORY")) {
			idString = "CATEGORY_ID ";
			joinSql = "master.category_id = main.category_id ";
			masterTableSQL = "CATEGORY_MASTER master ";
		}
		
		SQL = "SELECT transaction_date, master." + idString + ", sum(credit), sum(debit) " +
			  "from transaction_main main, transaction_sub sub, " + masterTableSQL +
			  "where main.transaction_id = sub.transaction_id " +
			  "and sub.method_id not in (8,9) " +
			  "and " + joinSql + showBySQL +
			  "GROUP BY transaction_date, master." + idString + " ORDER BY transaction_date desc ";
		
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
//			System.out.println(SQL);
			stmt = DatabaseController.getConnection().prepareStatement(SQL);
			oraResultSet = stmt.executeQuery();
			while (oraResultSet.next()) {
				Date transactionDate = oraResultSet.getDate(1);
				String formattedDate = Utils.convDateToAcc(transactionDate);
				int id = oraResultSet.getInt(2);
				BigDecimal credit = oraResultSet.getBigDecimal(3);
				BigDecimal debit = oraResultSet.getBigDecimal(4);
				msr.add(formattedDate, id, credit, debit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				if (oraResultSet != null) {
					oraResultSet.close();
					oraResultSet = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new AccountsException(e.getMessage());
//				e.printStackTrace();
			}
		}
		return msr;
	}
	
	public ArrayList<ReportSummaryRecord> getReportSummary(int typeId, int methodId, int accountId, int categoryId, String startDate, String endDate) throws AccountsException {
		java.util.Date toDate = Utils.convStringToDate(endDate);
		java.util.Date fromDate = Utils.convStringToDate(startDate);
		Date sqlToDate = new Date(toDate.getTime());
		Date sqlFromDate = new Date(fromDate.getTime());
		String SQL = "" +
				"SELECT sub.method_id, sub.account_id, main.category_id, c.type_id, " +
				"sum(credit), " +
				"sum(debit) " +
				"from transaction_sub sub, transaction_main main, category_master c, method_master m " +
				"where sub.transaction_id = main.transaction_id " +
				"and sub.method_id not in (8,9) " +
				"and main.category_id = c.category_id " +
				"and sub.method_id = m.method_id " +
				"and main.transaction_date >= ? " +
				"and main.transaction_date <= ? ";
		if (typeId > 0)
			SQL += "and c.type_id = " + typeId + " ";
		if (methodId > 0)
			SQL += "and sub.method_id = " + methodId + " ";
		if (accountId > 0)
			SQL += "and sub.account_id = " + accountId + " ";
		if (categoryId > 0)
			SQL += "and main.category_id = " + categoryId + " ";
		SQL += "group by sub.method_id, sub.account_id, main.category_id, c.type_id, m.sort_order order by m.sort_order ";
		
		ArrayList<ReportSummaryRecord> sumRecords = new ArrayList<ReportSummaryRecord>();
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			stmt = DatabaseController.getConnection().prepareStatement(SQL);
			stmt.setDate(1,sqlFromDate);
			stmt.setDate(2,sqlToDate);
			oraResultSet = stmt.executeQuery();
			while (oraResultSet.next()) {
				int m = oraResultSet.getInt(1);
				int a = oraResultSet.getInt(2);
				int c = oraResultSet.getInt(3);
				int t = oraResultSet.getInt(4);
				BigDecimal credit = oraResultSet.getBigDecimal(5);
				BigDecimal debit = oraResultSet.getBigDecimal(6);
				ReportSummaryRecord r = new ReportSummaryRecord(t, c, a, m, credit, debit);
				sumRecords.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				if (oraResultSet != null) {
					oraResultSet.close();
					oraResultSet = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new AccountsException(e.getMessage());
//				e.printStackTrace();
			}
		}
		return sumRecords; 
	}
	
	public BigDecimal getIndividualSummary(int methodId, int accountId, String startDate, String endDate) throws AccountsException {
		java.util.Date toDate = Utils.convStringToDate(endDate);
		java.util.Date fromDate = Utils.convStringToDate(startDate);
		Date sqlToDate = new Date(toDate.getTime());
		Date sqlFromDate = new Date(fromDate.getTime());
		
		String TRANSACTION_SUB = "" +
				"select 'A', account_id, method_id, sum(credit) - sum(debit) " +
				"from transaction_sub a, transaction_main b " +
				"where a.transaction_id = b.transaction_id " +
				"and b.transaction_date >= ? " +
				"and b.transaction_date <= ? ";
		String GROUP_BY = "GROUP BY 'A' ";
		if (accountId > 0) {
			TRANSACTION_SUB += "and account_id = " + accountId + " ";
			GROUP_BY += ", ACCOUNT_ID "; 
		}
		if (methodId > 0) {
			TRANSACTION_SUB += "and method_id = " + methodId + " ";
			GROUP_BY += ", METHOD_ID "; 
		}
		TRANSACTION_SUB += GROUP_BY;
		BigDecimal returnValue = BigDecimal.valueOf(0);
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			stmt = DatabaseController.getConnection().prepareStatement(TRANSACTION_SUB);
			stmt.setDate(1,sqlFromDate);
			stmt.setDate(2,sqlToDate);
			oraResultSet = stmt.executeQuery();
			if (oraResultSet.next()) {
				returnValue = oraResultSet.getBigDecimal(4);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				if (oraResultSet != null) {
					oraResultSet.close();
					oraResultSet = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				throw new AccountsException(e.getMessage());
//				e.printStackTrace();
			}
		}
		return returnValue; 
	}

}
