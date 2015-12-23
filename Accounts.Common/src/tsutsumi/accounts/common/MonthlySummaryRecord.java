package tsutsumi.accounts.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthlySummaryRecord {
	private ArrayList<Integer> idList;
	private HashMap<Integer, BigDecimal> creditMap;
	private HashMap<Integer, BigDecimal> debitMap;
	
	public MonthlySummaryRecord() {
		idList = new ArrayList<Integer>();
		creditMap = new HashMap<Integer, BigDecimal>();
		debitMap = new HashMap<Integer, BigDecimal>();
	}
	
	public void addCredit(int id, BigDecimal credit) {
		if (!idList.contains(id)) {
			idList.add(id);
		}
		if (!creditMap.containsKey(id)) {
			creditMap.put(id, new BigDecimal(0));			
		}
		creditMap.put(id, creditMap.get(id).add(credit));
	}
	
	public void addDebit(int id, BigDecimal debit) {
		if (!idList.contains(id)) {
			idList.add(id);
		}
		if (!debitMap.containsKey(id)) {
			debitMap.put(id, new BigDecimal(0));			
		}
		debitMap.put(id, debitMap.get(id).add(debit));
	}
	
	public BigDecimal getCredit(int id) {
		if (creditMap.containsKey(id)) {
			return creditMap.get(id);
		}
		return new BigDecimal(0);
	}
	
	public BigDecimal getDebit(int id) {
		if (debitMap.containsKey(id)) {
			return debitMap.get(id);
		}
		return new BigDecimal(0);
	}
	
	public ArrayList<Integer> getIdList() {
		return idList;
	}
}
