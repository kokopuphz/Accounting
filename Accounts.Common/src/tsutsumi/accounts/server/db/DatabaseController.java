package tsutsumi.accounts.server.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DatabaseController {

	private Connection conn;
	private static DatabaseController dbController;
	String user = "user";
	String password = "password";
	String host = "remotehost";
	int port=929;
	
	private DatabaseController() {
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			session.setPortForwardingL(3929, "localhost", 3929);
	    } catch(Exception e){
	    	System.err.print(e);
		}

		
		dbController = this;
		try {
			DriverManager.registerDriver((Driver)Class.forName("org.mariadb.jdbc.Driver").newInstance());
			
		} catch (Exception e) {
			System.out.println("Driver NOT loaded!");
			e.printStackTrace();
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
	}
	
	
	public static void go(){
	}
	
	private void openConnection() throws SQLException {
		String url = "jdbc:mariadb://localhost:3929/accounting_dollars";
		String username="root";
		String pasword="password";
		conn = DriverManager.getConnection(url, username, pasword);
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
