package tsutsumi.accounts.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.XmlInterface;

public class ServerThread extends Thread {
    private Socket socket = null;
    public ServerThread(Socket socket) {
    	super("ServerThread");
    	this.socket = socket;
    	
    }

    public ServerThread() {
    	
    }
    
    public String postLocal(String xmlRequest) {
    	try {
	    	XmlInterface xmlObject = XmlInterface.parseString(xmlRequest);
			Command.processXmlInterface(xmlObject);
			return xmlObject.getXML();
    	} catch (AccountsException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public void run() {
    	ObjectOutputStream oOut = null;
    	ObjectInputStream oIn = null;
		try {
			oOut = new ObjectOutputStream(socket.getOutputStream());
			oIn = new ObjectInputStream(socket.getInputStream());
		    String xmlRequest;
	    	while ((xmlRequest = (String)oIn.readObject()) != null) {
	    		XmlInterface xmlObject = null;
	    		try {
	    			xmlObject = XmlInterface.parseString(xmlRequest);
    				Command.processXmlInterface(xmlObject);
    	    		oOut.writeObject(xmlObject.getXML());
    	    		oOut.flush();
    	    		oOut.reset();
	    		} catch (AccountsException e) {
	    			e.printStackTrace();
	    			System.err.println("Occurred whilst processing ServerThread");
	    		}
	    	}
		} catch (IOException e) {
//		    e.printStackTrace();
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} finally {
			try {
				if (oOut != null)
					oOut.close();
				if (oIn != null)
					oIn.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

}
