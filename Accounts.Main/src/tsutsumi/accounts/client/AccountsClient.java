package tsutsumi.accounts.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.server.ServerThread;

public class AccountsClient {
	private Socket socket = null;
    private static AccountsClient current = null;
    private static AccountsClient backGroundClient = null;
    private static AccountsClient historyClient = null;
    private ObjectOutputStream oOut;
    private ObjectInputStream oIn;
	private String hostname = "localhost";
	private static final boolean LOCAL = true;
	private ServerThread serverLocal;

	
    public static AccountsClient getDefault() {
    	if (current == null || current.socket == null || current.socket.isClosed()) {
    		current = new AccountsClient("Default");
    	}
    	return current;
    }

    public static AccountsClient getBackgroundClient() {
    	if (backGroundClient == null || backGroundClient.socket == null || backGroundClient.socket.isClosed()) {
    		backGroundClient = new AccountsClient("Background");
    	}
    	return backGroundClient;
    }

    public static AccountsClient getHistoryClient() {
    	if (historyClient == null || historyClient.socket == null || historyClient.socket.isClosed()) {
    		historyClient = new AccountsClient("History");
    	}
    	return historyClient;
    }

	private AccountsClient(String s) {
        connect();
	}
	
	public void connect() {
    	if (!LOCAL) {
	        try {
	            socket = new Socket(hostname, 443);
	
	//            SocketAddress addr = new InetSocketAddress("170.105.225.130", 8080);
	//            Proxy proxy = new Proxy(Proxy.Type.DIRECT, addr);
	//            socket = new Socket(proxy);
	//            InetSocketAddress dest = new InetSocketAddress("125.54.103.28", 443);
	//            socket.connect(dest);
	            
				oOut = new ObjectOutputStream(socket.getOutputStream());
				oIn = new ObjectInputStream(socket.getInputStream());
	        } catch (UnknownHostException e) {
	        	e.printStackTrace();
	            System.err.println("Could not find host");
	            close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	            System.err.println("IO Error");
	            close();
	        }
    	} else {
    		serverLocal = new ServerThread();
    	}
	}
	
	public synchronized XmlInterface post(XmlInterface xmlRequest) {
		XmlInterface response = xmlRequest;
		if (!LOCAL) {
			if (oOut == null) {
				connect();
			}
			if (oOut != null) {
				try {
					oOut.writeObject(xmlRequest.getXML());
					oOut.flush();
					oOut.reset();
					if (oIn != null) {
						try {
							response = XmlInterface.parseString((String)oIn.readObject());
						} catch (EOFException e) {
							e.printStackTrace();
							close();
						} catch (IOException e) {
							e.printStackTrace();
							close();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
							close();
						}
					}
				} catch (AccountsException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					close();
					System.out.println("Connection lost while sending command to server");
				}
			}
		} else {
			try {
				response = XmlInterface.parseString(serverLocal.postLocal(xmlRequest.getXML()));
			} catch (AccountsException e) {
				e.printStackTrace();
			}
		}
		return response;

	}
	
	public void close() {
		if (!LOCAL) {
			try {
				if (oOut != null) {
					oOut.close();
				}
				if (oIn != null) {
					oIn.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				oOut=null;
				oIn=null;
				socket=null;
				current=null;
			}
		}
	}
}

