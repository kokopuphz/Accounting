package tsutsumi.accounts.common;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import tsutsumi.accounts.common.response.Response;


public class XmlInterface {
	private Document doc;
	private static final char[] loc = new char[0];
	private static final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	private static final DocumentBuilder domParser;
	static {
		try {
			domParser = domFactory.newDocumentBuilder();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(new AccountsException(e.getMessage()));
		}
	}

	public XmlInterface() {
		doc = domParser.newDocument();
		Element rootNode = doc.createElement("XMLINTERFACE");
		doc.appendChild(rootNode);
	}
	
	public static Document getGenericDocument() {
		return domParser.newDocument();
	}
	
	public void setCommandParams(String param, String value) {
		Node rootNode = doc.getFirstChild();
		Node paramNode = doc.createElement("PARAM");
		rootNode.appendChild(paramNode);
		Node keyNode = doc.createElement("KEY");
		keyNode.setTextContent(param);
		paramNode.appendChild(keyNode);
		Node valueNode = doc.createElement("VALUE");
		valueNode.setTextContent(value);
		paramNode.appendChild(valueNode);
	}
	
	public void applyParams(Command command) throws AccountsException {
		NodeList nodes = doc.getElementsByTagName("PARAM");
		for (int i=0; i < nodes.getLength(); i++) {
			Node paramNode = nodes.item(i);
			NodeList keyValPair = paramNode.getChildNodes();
			String key = null;
			String value = null;
			for (int j=0; j < keyValPair.getLength(); j++) {
				Node keyValNode = keyValPair.item(j);
				if (keyValNode.getNodeName().equals("KEY"))
					key = keyValNode.getTextContent(); 
				if (keyValNode.getNodeName().equals("VALUE"))
					value = keyValNode.getTextContent(); 
			}
			if (key != null && value != null) {
				command.setParams(key, value);
			}
		}
	}

	public String getCommandParam(String param) {
		NodeList nodes = doc.getElementsByTagName("PARAM");
		for (int i=0; i < nodes.getLength(); i++) {
			Node paramNode = nodes.item(i);
			NodeList keyValPair = paramNode.getChildNodes();
			String key = null;
			String value = null;
			for (int j=0; j < keyValPair.getLength(); j++) {
				Node keyValNode = keyValPair.item(j);
				if (keyValNode.getNodeName().equals("KEY"))
					key = keyValNode.getTextContent(); 
				if (keyValNode.getNodeName().equals("VALUE"))
					value = keyValNode.getTextContent(); 
			}
			if (key.equals(param)) {
				return value;
			}
		}
		return null;
	}
	
//	private Node getNodeByTagName(String tagname) {
//		NodeList list = doc.getElementsByTagName(tagname);
//		return null;
//	}
	
	private Node getFirstNode(String tagname) {
		NodeList list = doc.getElementsByTagName(tagname);
		if (list.getLength() > 0) {
			return list.item(0);
		} else {
			return null;
		}
	}
	
	public void setCommand(CommandEnum commandId) {
		Node rootNode = doc.getFirstChild();
		if (doc.getElementsByTagName("COMMAND").getLength()==0) {
			Node commandNode = doc.createElement("COMMAND");
			commandNode.setTextContent(commandId.toString());
			rootNode.appendChild(commandNode);
		} else {
			Node commandNode = getFirstNode("COMMAND");
			commandNode.setTextContent(commandId.toString());
		}
	}
	
	public String getXML() throws AccountsException {
		StringWriter stringWriter = new StringWriter();
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
		} catch (Exception e) {
			throw new AccountsException(e.getMessage());
		}
		return stringWriter.toString();
	}
	
	public static XmlInterface parseString(String s) throws AccountsException  {
		XmlInterface xmlInt = new XmlInterface();
		try {
			synchronized (loc) {
				xmlInt.doc = domParser.parse(new InputSource(new StringReader(s)));
			}
		} catch (Exception e) {
			throw new AccountsException(e.getMessage());
		}
		return xmlInt;
	}
	
	public void setResponse(Response response) {
		Node rootNode = doc.getFirstChild();
		NodeList list = doc.getElementsByTagName("RESPONSE");
		if (list.getLength()>0) {
			for (int i = 0; i < list.getLength(); i++) {
				rootNode.removeChild(list.item(i));
			}
		}
		Node responseNode = doc.createElement("RESPONSE");
		rootNode.appendChild(responseNode);
		responseNode.appendChild(doc.adoptNode(response.getXmlFragment()));
	}
	
	public Response getResponse() {
		Node responseNode = getFirstNode("RESPONSE");
		return Response.getFromXML(responseNode);
	}
	
	public CommandEnum getCommandEnum() {
		Node commandNode = getFirstNode("COMMAND");
		if (commandNode != null) {
			return CommandEnum.parseString(commandNode.getTextContent());	
		}
		return null;
	}
}
