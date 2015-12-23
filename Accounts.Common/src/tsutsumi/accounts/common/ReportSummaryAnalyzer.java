package tsutsumi.accounts.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportSummaryAnalyzer {
	ArrayList<ReportSummaryRecord> rsrList;
	HashMap<Integer, BigDecimal[]> typeMap;
	HashMap<Integer, BigDecimal[]> accountMap;
	HashMap<Integer, BigDecimal[]> categoryMap;
	HashMap<Integer, BigDecimal[]> methodMap;
	
	public ReportSummaryAnalyzer() {
		rsrList = new ArrayList<ReportSummaryRecord>();
		typeMap = new HashMap<Integer, BigDecimal[]>();
		accountMap = new HashMap<Integer, BigDecimal[]>();
		categoryMap = new HashMap<Integer, BigDecimal[]>();
		methodMap = new HashMap<Integer, BigDecimal[]>();
	}
	
	public void setRecords(ArrayList<ReportSummaryRecord> rsrList) {
		this.rsrList = rsrList;
		analyze();
	}
	
	public ArrayList<ReportSummaryRecord> getTypeResults() {
		//public ReportSummaryRecord(int t, int c, int a, int m, BigDecimal credit, BigDecimal debit) {
		ArrayList<ReportSummaryRecord> result = new ArrayList<ReportSummaryRecord>();
		for (int typeId : typeMap.keySet()) {
			ReportSummaryRecord rt = new ReportSummaryRecord(typeId, 0, 0, 0, typeMap.get(typeId)[0], typeMap.get(typeId)[1]);
			result.add(rt);
		}
		return result;
	}

	public ArrayList<ReportSummaryRecord> getCategoryResults() {
		//public ReportSummaryRecord(int t, int c, int a, int m, BigDecimal credit, BigDecimal debit) {
		ArrayList<ReportSummaryRecord> result = new ArrayList<ReportSummaryRecord>();
		for (int id : categoryMap.keySet()) {
			ReportSummaryRecord rt = new ReportSummaryRecord(0, id, 0, 0, categoryMap.get(id)[0], categoryMap.get(id)[1]);
			result.add(rt);
		}
		return result;
	}
	
	public ArrayList<ReportSummaryRecord> getAccountResults() {
		//public ReportSummaryRecord(int t, int c, int a, int m, BigDecimal credit, BigDecimal debit) {
		ArrayList<ReportSummaryRecord> result = new ArrayList<ReportSummaryRecord>();
		for (int id : accountMap.keySet()) {
			ReportSummaryRecord rt = new ReportSummaryRecord(0, 0, id, 0, accountMap.get(id)[0], accountMap.get(id)[1]);
			result.add(rt);
		}
		return result;
	}

	public ArrayList<ReportSummaryRecord> getMethodResults() {
		//public ReportSummaryRecord(int t, int c, int a, int m, BigDecimal credit, BigDecimal debit) {
		ArrayList<ReportSummaryRecord> result = new ArrayList<ReportSummaryRecord>();
		for (int id : methodMap.keySet()) {
			ReportSummaryRecord rt = new ReportSummaryRecord(0, 0, 0, id, methodMap.get(id)[0], methodMap.get(id)[1]);
			result.add(rt);
		}
		return result;
	}

	private void analyze() {
		for (ReportSummaryRecord rsr : rsrList) {
			int t = rsr.getTypeId();
			int c = rsr.getCategoryId();
			int m = rsr.getMethodId();
			int a = rsr.getAccountId();
			BigDecimal credit = rsr.getCredit();
			BigDecimal debit = rsr.getDebit();
			if (!typeMap.containsKey(t))
				typeMap.put(t, new BigDecimal[] { new BigDecimal(0), new BigDecimal(0) } );
			if (!accountMap.containsKey(a))
				accountMap.put(a, new BigDecimal[] { new BigDecimal(0), new BigDecimal(0) } );
			if (!categoryMap.containsKey(c))
				categoryMap.put(c, new BigDecimal[] { new BigDecimal(0), new BigDecimal(0) } );
			if (!methodMap.containsKey(m))
				methodMap.put(m, new BigDecimal[] { new BigDecimal(0), new BigDecimal(0) } );
			
			typeMap.get(t)[0]=typeMap.get(t)[0].add(credit);
			typeMap.get(t)[1]=typeMap.get(t)[1].add(debit);
			accountMap.get(a)[0]=accountMap.get(a)[0].add(credit);
			accountMap.get(a)[1]=accountMap.get(a)[1].add(debit);
			categoryMap.get(c)[0]=categoryMap.get(c)[0].add(credit);
			categoryMap.get(c)[1]=categoryMap.get(c)[1].add(debit);
			methodMap.get(m)[0]=methodMap.get(m)[0].add(credit);
			methodMap.get(m)[1]=methodMap.get(m)[1].add(debit);
		}
	}
	
	
}
