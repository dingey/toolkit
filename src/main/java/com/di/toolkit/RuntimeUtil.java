package com.di.toolkit;

/**
 * @author di
 */
public class RuntimeUtil {
	/**
	 * Call other executable files, such as: make their own exe, or download and
	 * install the software.
	 * 
	 * @param filePath
	 *            C:\\Program Files\\Notepad++\\notepad++.exe
	 * @return
	 */
	public static Process openExe(String filePath) {
		final Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(filePath);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Error exec!");
		}
		return process;
	}
}
