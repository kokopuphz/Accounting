package tsutsumi.accounts.rcp.handlers;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import tsutsumi.accounts.client.AccountsClient;
import tsutsumi.accounts.common.CommandEnum;
import tsutsumi.accounts.common.Utils;
import tsutsumi.accounts.common.XmlInterface;
import tsutsumi.accounts.common.reference.AbstractReferenceData;
import tsutsumi.accounts.common.reference.Account;
import tsutsumi.accounts.common.reference.Category;
import tsutsumi.accounts.common.reference.Method;
import tsutsumi.accounts.common.response.BigDecimalResponse;
import tsutsumi.accounts.common.response.Response;

public class BalanceCashAmountsHandler extends AbstractHandler {
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ArrayList<Account> accountList = AbstractReferenceData.getAccounts();
		Account monthlyAccount = AbstractReferenceData.resolveAccountFromId(17);
		Method cashMethod = AbstractReferenceData.resolveMethodFromId(1);
		Method mitsuiMethod = AbstractReferenceData.resolveMethodFromId(4);
		BigDecimal zeroAmount = new BigDecimal(0);
		Category withdrawalCategory = AbstractReferenceData.resolveCategoryFromId(10);
		Category depositCategory = AbstractReferenceData.resolveCategoryFromId(9);
		ArrayList<int[]> updatedRefs = new ArrayList<int[]>();
		for (Account a : accountList) {
			if (a.getId() != 17) {
				BigDecimal amount = getAmount(cashMethod, a);
				if (amount.compareTo(zeroAmount)< 0) {
					//amount is negative need to withdraw
					BigDecimal transAmount = zeroAmount.subtract(amount);
					String description = "ATM " + withdrawalCategory.getName() + " :" + mitsuiMethod.getName() + ":" + a.getName();
					int returnCode = addTrans(a, withdrawalCategory, mitsuiMethod, cashMethod, transAmount, description);
					description = "ATM " + depositCategory.getName() + " :" + mitsuiMethod.getName() + ":" + monthlyAccount.getName();
					if (returnCode == 1) {
						addTrans(monthlyAccount, depositCategory, cashMethod, mitsuiMethod, transAmount, description);
						
					}
					updatedRefs.add(new int[]{mitsuiMethod.getId(), a.getId(), withdrawalCategory.getId()});
					updatedRefs.add(new int[]{mitsuiMethod.getId(), monthlyAccount.getId(), depositCategory.getId()});
					updatedRefs.add(new int[]{cashMethod.getId(), monthlyAccount.getId(), withdrawalCategory.getId()});
					updatedRefs.add(new int[]{cashMethod.getId(), a.getId(), depositCategory.getId()});
				} else if (amount.compareTo(zeroAmount)>0) {
					String description = "ATM " + depositCategory.getName() + " :" + mitsuiMethod.getName() + ":" + a.getName();
					int returnCode = addTrans(a, depositCategory, cashMethod, mitsuiMethod, amount, description);
					description = "ATM " + withdrawalCategory.getName() + " :" + mitsuiMethod.getName() + ":" + monthlyAccount.getName();
					if (returnCode == 1) {
						addTrans(monthlyAccount, withdrawalCategory, mitsuiMethod, cashMethod, amount, description);
					}
					updatedRefs.add(new int[]{mitsuiMethod.getId(), monthlyAccount.getId(), withdrawalCategory.getId()});
					updatedRefs.add(new int[]{mitsuiMethod.getId(), a.getId(), depositCategory.getId()});
					updatedRefs.add(new int[]{cashMethod.getId(), a.getId(), withdrawalCategory.getId()});
					updatedRefs.add(new int[]{cashMethod.getId(), monthlyAccount.getId(), depositCategory.getId()});
				}
			}
		}
		
    	for (int[] refs : updatedRefs) {
    		ArrayList<Parameterization> parameters = new ArrayList<Parameterization>();
    		IParameter iparam;
    		Parameterization params;
    		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    		ICommandService cmdService = (ICommandService)window.getService(ICommandService.class);
    		Command cmd = cmdService.getCommand("Accounts.Main.UpdateAmounts");
    		try {
	    		iparam = cmd.getParameter("Accounts.Main.UpdateAmounts.MethodId");
	    		params = new Parameterization(iparam, String.valueOf(refs[0]));
	    		parameters.add(params);
	
	    		iparam = cmd.getParameter("Accounts.Main.UpdateAmounts.AccountId");
	    		params = new Parameterization(iparam, String.valueOf(refs[1]));
	    		parameters.add(params);
	
	    		iparam = cmd.getParameter("Accounts.Main.UpdateAmounts.CategoryId");
	    		params = new Parameterization(iparam, String.valueOf(refs[2]));
	    		parameters.add(params);
    		} catch (NotDefinedException e) {
    			e.printStackTrace();
    		}
    		//build the parameterized command
    		ParameterizedCommand pc = new ParameterizedCommand(cmd, parameters.toArray(new Parameterization[parameters.size()]));
    		//execute the command
    		IHandlerService handlerService = (IHandlerService)window.getService(IHandlerService.class);
    		try {
    			handlerService.executeCommand(pc, null);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		return null;
	}
	
	private int addTrans(Account a, Category c, Method fromMethod, Method toMethod, BigDecimal amount, String description) {
    	XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.ADD);
    	xmlRequest.setCommandParams("DATE", Utils.getToday());
    	xmlRequest.setCommandParams("FROM_ACCOUNT_ID", String.valueOf(a.getId()));
    	xmlRequest.setCommandParams("TO_ACCOUNT_ID", String.valueOf(a.getId()));
    	xmlRequest.setCommandParams("CATEGORY_ID", String.valueOf(c.getId()));
    	xmlRequest.setCommandParams("FROM_METHOD_ID", String.valueOf(fromMethod.getId()));
    	xmlRequest.setCommandParams("TO_METHOD_ID", String.valueOf(toMethod.getId()));
    	xmlRequest.setCommandParams("AMOUNT", amount.toString());
    	xmlRequest.setCommandParams("DESCRIPTION", description);
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	return rr.getStatus();
	}


	private BigDecimal getAmount(Method m, Account a) {
		String sDate = "1900/01/01";
		String eDate = Utils.getToday();
		BigDecimal returnValue = new BigDecimal(0);
		XmlInterface xmlRequest = new XmlInterface();
    	xmlRequest.setCommand(CommandEnum.GET_INDIVIDUAL_SUMMARY);
    	xmlRequest.setCommandParams("ACCOUNT_ID",String.valueOf(a.getId()));
    	xmlRequest.setCommandParams("METHOD_ID",String.valueOf(m.getId()));
    	xmlRequest.setCommandParams("TO_DATE",eDate);
    	xmlRequest.setCommandParams("FROM_DATE",sDate);
    	AccountsClient client = AccountsClient.getDefault();
    	XmlInterface response = client.post(xmlRequest);
    	Response rr = response.getResponse();
    	if (rr != null && rr.getStatus() == Response.SUCCESS) {
    		BigDecimalResponse sumresponse = (BigDecimalResponse)rr;
    		return sumresponse.getValue();
    	}
    	return returnValue;
	}
}
