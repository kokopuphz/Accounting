package tsutsumi.accounts.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class MonthlySummaryTotals {
	private ArrayList<String> YYYYMMList = new ArrayList<String>();
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	private HashMap<Integer, BigDecimal> runningTotals = new HashMap<Integer, BigDecimal>();
	private HashMap<String, MonthlySummaryRecord> mapList = new HashMap<String, MonthlySummaryRecord>();
	
	
	public void add(String YYYYMM, int id, BigDecimal credit, BigDecimal debit) {
		if (!YYYYMMList.contains(YYYYMM)) {
			YYYYMMList.add(YYYYMM);
			MonthlySummaryRecord msr = new MonthlySummaryRecord();
			mapList.put(YYYYMM, msr);
		}
		if (!idList.contains(id)) {
			idList.add(id);
		}
		mapList.get(YYYYMM).addCredit(id, credit);
		mapList.get(YYYYMM).addDebit(id, debit);
		if (!runningTotals.containsKey(id)) {
			runningTotals.put(id, new BigDecimal(0));
		}
		runningTotals.put(id,runningTotals.get(id).add(credit).subtract(debit));
	}
	
	public BigDecimal getTotal(int id) {
		return runningTotals.get(id)== null ? new BigDecimal(0): runningTotals.get(id);
	}
	
	public HashMap<Integer, BigDecimal> getTotals() {
		return new HashMap<Integer, BigDecimal>(runningTotals);
	}
	
	public ArrayList<String> getYYYYMMList() {
		return YYYYMMList;
	}
	
	public BigDecimal getCredit(String YYYYMM, int id) {
		if (!YYYYMMList.contains(YYYYMM)) {
			return new BigDecimal(0);
		}
		return mapList.get(YYYYMM).getCredit(id);
	}

	public BigDecimal getDebit(String YYYYMM, int id) {
		if (!YYYYMMList.contains(YYYYMM)) {
			return new BigDecimal(0);
		}
		return mapList.get(YYYYMM).getDebit(id);
	}
	
	public ArrayList<Integer> getIdList() {
		return idList;
	}
}
