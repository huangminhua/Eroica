package eroica.util;

/**
 * Tools for string operations.
 * 
 * @author Minhua HUANG
 *
 */
public class StringUtils {
	/**
	 * Splits str by regex, and trims the results.
	 * 
	 * @param str   the string
	 * @param regex the delimiting regular expression
	 * @return the array of strings computed by splitting and trimmings
	 */
	public static String[] splitAndTrim(String str, String regex) {
		String[] ss = str.split(regex);
		for (int i = 0; i < ss.length; i++)
			ss[i] = ss[i].trim();
		return ss;
	}
}