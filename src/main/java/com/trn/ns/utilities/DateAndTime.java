package com.trn.ns.utilities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class DateAndTime {
	private static final Logger LOGGER = Logg.createLogger();
	private DateAndTime() {
	}

	/**
	 * This method generates the Current Time in the milli seconds using the
	 * System Class
	 * 
	 * @return long - current time in milli seconds
	 */
	public static long getCurrentTimeInMillis() {
		long timeInMillis = 0;
		try {
			timeInMillis = System.currentTimeMillis();
			LOGGER.info("Current Time in millis is " + timeInMillis);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception in getCurrentTimeInMillis() method of DateAndTime Class "
							+ ex.getMessage(), ex);
		}
		return timeInMillis;
	}

	/**
	 * This method generates the Current Date And Time in the default format
	 * using the Date Object
	 * 
	 * @return String- string of Current Date And Time
	 */
	public static String getCurrentDateAndTime() {
		String dateTime = null;
		try {
			dateTime = new Date().toString();
			LOGGER.info("Current Date and Time is " + dateTime);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception in getCurrentDateAndTime() method of DateAndTime Class "
							+ ex.getMessage(), ex);
			return null;
		}
		return dateTime;
	}

	/**
	 * This method generates formatted Current Date And Time using the Date
	 * Object and SimpleDateFormat class
	 * 
	 * @param format
	 *            - string set by the User
	 * @return String- string of Current Date And Time formatted as per the user
	 */
	public static String getFormattedCurrentDateAndTime(String format) {
		String formattedDateTime = null;
		try {
			formattedDateTime = new SimpleDateFormat(format).format(new Date());
			LOGGER.info("Formatted(" + format + ") Current Date and Time is " + formattedDateTime);

		} catch (Exception ex) {
			LOGGER.error(
					"Exception in getFormattedCurrentDateAndTime() method of DateAndTime Class "
							+ ex.getMessage(), ex);
			return null;
		}
		return formattedDateTime;
	}

	/* change the region and language -- as provided in the bat file */
	void changeLocale() {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("cmd /c start /NorthStar/src/main/resources/com/commands/changelocale.bat");
			p.waitFor();
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}

		LOGGER.info("done");
	}

	/* change the date format -- as provided in the bat file */
	void changeDateFormat() {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("cmd /c start /NorthStar/src/main/resources/com/commands/changedateformat.bat");
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.info("done");
	}
}
