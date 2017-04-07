package br.ufrn.lets.stacktraceminer.util;

/**
 * @author Teresa Fernandes
 *
 */
public class RegexUtil {
	
	/**
	 * Regular expressions corresponding to the classes of POSIX
	 * Reference: http://docs.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html
	 * */
	private static final String lowerRegex = "a-z";
	private static final String upperRegex = "A-Z";
	private static final String alphaRegex = lowerRegex+upperRegex;
	private static final String digitRegex = "0-9";
	private static final String alnumRegex = alphaRegex+digitRegex;
	private static final String punctRegex = "!\"#$%&\'()*+,-.\\/:;<=>?@\\[\\]\\^_`{|}~ ";
	private static final String graphRegex = alnumRegex+punctRegex;
	private static final String blankRegex = " \\t";
	

	/** Regular expression that define an stacktrace <br/>
	 * Reference: https://regex101.com/r/eJ8uC3/8*/
	public static final String exceptionRegex = "((["+graphRegex+"]*["+blankRegex+"]+|)((["+alnumRegex+".]+)(Exception|Error))(.*))";
	private static final String atRegex = "(\\s+at\\s+)";
	private static final String methodPathRegex = "(["+graphRegex+"]*)";
	private static final String classRegex = "(((["+graphRegex+"]*.["+lowerRegex+"]+):(["+digitRegex+"]*))|(["+graphRegex+"]*))";
	public static final String stackFrameRegex = "("+exceptionRegex+"?("+atRegex+"("+methodPathRegex+"\\("+classRegex+"\\))))";
	public static final String causedByRegex = "(((Caused by([^\\n]*))\\n)+"+atRegex+methodPathRegex+"\\("+classRegex+"\\))";
	

	/**
	 * Indexes that represents the components grouping of the frames
	*/
	/** The 5 group corresponds exception thrown. */
	public static final int exceptionNameGroupRegex = 4;
	/** The 7 group corresponds the description (complement) of the exception thrown.
	 * Like the name of the exception, this information is captured from the first line before the string that starts with 'at'*/
	public static final int exceptionDescGroupRegex = 2;
	/** The 9 group corresponds the methos that throw the exception*/
	public static final int methodPathGroupRegex = 11;
	/** The 12 group corresponds the class that contains the method*/
	public static final int classGroupRegex = 14;
	/** The 13 group corresponds the class line which the exception was thrown.*/
	public static final int lineGroupRegex = 15;
	/** The 14 group corresponds the description if the java class is not found ("unnkown source" or "native method")*/
	public static final int unknownMethodGroupRegex = 16;
	
}
