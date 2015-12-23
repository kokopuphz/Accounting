package tsutsumi.accounts.common.response;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.XmlInterface;

public class TestResponse extends Response {
	private ArrayList<String> stringList = new ArrayList<String>();
	
	public void setStringList(ArrayList<String> list) {
		stringList.addAll(list);
	}
	
	public ArrayList<String> getStringList() {
		return stringList;
	}
	
	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		for (String s : stringList) {
			Node stringNode = doc.createElement("STRING");
			stringNode.setTextContent(s);
			responseFrag.appendChild(stringNode);
		}
		return responseFrag;
	}

	protected void populateContents(Node contents) {
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("STRING")) {
				stringList.add(child.getTextContent());
			}
		}
	}
	
}
