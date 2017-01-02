package com.di.toolkit;

/**
 * @author di
 */
public class RuntimeUtil {
	/**
	 * 调用其他的可执行文件，例如：自己制作的exe，或是 下载 安装的软件.
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
			System.out.println("Error exec!");
		}
		return process;
	}
}
