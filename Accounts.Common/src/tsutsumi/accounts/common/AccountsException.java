package tsutsumi.accounts.common;



public class AccountsException extends Exception {
	private static final long serialVersionUID = 1L;
	public AccountsException(String logMessage) {
		super(logMessage);
	}
}
