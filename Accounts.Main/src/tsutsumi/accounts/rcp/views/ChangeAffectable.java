package tsutsumi.accounts.rcp.views;


public interface ChangeAffectable {
	public void updateAmount(final int methodId, final int accountId, final int categoryId);
}
