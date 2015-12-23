package tsutsumi.accounts.common.response;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.XmlInterface;

public abstract class Response {
	public static final int ABORT = 9;
	public static final int SUCCESS = 1;
	public static final int DEFAULT = 9999;
	
	public static Response getFromXML(Node responseNode) {
		if (responseNode == null) {
			return null;
		}
		NodeList childList = responseNode.getChildNodes();
		Node contents = null;
		Response cr = new DefaultResponse();
		int responseStatus = Response.DEFAULT;
		String responseText = "";
		for (int i=0; i < childList.getLength(); i++) {
			Node child = childList.item(i);
			if (child.getNodeName().equals("TYPE")) {
				try {
					cr = (Response)Class.forName(child.getTextContent()).newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (child.getNodeName().equals("CONTENTS")) {
				contents = child;
			} else if (child.getNodeName().equals("RESPONSE_STATUS")) {
				responseStatus = Integer.valueOf(child.getTextContent());
			} else if (child.getNodeName().equals("RESPONSE_TEXT")) {
				responseText = child.getTextContent();
			}
		}
		cr.setStatus(responseStatus);
		cr.setMessage(responseText);
		cr.populateContents(contents);
		return cr;
	}
	
	public String toString() {
		return this.getClass().getCanonicalName();
	}
	
	
	public DocumentFragment getXmlFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		Node responseStatus = doc.createElement("RESPONSE_STATUS");
		responseStatus.setTextContent(String.valueOf(getStatus()));
		responseFrag.appendChild(responseStatus);

		Node responseMessage = doc.createElement("RESPONSE_TEXT");
		responseMessage.setTextContent(getMessage());
		responseFrag.appendChild(responseMessage);

		Node responseType = doc.createElement("TYPE");
		responseType.setTextContent(this.toString());
		responseFrag.appendChild(responseType);
		Node contents = doc.createElement("CONTENTS");
		DocumentFragment frag = getXmlContentFragment();
		if (frag != null)
			contents.appendChild(doc.adoptNode(getXmlContentFragment()));
		responseFrag.appendChild(contents);
		return responseFrag;
	}
	
	protected abstract DocumentFragment getXmlContentFragment();
	protected abstract void populateContents(Node contents);
	
	private int status = DEFAULT;
	private String message = "";
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setMessage(String s) {
		message = s;
	}
	public String getMessage() {
		return message;
	}
	
}
