/**
 * Data: 19/03/2016
 */
package br.ufrn.lets.stacktraceminer.util;

/**
 * @author Teresa Fernandes
 *
 */
public class StringUtil {

	/** 
	 * Remove html tags of the string
	 * @param noHTMLString
	 * @return
	 * */
	public static String removeHtmlTags(String noHTMLString){
		if(noHTMLString !=null){
			noHTMLString =  removeDuplicateWhiteSpaces(noHTMLString);
			noHTMLString = noHTMLString.replaceAll("\\<.*?>","");
		}
		return noHTMLString;
	}
	
	/** 
	 * Remove duplicate white spaces of the string
	 * @param stringDuplicateSpaces
	 * @return
	 * */
	public static String removeDuplicateWhiteSpaces(String stringDuplicateSpaces){
		if(stringDuplicateSpaces !=null){
			while (stringDuplicateSpaces.indexOf("  ") != -1)
				stringDuplicateSpaces = stringDuplicateSpaces.replace("  ", " ");
		}
		return stringDuplicateSpaces;
	}
}
