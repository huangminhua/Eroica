package eroica.util;

/**
 * Some utils of strings.
 * 
 * @author Minhua HUANG
 *
 */
public class StringUtils {
	public static String[] splitAndTrim(String str, String regex) {
		String[] ss = str.split(regex);
		for (int i = 0; i < ss.length; i++) {
			ss[i] = ss[i].trim();
		}
		return ss;
	}
}
