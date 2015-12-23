package tsutsumi.accounts.common;

import java.math.BigDecimal;

public class TransactionRecord {
	private int m;
	private int c;
	private int a;
	private int t;
	private BigDecimal credit;
	private BigDecimal debit;
	String transactionDate;
	String description;
	int transactionId;
	int subTransactionId;
	private int rownumber;
	public TransactionRecord(int row, int t, int c, int a, int m, BigDecimal credit, BigDecimal debit, String transDate, String desc, int transId, int subTransId) {
		this.m = m;
		this.t = t;
		this.c = c;
		this.a = a;
		this.credit = credit;
		this.debit = debit;
		this.transactionDate = transDate;
		this.description = desc;
		this.transactionId = transId;
		this.subTransactionId = subTransId;
		this.rownumber = row;
	}
	
	public int getMethodId() {
		return m;
	}
	
	public BigDecimal getCredit() {
		return credit;
	}
	
	public BigDecimal getDebit() {
		return debit;
	}
	
	public int getAccountId() {
		return a;
	}
	
	public int getCategoryId() {
		return c;
	}
	
	public int getTypeId() {
		return t;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	public int getSubTransactionId() {
		return subTransactionId;
	}
	public int getRownumber() {
		return rownumber;
	}
}
