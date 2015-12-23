package tsutsumi.accounts.server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.reference.Type;

public class ReferenceDataDbHandler {
	private PreparedStatement stmt;
	private ResultSet oraResultSet;
	
	public ArrayList<Method> getMethods() throws AccountsException {
		ArrayList<Method> returnString = new ArrayList<Method>();
		String QUERY_STRING = "SELECT method_id, method_name, cc_fg, sort_order, selectable_fg, method_type_id " +
				"FROM method_master order by sort_order";
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			if (stmt == null) {
				stmt = DatabaseController.getConnection().prepareStatement(QUERY_STRING);	
			}
			oraResultSet = stmt.executeQuery();
			
			while (oraResultSet.next())
			{
				Method method = new Method();
				method.setId(oraResultSet.getInt(1));
				method.setName(oraResultSet.getString(2));
				method.setCcFlag(oraResultSet.getBoolean(3));
				method.setSortOrder(oraResultSet.getInt(4));
				method.setSelectable(oraResultSet.getBoolean(5));
				method.setTypeId(oraResultSet.getInt(6));
				returnString.add(method);
			}
			oraResultSet.close();
			stmt.close();
			oraResultSet=null;
			stmt=null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		}
		return returnString;
	}

	public ArrayList<Category> getCategories() throws AccountsException {
		ArrayList<Category> returnString = new ArrayList<Category>();
		String QUERY_STRING = "SELECT category_id, category_name, editable, type_id, selectable_fg " +
				"FROM category_master order by sort_order";
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			if (stmt == null) {
				stmt = DatabaseController.getConnection().prepareStatement(QUERY_STRING);	
			}
			oraResultSet = stmt.executeQuery();
			
			while (oraResultSet.next())
			{
				Category cat = new Category();
				cat.setId(oraResultSet.getInt(1));
				cat.setName(oraResultSet.getString(2));
				cat.setEditable(oraResultSet.getBoolean(3));
				cat.setTypeId(oraResultSet.getInt(4));
				cat.setSelectable(oraResultSet.getBoolean(5));
				returnString.add(cat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				oraResultSet.close();
				stmt.close();
				oraResultSet=null;
				stmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AccountsException(e.getMessage());
			}
		}
		return returnString;
	}

	public ArrayList<Account> getAccounts() throws AccountsException {
		ArrayList<Account> returnString = new ArrayList<Account>();
		String QUERY_STRING = "SELECT account_id, account_name " +
				"FROM account_master ORDER BY sort_order";
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			if (stmt == null) {
				stmt = DatabaseController.getConnection().prepareStatement(QUERY_STRING);	
			}
			oraResultSet = stmt.executeQuery();
			
			while (oraResultSet.next())
			{
				Account account = new Account();
				account.setId(oraResultSet.getInt(1));
				account.setName(oraResultSet.getString(2));
				returnString.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				oraResultSet.close();
				stmt.close();
				oraResultSet=null;
				stmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AccountsException(e.getMessage());
			}
		}
		return returnString;
	}

	public ArrayList<Type> getTypes() throws AccountsException {
		ArrayList<Type> returnString = new ArrayList<Type>();
		String QUERY_STRING = "SELECT type_id, type_name " +
				"FROM type_master ORDER BY type_id ";
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			if (stmt == null) {
				stmt = DatabaseController.getConnection().prepareStatement(QUERY_STRING);	
			}
			oraResultSet = stmt.executeQuery();
			
			while (oraResultSet.next())
			{
				Type account = new Type();
				account.setId(oraResultSet.getInt(1));
				account.setName(oraResultSet.getString(2));
				returnString.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				oraResultSet.close();
				stmt.close();
				oraResultSet=null;
				stmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AccountsException(e.getMessage());
			}
		}
		return returnString;
	}

	public ArrayList<String> getDescriptionProposalText() throws AccountsException {
		ArrayList<String> returnString = new ArrayList<String>();
		String QUERY_STRING = "SELECT description " +
				"FROM transaction_main " +
				"GROUP BY description " +
				"ORDER BY max(transaction_date) desc ";
		try {
			if (oraResultSet != null) {
				oraResultSet.close();
				oraResultSet = null;
			}
			if (stmt == null) {
				stmt = DatabaseController.getConnection().prepareStatement(QUERY_STRING);	
			}
			oraResultSet = stmt.executeQuery();
			
			while (oraResultSet.next())
			{
				returnString.add(oraResultSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccountsException(e.getMessage());
		} finally{
			try {
				oraResultSet.close();
				stmt.close();
				oraResultSet=null;
				stmt=null;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AccountsException(e.getMessage());
			}
		}
		return returnString;
	}

	
}
