package tsutsumi.accounts.server.commands;

import java.util.ArrayList;

import tsutsumi.accounts.common.AccountsException;
import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.reference.Type;
import tsutsumi.accounts.common.response.ReferenceDataResponse;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.server.db.ReferenceDataDbHandler;

public class ReferenceDataCommand extends Command {
	public Response process() {
		String loadType = getParam("LOADTYPE", "ALL");
		ReferenceDataDbHandler sh = new ReferenceDataDbHandler();
		ReferenceDataResponse response = new ReferenceDataResponse();
		try {
			if (loadType.equals("ALL") || loadType.equals("METHOD") || loadType.equals("EXCEPTPROP")) {
				ArrayList<Method> stringList = sh.getMethods();
				response.addMethods(stringList);
			}
			if (loadType.equals("ALL") || loadType.equals("ACCOUNT") || loadType.equals("EXCEPTPROP")) {			
				ArrayList<Account> accountList = sh.getAccounts();
				response.addAccounts(accountList);
			}
			if (loadType.equals("ALL") || loadType.equals("CATEGORY") || loadType.equals("EXCEPTPROP")) {
				ArrayList<Category> catList = sh.getCategories();
				response.addCategories(catList);
			}
			if (loadType.equals("ALL") || loadType.equals("DESCRIPTION_PROPOSAL")) {
				ArrayList<String> propList = sh.getDescriptionProposalText();
				response.addDescriptionProposalText(propList);
			}
			if (loadType.equals("ALL") || loadType.equals("TYPE") || loadType.equals("EXCEPTPROP")) {
				ArrayList<Type> typeList = sh.getTypes();
				response.addTypes(typeList);
			}

			response.setStatus(Response.SUCCESS);
		} catch (AccountsException e) {
			response.setStatus(Response.ABORT);
			e.printStackTrace();
		}
		return response;
	}
}
