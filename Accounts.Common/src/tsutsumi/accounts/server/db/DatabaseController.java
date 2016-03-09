package tsutsumi.accounts.server.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DatabaseController {

	private Connection conn;
	private static DatabaseController dbController;

	private String sshUser = "xxx";
	private String sshPassword = "xxx";
	private String sshHost = "xx.xx.com";
	private int sshPort=100;
	private int sqlPort=500;
	private String sqlUser = "xx";
	private String sqlPass = "xx";
	private String sqlDbName = "xx";
	private String url = "jdbc:mariadb://localhost:" + sqlPort + "/" + sqlDbName;
	
	private static Session session; 
	
	private DatabaseController() {
		dbController = this;
		try {
			DriverManager.registerDriver((Driver)Class.forName("org.mariadb.jdbc.Driver").newInstance());
			
		} catch (Exception e) {
			System.out.println("Driver NOT loaded!");
			e.printStackTrace();
		    throw new IllegalStateException("Cannot find the driver in the classpath!", e);
		}
	}
	
	private void openConnection() throws SQLException {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sshUser, sshHost, sshPort);
			session.setPassword(sshPassword);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			session.setPortForwardingL(sqlPort, "localhost", sqlPort);
			System.out.println("ssh connection established");
	    } catch(Exception e){
	    	System.err.print(e);
		}

		conn = DriverManager.getConnection(url, sqlUser, sqlPass);
		conn.setAutoCommit(true);
	}

	
	public static Connection getConnection() throws SQLException {
		if (dbController == null) {
			dbController = new DatabaseController();
		}
		if (dbController.conn == null || session == null || !sshIsOpen() || dbController.conn.isClosed()) {
			dbController.openConnection();			
		}
		return dbController.conn;
	}
	
	private static boolean sshIsOpen() {
		if (session==null)
			return false;
		try  {
	        ChannelExec testChannel = (ChannelExec)session.openChannel("exec");
	        testChannel.setCommand("true");
	        testChannel.connect();
	        testChannel.disconnect();
	        return true;
		} catch (Exception e) {
			session.disconnect();
			session = null;
			return false;
		}
	}
	
	
	public static void close() throws SQLException {
		dbController.conn.close();
		dbController.conn = null;
	}
	
}
