package tsutsumi.accounts.common;

import java.math.BigDecimal;
import java.util.HashMap;

import tsutsumi.accounts.common.response.DefaultResponse;
import tsutsumi.accounts.common.response.Response;

public abstract class Command {
	HashMap<String, String> params = new HashMap<String, String>();
	public static void processXmlInterface(XmlInterface obj) {
		Response dr = new DefaultResponse();
		try {
			Command command = (Command)Class.forName(obj.getCommandEnum().getCommandId()).newInstance();
			obj.applyParams(command);
			dr = command.process();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			dr.setStatus(Response.ABORT);
			dr.setMessage(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			dr.setStatus(Response.ABORT);
			dr.setMessage(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			dr.setStatus(Response.ABORT);
			dr.setMessage(e.getMessage());
		} catch (AccountsException e) {
			e.printStackTrace();
			dr.setStatus(Response.ABORT);
			dr.setMessage(e.getMessage());
		} finally {
			obj.setResponse(dr);
		}
	}
	
	public abstract Response process() throws AccountsException;
	public void setParams(String param, String value) throws AccountsException {
		params.put(param, value);
	}
//	public void setParams(String param, int value) throws AccountsException {
//		params.put(param, String.valueOf(value));
//	}
//	public void setParams(String param, boolean value) throws AccountsException {
//		params.put(param, String.valueOf(value));
//	}
//	public void setParams(String param, BigDecimal value) throws AccountsException {
//		params.put(param, value.toString());
//	}
//	public void setParams(String param, int[] value) throws AccountsException {
//		String valueString = "";
//		for (int i =0; i < value.length; i++) {
//			if (i == value.length -1) {
//				valueString += String.valueOf(value[i]) + ",";
//			} else {
//				valueString += String.valueOf(value[i]);
//			}
//		}
//		params.put(param, valueString);
//	}

	
	protected String getParam(String param, String defaultValue) {
		if (params.containsKey(param)) {
			return params.get(param);
		}
		return defaultValue;
	}
	
	protected int getParam(String param, int defaultValue) {
		if (params.containsKey(param)) {
			return Integer.valueOf(params.get(param));
		}
		return defaultValue;
	}

	protected boolean getParam(String param, boolean defaultValue) {
		if (params.containsKey(param)) {
			return Boolean.valueOf(params.get(param));
		}
		return defaultValue;
	}
	
	protected BigDecimal getParam(String param, BigDecimal defaultValue) {
		if (params.containsKey(param))
			return BigDecimal.valueOf(Double.valueOf(params.get(param)));
		return defaultValue;
	}
	
	protected int[] getParam(String param, int[] defaultValue) {
		if (params.containsKey(param)) {
			String intArray = params.get(param);
			String[] array = intArray.split(",");
			int[] returnArray = new int[array.length];
			for (int i = 0; i < array.length; i++) {
				returnArray[i] = Integer.valueOf(array[i]);
			}
			return returnArray;
		}
		return defaultValue;
	}
}
