/**
 * Data: 19/03/2016
 */
package br.ufrn.lets.stacktraceminer.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Teresa Fernandes
 *
 */
public class CalendarUtil {

	/** 
	 * Convert an string with Unix Time format to a Date
	 * @param unixTime
	 * @return
	 * */
	public static String convertUnixTimeToDate(String unixTime){
		if(unixTime != null && !unixTime.isEmpty() && !unixTime.contains("null")){
			Date date = new Date(Long.valueOf(unixTime) * 1000L);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			return sdf.format(date);
		}
		return null;
	}
	
	/** 
	 * Convert a Date on string with Unix Time format
	 * @param unixTime
	 * @return
	 * */
	public static String convertDateToUnixTime(Date date){
		if(date != null){
			return String.valueOf(date.getTime() / 1000);
		}
		return null;
	}
}
