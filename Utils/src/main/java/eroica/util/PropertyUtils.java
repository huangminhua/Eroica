package eroica.util;

import java.util.Properties;

/**
 * Tools for properties.
 * 
 * @author Minhua HUANG
 *
 */
public class PropertyUtils {

	/**
	 * Fulfill src with props, according to the format of prefix and suffix.<br>
	 * For example: <br>
	 * 1 src = "a{b}{c}", prefix = "{", suffix = "}",<br>
	 * props contains {"b": "c", "c": "d"}, then the result is acd.<br>
	 * 2 src = "a{b{c}}", prefix = "{", suffix = "}",<br>
	 * props contains {"b": "c", "c": "d", "bd": "ef"}, then the result is aef.<br>
	 * 
	 * @param src
	 * @param placeHolderPrefix
	 * @param placeHolderSuffix
	 * @param props
	 * @return
	 */
	public static String fulfill(String src, String placeHolderPrefix, String placeHolderSuffix, Properties props) {
		boolean replaced = false;
		do {
			replaced = false;
			for (String key : props.stringPropertyNames()) {
				String k = placeHolderPrefix + key + placeHolderSuffix;
				int i = src.indexOf(k);
				if (i >= 0) {
					src = src.substring(0, i) + props.getProperty(key) + src.substring(i + k.length());
					replaced = true;
				}
			}
		} while (replaced);
		return src;
	}

	/**
	 * Deep clone props.
	 * 
	 * @param props
	 * @return
	 */
	public static Properties clone(Properties props) {
		Properties tmp = new Properties();
		for (String key : props.stringPropertyNames())
			tmp.setProperty(key, props.getProperty(key));
		return tmp;
	}
}
