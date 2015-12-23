package tsutsumi.accounts.common.reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractReferenceData {
	private static ArrayList<Type> typeList = new ArrayList<Type>();
	private static HashMap<Integer, ArrayList<Category>> categoryList = new HashMap<Integer, ArrayList<Category>>();
//	private static ArrayList<Method> methodList = new ArrayList<Method>();
	private static ArrayList<Account> accountList = new ArrayList<Account>();
	private static ArrayList<String> descriptionProposalText = new ArrayList<String>();
	private static ArrayList<Method> bankMethodList = new ArrayList<Method>();
	private static ArrayList<Method> creditCardMethodList = new ArrayList<Method>();
	private static ArrayList<Method> cashMethodList = new ArrayList<Method>();
	
	private static HashMap<Integer, Method> methodIdMap = new HashMap<Integer, Method>();
	private static HashMap<Integer, Account> accountIdMap = new HashMap<Integer, Account>();
	private static HashMap<Integer, Category> categoryIdMap = new HashMap<Integer, Category>();
	private static HashMap<Integer, Type> typeIdMap = new HashMap<Integer, Type>();
	
	public static void setTypes(List<Type> array) {
		typeList.clear();
		typeList.addAll(array);
		for (Type a : typeList) {
			typeIdMap.put(a.getId(), a);
		}
	}
	
	public static ArrayList<Type> getTypes() {
		return typeList;
	}

	public static Type resolveTypeFromId(int typeId) {
		if (typeIdMap.containsKey(typeId)) {
			return typeIdMap.get(typeId);
		}
		return null;
	}
	
	public static Method resolveMethodFromId(int methodId) {
		if (methodIdMap.containsKey(methodId)) {
			return methodIdMap.get(methodId);
		}
		return null;
	}

	public static Account resolveAccountFromId(int accountId) {
		if (accountIdMap.containsKey(accountId)) {
			return accountIdMap.get(accountId);
		}
		return null;
	}

	public static Category resolveCategoryFromId(int categoryId) {
		if (categoryIdMap.containsKey(categoryId)) {
			return categoryIdMap.get(categoryId);
		}
		return null;
	}

	public static void setMethods(List<Method> array) {
		bankMethodList.clear();
		creditCardMethodList.clear();
		cashMethodList.clear();
		for (Method m : array) {
			if (m.isSelectable()) {
				if (m.getTypeId()==1) {
					cashMethodList.add(m);
				} else if (m.getTypeId()==2) {
					bankMethodList.add(m);	
				} else if (m.getTypeId()==3) {
					creditCardMethodList.add(m);
				}
			}
			methodIdMap.put(m.getId(), m);
		}
	}
	
	public static void setCategories(List<Category> array) {
		categoryList.clear();
		for (Category c : array) {
			if (!categoryList.containsKey(c.getTypeId())) {
				categoryList.put(c.getTypeId(), new ArrayList<Category>());
			}
			categoryList.get(c.getTypeId()).add(c);
			categoryIdMap.put(c.getId(), c);
		}
	}
	
	public static void setAccounts(List<Account> array) {
		accountList.clear();
		accountList.addAll(array);
		for (Account a : accountList) {
			accountIdMap.put(a.getId(), a);
		}
	}
	
//	public static ArrayList<Method> getMethods() {
//		return methodList;
//	}

	public static ArrayList<Method> getBankMethods() {
		return bankMethodList;
	}

	public static ArrayList<Method> getCreditCardMethods() {
		return creditCardMethodList;
	}

	public static ArrayList<Method> getCashMethods() {
		return cashMethodList;
	}
	
	public static ArrayList<Category> getCategories(int typeId) {
		if (typeId==0) {
			ArrayList<Category> returnArray = new ArrayList<Category>();
			for (int i= 0; i < typeList.size(); i++) {
				returnArray.addAll(categoryList.get(typeList.get(i).getId()));	
			}
			return returnArray;
		}
		return categoryList.get(typeId);
	}

	public static ArrayList<Account> getAccounts() {
		return accountList;
	}
	
	public static String[] getDescriptionProposalTextArray() {
		return descriptionProposalText.toArray(new String[descriptionProposalText.size()]);
	}
	
	public static void setDescriptionProposalText(ArrayList<String> s) {
		descriptionProposalText = s;
	}

	public static void addDescriptionProposal(String s) {
		ArrayList<String> newArray = new ArrayList<String>();
		newArray.add(s);
		if (descriptionProposalText.contains(s)) {
			descriptionProposalText.remove(s);
		}
		newArray.addAll(descriptionProposalText);
		descriptionProposalText = newArray;
	}
	
	
	private int id;
	private String name;

	public AbstractReferenceData(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public AbstractReferenceData() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
}
