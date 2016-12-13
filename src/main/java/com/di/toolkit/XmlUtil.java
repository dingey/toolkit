package com.di.toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author di
 */
public class XmlUtil {
	public static String start = "<![CDATA[";
	public static String end = "]]>";

	public <T> T toObject(String xml, Class<T> cl) {
		return null;
	}

	public Map<String, Object> toMap(String xml) {

		return null;
	}

	public static List<String> split(String xml) {
		int i = 0;
		int left = 0;// <
		int right = 0;// >
		int close = 0;// </ left+close=right;
		// int cSt = 0;// <![CDATA[
		// int cEnd = 0;// ]]>
		char[] cs = xml.toCharArray();
		List<Integer> ls = new ArrayList<Integer>();
		while (i < cs.length) {
			switch (cs[i]) {
			case '<':
				if (i == 0 || xml.indexOf(start, i - 1) != i) {
					if (cs[i + 1] != '/') {
						left++;
					} else {
						close++;
					}
				} else if (xml.indexOf(start, i - 1) == i) {
					i = xml.indexOf(end, i) + 3;
					continue;
					// cSt++;
					// cEnd++;
				}
				break;
			case '>':
				if (cs[i - 1] != ']' && cs[i - 2] != ']') {
					right++;
				}
				if ((left + close) == right && left == close) {
					ls.add(i);
				}
				break;
			default:
				break;
			}
			i++;
		}
		List<String> ss = new ArrayList<String>();
		if (ls.size() == 0) {
			ss.add(xml);
		} else if (ls.size() == 1) {
			ss.add(xml.substring(0, ls.get(0)));
			ss.add(xml.substring(ls.get(0) + 1));
		} else if (ls.size() > 1) {
			ss.add(xml.substring(0, ls.get(0) + 1));
			for (int j = 0; j < ls.size() - 1; j++) {
				ss.add(xml.substring(ls.get(j) + 1, ls.get(j + 1) + 1));
			}
		}
		return ss;
	}
}
