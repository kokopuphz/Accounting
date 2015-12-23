package tsutsumi.accounts.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.response.ReferenceDataResponse;
import tsutsumi.accounts.common.response.Response;

public class Controller {
	public static Controller current;
	private static boolean listening = true;
	private ServerSocket serverSocket;
//	private Object synchObject = new Object();
	
	public Controller() throws IOException {
		serverSocket = new ServerSocket(5123);
		current = this;
        //initiate AbstractData
    	XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.REFERENCEDATA);
    	xmlRequest.setCommandParams("DATATYPE", "ALL");
    	ServerThread serverLocal = new ServerThread();
    	XmlInterface response = null;
    	try {
    		response = XmlInterface.parseString(serverLocal.postLocal(xmlRequest.getXML()));
    	} catch (Exception e) {
    		e.printStackTrace();
//    		System.exit(12);
    	}
//    	AccountsClient client = AccountsClient.getDefault();
//    	XmlInterface response = client.post(xmlRequest);
    	Response r = response.getResponse();
//    	System.out.println(r.getStatus());
    	if (r.getStatus()==Response.SUCCESS && r instanceof ReferenceDataResponse){ 
	    	ReferenceDataResponse tr = (ReferenceDataResponse)r;
	    	AbstractReferenceData.setMethods(tr.getMethodArray());
	    	AbstractReferenceData.setCategories(tr.getCategoryArray());
	    	AbstractReferenceData.setAccounts(tr.getAccountArray());
	    	AbstractReferenceData.setDescriptionProposalText(tr.getDescriptionProposalText());
	    	AbstractReferenceData.setTypes(tr.getTypeArray());
    	}
        while (listening) {
        	listen();
        }
	}

    public static void main(String[] args) throws IOException {
        try {
        	new Controller();
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5123.");
            System.exit(-1);
        }
        System.exit(0);
    }
    
	public void listen() {
		try {
			new ServerThread(serverSocket.accept()).start();
		} catch (SocketException e) {
			System.out.println("System shutdown detected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
