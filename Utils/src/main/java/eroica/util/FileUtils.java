package eroica.util;

import java.io.File;

/**
 * Tools for operations on file system.
 * 
 * @author Minhua HUANG
 *
 */
public class FileUtils {
	/**
	 * Delete a file or directory recursively.
	 * 
	 * @param file a file or directory
	 * @return <code>true</code> if and only if the file or directory is
	 *         successfully deleted; <code>false</code> otherwise
	 */
	public static boolean deleteRecursively(File file) {
		if (!file.exists())
			return false;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteRecursively(files[i]);
			}
		}
		return file.delete();
	}
}
