package tsutsumi.accounts.common.response;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.reference.Type;

public class ReferenceDataResponse extends Response {
	private ArrayList<Method> methodMaster = new ArrayList<Method>();
	private ArrayList<Category> categoryMaster = new ArrayList<Category>();
	private ArrayList<Account> accountMaster = new ArrayList<Account>();
	private ArrayList<Type> typeMaster = new ArrayList<Type>();
	private ArrayList<String> descriptionProposalText= new ArrayList<String>();
	
	public void addMethods(List<Method> methods) {
		methodMaster.addAll(methods);
	}

	public ArrayList<Method> getMethodArray() {
		return methodMaster;
	}

	public void addTypes(List<Type> types) {
		typeMaster.addAll(types);
	}

	public ArrayList<Type> getTypeArray() {
		return typeMaster;
	}

	public void addCategories(List<Category> cats) {
		categoryMaster.addAll(cats);
	}
	
	public ArrayList<Category> getCategoryArray() {
		return categoryMaster;
	}

	public void addAccounts(List<Account> cats) {
		accountMaster.addAll(cats);
	}
	
	public ArrayList<Account> getAccountArray() {
		return accountMaster;
	}

	public void addDescriptionProposalText(ArrayList<String> s) {
		descriptionProposalText.addAll(s);
	}
	
	public ArrayList<String> getDescriptionProposalText() {
		return descriptionProposalText;
	}
	
	
	protected DocumentFragment getXmlContentFragment() {
		Document doc = XmlInterface.getGenericDocument();
		DocumentFragment responseFrag = doc.createDocumentFragment();
		for (Method s : methodMaster) {
			Node stringNode = doc.createElement("METHOD");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("METHOD_ID");
			elementNode.setTextContent(String.valueOf(s.getId()));
			stringNode.appendChild(elementNode);

			Node elementNode2 = doc.createElement("METHOD_NAME");
			elementNode2.setTextContent(String.valueOf(s.getName()));
			stringNode.appendChild(elementNode2);

			Node elementNode3 = doc.createElement("CC_FLAG");
			elementNode3.setTextContent(String.valueOf(s.isCcFlag()));
			stringNode.appendChild(elementNode3);

			Node elementNode4 = doc.createElement("SORT_ORDER");
			elementNode4.setTextContent(String.valueOf(s.getSortOrder()));
			stringNode.appendChild(elementNode4);

			Node elementNode5 = doc.createElement("SELECTABLE");
			elementNode5.setTextContent(String.valueOf(s.isSelectable()));
			stringNode.appendChild(elementNode5);
			
			Node elementNode6 = doc.createElement("TYPE_ID");
			elementNode6.setTextContent(String.valueOf(s.getTypeId()));
			stringNode.appendChild(elementNode6);

		}
		
		for (Category s : categoryMaster) {
			Node stringNode = doc.createElement("CATEGORY");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("CATEGORY_ID");
			elementNode.setTextContent(String.valueOf(s.getId()));
			stringNode.appendChild(elementNode);

			Node elementNode2 = doc.createElement("CATEGORY_NAME");
			elementNode2.setTextContent(String.valueOf(s.getName()));
			stringNode.appendChild(elementNode2);
			
			Node elementNode3 = doc.createElement("EDITABLE");
			elementNode3.setTextContent(String.valueOf(s.isEditable()));
			stringNode.appendChild(elementNode3);

			Node elementNode4 = doc.createElement("TYPE_ID");
			elementNode4.setTextContent(String.valueOf(s.getTypeId()));
			stringNode.appendChild(elementNode4);
			
			Node elementNode5 = doc.createElement("SELECTABLE_FG");
			elementNode5.setTextContent(String.valueOf(s.isSelectable()));
			stringNode.appendChild(elementNode5);


		}		

		for (Account s : accountMaster) {
			Node stringNode = doc.createElement("ACCOUNT");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("ACCOUNT_ID");
			elementNode.setTextContent(String.valueOf(s.getId()));
			stringNode.appendChild(elementNode);

			Node elementNode2 = doc.createElement("ACCOUNT_NAME");
			elementNode2.setTextContent(String.valueOf(s.getName()));
			stringNode.appendChild(elementNode2);
		}

		for (Type s : typeMaster) {
			Node stringNode = doc.createElement("TYPE");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("TYPE_ID");
			elementNode.setTextContent(String.valueOf(s.getId()));
			stringNode.appendChild(elementNode);

			Node elementNode2 = doc.createElement("TYPE_NAME");
			elementNode2.setTextContent(String.valueOf(s.getName()));
			stringNode.appendChild(elementNode2);
		}

		for (String s: descriptionProposalText) {
			Node stringNode = doc.createElement("DESCRIPTION_PROPOSAL");
			responseFrag.appendChild(stringNode);

			Node elementNode = doc.createElement("STRING");
			elementNode.setTextContent(String.valueOf(s));
			stringNode.appendChild(elementNode);
		}
		return responseFrag;
	}

	protected void populateContents(Node contents) {
		NodeList childNodes = contents.getChildNodes();
		for (int i=0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeName().equals("METHOD")) {
				NodeList values = child.getChildNodes();
				Method method = new Method();
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("METHOD_ID"))
						method.setId(Integer.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("METHOD_NAME"))
						method.setName(String.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("CC_FLAG"))
						method.setCcFlag(Boolean.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("SORT_ORDER"))
						method.setSortOrder(Integer.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("SELECTABLE"))
						method.setSelectable(Boolean.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("TYPE_ID"))
						method.setTypeId(Integer.valueOf(value.getTextContent()));

				}
				methodMaster.add(method);
			}
			
			if (child.getNodeName().equals("CATEGORY")) {
				NodeList values = child.getChildNodes();
				Category cat = new Category();
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("CATEGORY_ID"))
						cat.setId(Integer.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("CATEGORY_NAME"))
						cat.setName(String.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("EDITABLE"))
						cat.setEditable(Boolean.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("TYPE_ID"))
						cat.setTypeId(Integer.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("SELECTABLE_FG"))
						cat.setSelectable(Boolean.valueOf(value.getTextContent()));
				}
				categoryMaster.add(cat);
			}

			if (child.getNodeName().equals("ACCOUNT")) {
				NodeList values = child.getChildNodes();
				Account cat = new Account();
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("ACCOUNT_ID"))
						cat.setId(Integer.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("ACCOUNT_NAME"))
						cat.setName(String.valueOf(value.getTextContent()));
				}
				accountMaster.add(cat);
			}
			
			if (child.getNodeName().equals("TYPE")) {
				NodeList values = child.getChildNodes();
				Type cat = new Type();
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("TYPE_ID"))
						cat.setId(Integer.valueOf(value.getTextContent()));
					if (value.getNodeName().equals("TYPE_NAME"))
						cat.setName(String.valueOf(value.getTextContent()));
				}
				typeMaster.add(cat);
			}

			
			if (child.getNodeName().equals("DESCRIPTION_PROPOSAL")) {
				NodeList values = child.getChildNodes();
				for (int j=0; j < values.getLength(); j++) {
					Node value = values.item(j);
					if (value.getNodeName().equals("STRING"))
						descriptionProposalText.add(value.getTextContent());
				}
			}
		}
	}
}
