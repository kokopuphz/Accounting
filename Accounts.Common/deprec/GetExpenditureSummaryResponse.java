package tsutsumi.accounts.common.response;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.XmlInterface;

public class GetExpenditureSummaryResponse extends Response {
	ArrayList<Object[]> data = new ArrayList<Object[]>();
	
	public void setData(ArrayList<Object[]> data) {
		this.data = data;
	}
	
	public ArrayList<Object[]> getData() {
		return data;
	}
	
	
	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		for (Object[] obj : data) {
			int typeId = (Integer)obj[0];
			BigDecimal amount = (BigDecimal)obj[1];
			Node stringNode = doc.createElement("SUMMARY");
			responseFrag.appendChild(stringNode);
			Node methodNode = doc.createElement("ID");
			methodNode.setTextContent(String.valueOf(typeId));
			stringNode.appendChild(methodNode);
			Node amountNode = doc.createElement("AMOUNT");
			amountNode.setTextContent(String.valueOf(amount));
			stringNode.appendChild(amountNode);
		}
		return responseFrag;
	}

	protected void populateContents(Node contents) {
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("SUMMARY")) {
				NodeList values = child.getChildNodes();
				int typeId=0;
				BigDecimal amount=null;
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("ID"))
						typeId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("AMOUNT"))
						amount = BigDecimal.valueOf(Double.valueOf(value.getTextContent()));
				}
				data.add(new Object[]{typeId, amount});
			}
		}
	}
}
