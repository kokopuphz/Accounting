package tsutsumi.accounts.common.response;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.XmlInterface;

public class BigDecimalResponse extends Response {
	BigDecimal data = new BigDecimal(0);
	
	public void setValue(BigDecimal data) {
		this.data = data;
	}
	
	public BigDecimal getValue() {
		return data;
	}
	
	
	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		Node stringNode = doc.createElement("VALUE");
		stringNode.setTextContent(data.toString());
		responseFrag.appendChild(stringNode);
		return responseFrag;
	}

	protected void populateContents(Node contents) {
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("VALUE")) 
				data = BigDecimal.valueOf(Double.valueOf(child.getTextContent()));
		}
	}
}
