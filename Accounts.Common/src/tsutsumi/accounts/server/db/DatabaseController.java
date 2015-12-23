package tsutsumi.accounts.server.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

//import oracle.jdbc.OracleDriver;

public class DatabaseController {

	private Connection conn;
	private static DatabaseController dbController;
	
	private DatabaseController() {
		dbController = this;
		try {
			DriverManager.registerDriver((Driver)Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance());
		} catch (Exception e) {
			
		}
	}
	
	private void openConnection() throws SQLException {
		String filename = "C:/WORKFOLDER/accounts.mdb.accdb";
		String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=";
		database+= filename.trim() + ";DriverID=22;READONLY=false}"; // add on to the end 
		conn = DriverManager.getConnection( database ,"",""); 
		conn.setAutoCommit(true);
	}

	
	public static Connection getConnection() throws SQLException {
		if (dbController == null) {
			dbController = new DatabaseController();
		}
		if (dbController.conn == null || dbController.conn.isClosed()) {
			dbController.openConnection();			
		}
		return dbController.conn;
	}
	
	
	
	public static void close() throws SQLException {
		dbController.conn.close();
		dbController.conn = null;
	}
	
}
