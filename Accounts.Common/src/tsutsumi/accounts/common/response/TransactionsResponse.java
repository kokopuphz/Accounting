package tsutsumi.accounts.common.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.TransactionRecord;
import tsutsumi.accounts.common.XmlInterface;

public class TransactionsResponse extends Response {
	private ArrayList<TransactionRecord> transactions = new ArrayList<TransactionRecord>();
	private BigDecimal total = new BigDecimal(0);
	
	public void addTransactions(List<TransactionRecord> rsr) {
		transactions.addAll(rsr);
		for (TransactionRecord t : rsr) {
			total = total.add(t.getCredit()).subtract(t.getDebit());
		}
	}
	
	public ArrayList<TransactionRecord> getTransactions() {
		return transactions;
	}

	public BigDecimal getTotal() {
		return total;
	}
	
	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		Node totalNode = doc.createElement("TOTAL");
		responseFrag.appendChild(totalNode);
		totalNode.setTextContent(total.toString());

		for (TransactionRecord s : transactions) {
			Node stringNode = doc.createElement("TRANSACTIONS");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("ROWNUMBER");
			elementNode.setTextContent(String.valueOf(s.getRownumber()));
			stringNode.appendChild(elementNode);

			Node elementNode0 = doc.createElement("METHOD_ID");
			elementNode0.setTextContent(String.valueOf(s.getMethodId()));
			stringNode.appendChild(elementNode0);

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
			
			Node elementNode7 = doc.createElement("TRANSACTION_DATE");
			elementNode7.setTextContent(String.valueOf(s.getTransactionDate()));
			stringNode.appendChild(elementNode7);

			Node elementNode8 = doc.createElement("DESCRIPTION");
			elementNode8.setTextContent(String.valueOf(s.getDescription()));
			stringNode.appendChild(elementNode8);

			Node elementNode9 = doc.createElement("TRANSACTION_ID");
			elementNode9.setTextContent(String.valueOf(s.getTransactionId()));
			stringNode.appendChild(elementNode9);

			Node elementNode10 = doc.createElement("SUB_TRANSACTION_ID");
			elementNode10.setTextContent(String.valueOf(s.getSubTransactionId()));
			stringNode.appendChild(elementNode10);
		}
		return responseFrag;
	}
	
	protected void populateContents(Node contents) {
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("TOTAL")) {
				total = new BigDecimal(Double.valueOf(child.getTextContent()));
			} else if (child.getNodeName().equals("TRANSACTIONS")) {
				NodeList values = child.getChildNodes();
				int rownumber = 0;
				int methodId = 0;
				int accountId = 0;
				int typeId = 0;
				int categoryId = 0;
				BigDecimal credit = new BigDecimal(0);
				BigDecimal debit = new BigDecimal(0);
				String transDate = null;
				String desc = null;
				int transId = 0;
				int subTransId = 0;
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("ROWNUMBER"))
						rownumber = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("METHOD_ID"))
						methodId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("CATEGORY_ID"))
						categoryId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("TYPE_ID"))
						typeId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("ACCOUNT_ID"))
						accountId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("CREDIT"))
						credit = new BigDecimal(Double.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("DEBIT"))
						debit = new BigDecimal(Double.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("TRANSACTION_DATE"))
						transDate = value.getTextContent();
					if (value.getNodeName().equals("DESCRIPTION"))
						desc = value.getTextContent();
					if (value.getNodeName().equals("TRANSACTION_ID"))
						transId = Integer.valueOf(value.getTextContent());
					if (value.getNodeName().equals("SUB_TRANSACTION_ID"))
						subTransId = Integer.valueOf(value.getTextContent());
				}
				transactions.add(new TransactionRecord(rownumber, typeId, categoryId, accountId, methodId, credit, debit, transDate, desc, transId, subTransId));
			}
		}
	}
}
