package com.di.toolkit;

/**
 * @author di:
 * @date 创建时间：2016年10月24日 下午11:21:45
 * @version
 */
public class StringUtil {
	public static final String lineSeparator = System.getProperty("line.separator", "\n");

	public static String firstCharLower(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}

	public static String firstCharUpper(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	public static String trimUnderlinedFirstCharUpper(String s) {
		if (s.indexOf("_") != -1) {
			String[] ss = s.split("_");
			StringBuilder sb = new StringBuilder(ss[0]);
			if (ss.length > 1) {
				for (int i = 1; i < ss.length; i++) {
					sb.append(firstCharUpper(ss[i]));
				}
			}
			return sb.toString();
		} else {
			return s;
		}
	}
}
