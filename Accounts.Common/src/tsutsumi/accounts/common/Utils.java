package tsutsumi.accounts.common;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {
	private static DecimalFormat formatter = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US);
//	private static NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.JAPAN);
	private static SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
	private static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM");
	static {
		formatter.setMaximumFractionDigits(2);
		String symbol = formatter.getCurrency().getSymbol();
		formatter.setNegativePrefix("- "+ symbol); // or "-"+symbol if that's what you need
		formatter.setNegativeSuffix("");
	}
	public static int DECIMAL_PRECISION = 2;
	
	public static String formatNumber(BigDecimal number) {
		if (number==null)
			number = new BigDecimal(0).setScale(DECIMAL_PRECISION, BigDecimal.ROUND_HALF_UP);
		return formatNumber(number.doubleValue());
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
//			if (cal.get(Calendar.DAY_OF_MONTH) > 24) {
//				cal.add(Calendar.MONTH, 1);
//			}
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
//			if (cal.get(Calendar.DAY_OF_MONTH) > 24) {
//				cal.add(Calendar.MONTH, 1);
//			}
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
//		try {
//			Date date = formatter2.parse(YYYYMM);
//			Calendar cal = Calendar.getInstance();
//			cal.setTimeInMillis(date.getTime());
//			cal.add(Calendar.MONTH, -1);
//			returnString = formatter2.format(cal.getTime());
//			returnString += "/25";
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		returnString = YYYYMM + "/01";
		return returnString;
	}

	public synchronized static String convAccToEndString(String YYYYMM) {
		String returnString = "";
		try {
			Date date = formatter2.parse(YYYYMM + "/01");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date.getTime());
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
			returnString = formatter1.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnString;
//		return YYYYMM + "/24";
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
