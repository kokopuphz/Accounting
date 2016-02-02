package tsutsumi.accounts.common.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.ReportSummaryRecord;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;

public class ReportSummaryResponse extends Response {
	private ArrayList<ReportSummaryRecord> reportSummary = new ArrayList<ReportSummaryRecord>();
	
	public void addSummary(List<ReportSummaryRecord> rsr) {
		reportSummary.addAll(rsr);
	}
	
	public ArrayList<ReportSummaryRecord> getSummary() {
		return reportSummary;
	}

	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		for (ReportSummaryRecord s : reportSummary) {
			Node stringNode = doc.createElement("REPORTSUMMARY");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("METHOD_ID");
			elementNode.setTextContent(String.valueOf(s.getMethodId()));
			stringNode.appendChild(elementNode);

			Node elementNode4 = doc.createElement("TYPE_ID");
			elementNode4.setTextContent(String.valueOf(s.getTypeId()));
			stringNode.appendChild(elementNode4);

			Node elementNode5 = doc.createElement("ACCOUNT_ID");
			elementNode5.setTextContent(String.valueOf(s.getAccountId()));
			stringNode.appendChild(elementNode5);

			Node elementNode6 = doc.createElement("CATEGORY_ID");
			elementNode6.setTextContent(String.valueOf(s.getCategoryId()));
			stringNode.appendChild(elementNode6);

			Node elementNode2 = doc.createElement("CREDIT");
			elementNode2.setTextContent(s.getCredit().toString());
			stringNode.appendChild(elementNode2);

			Node elementNode3 = doc.createElement("DEBIT");
			elementNode3.setTextContent(s.getDebit().toString());
			stringNode.appendChild(elementNode3);
		}
		return responseFrag;
	}

	protected void populateContents(Node contents) {
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("REPORTSUMMARY")) {
				NodeList values = child.getChildNodes();
				int methodId = 0;
				int accountId = 0;
				int typeId = 0;
				int categoryId = 0;
				BigDecimal credit = new BigDecimal(0);
				BigDecimal debit = new BigDecimal(0);
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("METHOD_ID"))
						methodId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("CATEGORY_ID"))
						categoryId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("TYPE_ID"))
						typeId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("ACCOUNT_ID"))
						accountId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("CREDIT"))
						credit = new BigDecimal(Double.valueOf(value.getTextContent())).setScale(Utils.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
					if (value.getNodeName().equals("DEBIT"))
						debit = new BigDecimal(Double.valueOf(value.getTextContent())).setScale(Utils.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
				}
				reportSummary.add(new ReportSummaryRecord(typeId, categoryId, accountId, methodId, credit, debit));
			}
		}
	}
}
