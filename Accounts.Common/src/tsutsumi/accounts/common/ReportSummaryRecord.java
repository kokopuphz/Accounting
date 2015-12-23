package tsutsumi.accounts.common;

import java.math.BigDecimal;

public class ReportSummaryRecord {
	private int m;
	private int c;
	private int a;
	private int t;
	
	private BigDecimal credit;
	private BigDecimal debit;
	public ReportSummaryRecord(int t, int c, int a, int m, BigDecimal credit, BigDecimal debit) {
		this.m = m;
		this.t = t;
		this.c = c;
		this.a = a;
		this.credit = credit;
		this.debit = debit;
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
	
}
