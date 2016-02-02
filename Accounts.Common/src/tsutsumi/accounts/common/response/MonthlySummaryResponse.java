package tsutsumi.accounts.common.response;

import java.math.BigDecimal;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.MonthlySummaryTotals;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;

public class MonthlySummaryResponse extends Response {
	private MonthlySummaryTotals reportSummary = new MonthlySummaryTotals();
	
	public void setMonthlySummaryTotals(MonthlySummaryTotals rsr) {
		reportSummary=rsr;
	}
	
	public MonthlySummaryTotals getMonthlySummaryTotals() {
		return reportSummary;
	}

	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		for (String YYYYMM : reportSummary.getYYYYMMList()) {
			for (Integer id : reportSummary.getIdList()) {
				Node stringNode = doc.createElement("RECORD");
				responseFrag.appendChild(stringNode);
				
				Node elementNode = doc.createElement("YYYYMM");
				elementNode.setTextContent(String.valueOf(YYYYMM));
				stringNode.appendChild(elementNode);
				
				elementNode = doc.createElement("ID");
				elementNode.setTextContent(String.valueOf(id));
				stringNode.appendChild(elementNode);

				elementNode = doc.createElement("CREDIT");
				elementNode.setTextContent(reportSummary.getCredit(YYYYMM, id).toString());
				stringNode.appendChild(elementNode);

				elementNode = doc.createElement("DEBIT");
				elementNode.setTextContent(reportSummary.getDebit(YYYYMM, id).toString());
				stringNode.appendChild(elementNode);
			}
		}
		return responseFrag;
	}

	protected void populateContents(Node contents) {
		reportSummary = new MonthlySummaryTotals();
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("RECORD")) {
				NodeList values = child.getChildNodes();
				String YYYYMM =null;
				int id = 0;
				BigDecimal credit = new BigDecimal(0);
				BigDecimal debit = new BigDecimal(0);
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("YYYYMM"))
						YYYYMM = value.getTextContent();
					if (value.getNodeName().equals("ID"))
						id = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("CREDIT"))
						credit = new BigDecimal(Double.valueOf(value.getTextContent())).setScale(Utils.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
					if (value.getNodeName().equals("DEBIT"))
						debit = new BigDecimal(Double.valueOf(value.getTextContent())).setScale(Utils.DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
				}
				reportSummary.add(YYYYMM, id, credit, debit);
			}
		}
	}
}
