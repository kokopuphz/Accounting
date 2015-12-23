package tsutsumi.accounts.server.commands;

import java.util.ArrayList;

import tsutsumi.accounts.common.Command;
import tsutsumi.accounts.common.response.Response;
import tsutsumi.accounts.common.response.TestResponse;
import tsutsumi.accounts.server.db.SchemaHandler;

public class TestCommand extends Command {
	public Response process() {
		SchemaHandler sh = new SchemaHandler();
		ArrayList<String> stringList = sh.testSql();
		TestResponse response = new TestResponse();
		response.setStringList(stringList);
		return response;
	}

	@Override
	public void setParams(String param, String value) {
		// TODO Auto-generated method stub
		
	}
	
}
