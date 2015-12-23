package tsutsumi.accounts.server.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.reference.AbstractReferenceData;

public class AddTransactionDbHandler {
	private PreparedStatement stmt;
	private ResultSet oraResultSet;
	private SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");

	public int addTransaction  (
			String dateValue, 
			int categoryId, 
			String description,
			int fromAccountId,
			int toAccountId,
			int fromMethodId,
			int toMethodId,
			BigDecimal amount,
			int[] ccTransactionIds
			) throws AccountsException {
		
		int returnCode = 9;
		java.util.Date date = null;
		try {
			date = formatter1.parse(dateValue);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date sqlDate = new Date(date.getTime());
		String TRANSACTION_MAIN = "INSERT INTO transaction_main(transaction_date, category_id, description) values(?,?,?) ";
		String TRANSACTION_SUB = "INSERT INTO transaction_sub(transaction_id, sub_transaction_id, account_id, method_id, credit, debit) " +
				"values(?,?,?,?,?,?)";		
		String CC_CLEARING_INSERT = "INSERT INTO CC_CLEARING(transaction_id, clearing_transaction_id) values (?, ?) ";
		String CC_CLEARING_UPDATE = "UPDATE CC_CLEARING set clearing_transaction_id = ? where transaction_id = ? ";
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			stmt = DatabaseController.getConnection().prepareStatement(TRANSACTION_MAIN);
			stmt.setDate(1, sqlDate);
			stmt.setInt(2, categoryId);
			stmt.setString(3, description);
			//setBigDecimal
			returnCode = stmt.executeUpdate();
			stmt.close();
			stmt = null;
			
			if (returnCode == 1) {
				stmt = DatabaseController.getConnection().prepareStatement("SELECT @@IDENTITY FROM TRANSACTION_MAIN ");
				oraResultSet = stmt.executeQuery();
				oraResultSet.next();
				int keyValue = oraResultSet.getInt(1);
//				System.out.println(keyValue);
				oraResultSet.close();
				oraResultSet = null;
				stmt.close();
				stmt = null;

				stmt = DatabaseController.getConnection().prepareStatement(TRANSACTION_SUB);
				stmt.setInt(1, keyValue);
				stmt.setInt(2, 1);
				stmt.setInt(3, fromAccountId);
				stmt.setInt(4, fromMethodId);
				stmt.setBigDecimal(5, BigDecimal.valueOf(0));
				stmt.setBigDecimal(6, amount);
				returnCode = stmt.executeUpdate();
				stmt.close();
				stmt = null;
				if (returnCode == 1) {
					stmt = DatabaseController.getConnection().prepareStatement(TRANSACTION_SUB);
					stmt.setInt(1, keyValue);
					stmt.setInt(2, 2);
					stmt.setInt(3, toAccountId);
					stmt.setInt(4, toMethodId);
					stmt.setBigDecimal(5, amount);
					stmt.setBigDecimal(6, BigDecimal.valueOf(0));
					returnCode = stmt.executeUpdate();
					stmt.close();
					stmt = null;
				}
				if (AbstractReferenceData.resolveMethodFromId(fromMethodId).isCcFlag()) {
					stmt = DatabaseController.getConnection().prepareStatement(CC_CLEARING_INSERT);
					stmt.setInt(1, keyValue);
					stmt.setInt(2, 0);
					returnCode = stmt.executeUpdate();
					stmt.close();
					stmt = null;
				}
				if (categoryId==21) {
					//cc clearing transaction
					for (int transid : ccTransactionIds) {
						stmt = DatabaseController.getConnection().prepareStatement(CC_CLEARING_UPDATE);
						stmt.setInt(1, keyValue);
						stmt.setInt(2, transid);
						returnCode = stmt.executeUpdate();
						stmt.close();
						stmt = null;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
//				if (returnCode == 1) {
//					DatabaseController.getConnection().commit();
//				} else {
//					DatabaseController.getConnection().rollback();
//				}

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
		return returnCode;
	}
}
