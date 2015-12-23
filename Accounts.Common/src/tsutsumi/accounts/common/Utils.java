package tsutsumi.accounts.common;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

public class Utils {
	private static NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
	private static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM");
	static {
		formatter.setMaximumFractionDigits(0);
	}
	
	public static String formatNumber(BigDecimal number) {
		if (number==null)
			number = new BigDecimal(0);
		return formatter.format(number.doubleValue());
	}

	public static String formatNumber(double number) {
		return formatter.format(number);
	}
	public static boolean isNumeric(String str) {  
		try {  
			Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}

	public synchronized static String convStringToAcc(String YYYYMMDD) {
		String returnString = "";
		try {
			Date date = formatter1.parse(YYYYMMDD);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());
			if (cal.get(Calendar.DAY_OF_MONTH) > 24) {
				cal.add(Calendar.MONTH, 1);
			}
			returnString = formatter2.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnString;
	}
	
	public synchronized static String convDateToAcc(java.sql.Date date) {
		String returnString = "";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());
			if (cal.get(Calendar.DAY_OF_MONTH) > 24) {
				cal.add(Calendar.MONTH, 1);
			}
			returnString = formatter2.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnString;
	}

	public synchronized static String convDateToString(java.sql.Date date) {
		String returnString = "";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());
			returnString = formatter1.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnString;
	}

	public synchronized static String convDateToString(java.util.Date date) {
		String returnString = "";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());
			returnString = formatter1.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnString;
	}

	public synchronized static String convAccToStartString(String YYYYMM) {
		String returnString = "";
		try {
			Date date = formatter2.parse(YYYYMM);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());
			cal.add(Calendar.MONTH, -1);
			returnString = formatter2.format(cal.getTime());
			returnString += "/25";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnString;
	}

	public synchronized static String convAccToEndString(String YYYYMM) {
		return YYYYMM + "/24";
	}

	public synchronized static Date convStringToDate(String YYYYMMDD) {
		Date toDate = null;
		try {
			toDate = formatter1.parse(YYYYMMDD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toDate;
	}

	public static String getToday() {
		Calendar cal = Calendar.getInstance();
		return formatter1.format(cal.getTime());
	}
}
