package tsutsumi.accounts.server.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SchemaHandler {
	private PreparedStatement stmt;
	private ResultSet oraResultSet;
	
	public ArrayList<String> testSql() {
		ArrayList<String> returnString = new ArrayList<String>();
		String QUERY_STRING = "SELECT method_name FROM method_master where selectable_fg = yes";
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
				String schemaName = oraResultSet.getString(1);
				returnString.add(schemaName);
			}
			oraResultSet.close();
			stmt.close();
			oraResultSet=null;
			stmt=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnString;
	}

	
}
