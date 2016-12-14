package com.di.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author di
 */
public class XmlUtil {
	public static String start = "<![CDATA[";
	public static String end = "]]>";

	@SuppressWarnings("unchecked")
	public static <T> T toObject(String xml, Class<T> cl) {
		T o = null;
		if (cl == java.util.Collection.class || cl == java.util.List.class || cl == java.util.ArrayList.class) {
			List<Object> l = toList(xml);
			return (T) l;
		} else if (cl == java.util.Map.class || cl == java.util.HashMap.class) {
			Map<String, Object> m = toMap(xml);
			o = (T) m;
		} else {
			try {
				Map<String, Object> m = toMap(xml);
				o = cl.newInstance();
				set(m, o);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	private static <T> void set(Map<String, Object> m, T o) {
		Map<String, Object> m0 = (Map<String, Object>) m.get("element attributes");
		m.remove("element attributes");
		if (m0 != null) {
			try {
				Field f = o.getClass().getDeclaredField("attributes");
				f.setAccessible(true);
				Class<?> c1 = f.getType();
				Object fo = c1.getConstructor().newInstance();
				ClassUtil.setObjectFieldsValue(m0, fo);
				f.set(o, fo);
			} catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		ClassUtil.setObjectFieldsValue(m, o);
	}

	public static Map<String, Object> toMap(String xml) {
		Map<String, Object> m = new HashMap<>();
		m.put("element attributes", getAttributes(xml));
		if (getWrapperValue(xml).indexOf("<") == -1) {
			m.put(getWrapperName(xml), getWrapperValue(xml));
			return m;
		}
		xml = getWrapperValue(xml);
		List<String> split = split(xml);
		for (String s : split) {
			String value = getWrapperValue(s);
			String name = getWrapperName(s);
			if (!value.startsWith("<") && !value.startsWith(start)) {
				m.put(name, value);
			} else if (value.startsWith(start)) {
				m.put(name, replaceEscape(value));
			} else if (value.startsWith("<") && !isList(value)) {
				m.put(name, toMap(s));
			} else if (isList(value)) {
				m.put(name, toList(value));
			}
		}
		return m;
	}

	private static boolean isList(String xml) {
		String n1 = getWrapperName(xml);
		if (xml.indexOf("</" + n1 + ">") == -1) {
			return false;
		} else if (xml.substring(xml.indexOf("</" + n1 + ">")).indexOf("<" + n1 + ">") > -1) {
			return true;
		}
		return false;
	}

	private static List<Object> toList(String xml) {
		List<Object> ls = new ArrayList<>();
		for (String s : split(xml)) {
			if (isList(s)) {
				ls.add(toList(s));
			} else {
				ls.add(toMap(s));
			}
		}
		return ls;
	}

	private static Map<String, String> getAttributes(String xml) {
		String s = xml.substring(xml.indexOf("<") + 1, xml.indexOf(">"));
		String[] ss = s.split(" ");
		if (ss.length == 1) {
			return null;
		}
		Map<String, String> m = new HashMap<>();
		for (int i = 1; i < ss.length; i++) {
			String s0 = ss[i].trim();
			if (!s0.isEmpty()) {
				m.put(s0.split("=")[0], delQuot(s0.split("=")[1]));
			}
		}
		return m;
	}

	private static String delQuot(String s) {
		return s.substring(s.indexOf('"') + 1, s.lastIndexOf('"'));
	}

	private static String getWrapperName(String xml) {
		return xml.substring(xml.indexOf("<") > -1 ? (xml.indexOf("<") + 1) : 0,
				xml.indexOf(">") > -1 ? xml.indexOf(">") : xml.length()).split(" ")[0];
	}

	private static String getWrapperValue(String xml) {
		return xml.substring(xml.indexOf(">") > -1 ? (xml.indexOf(">") + 1) : 0,
				xml.lastIndexOf("<") > -1 ? xml.lastIndexOf("<") : xml.length());
	}

	private static String replaceEscape(String xml) {
		return xml.substring(xml.indexOf(start) + 9, xml.lastIndexOf(end));
	}

	private static List<String> split(String xml) {
		int i = 0;
		int left = 0;// <
		int right = 0;// >
		int close = 0;// </ left+close=right;
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

	public static enum XmlEscape {
		AND('&', "a"), LESS('<', "&lt;"), GREAT('>', "&gt;"), QUOT('"', "&quot;"), APOS('\'', "&apos;");
		private char ch;
		private String value;

		private XmlEscape(char ch, String value) {
			this.value = value;
			this.ch = ch;
		}

		public static String escape(char c) {
			for (XmlEscape xe : XmlEscape.values()) {
				if (xe.ch == c) {
					return xe.value;
				}
			}
			return null;
		}

		public static char unEscape(String s) {
			for (XmlEscape xe : XmlEscape.values()) {
				if (xe.value.equals(s)) {
					return xe.ch;
				}
			}
			return ' ';
		}
	}
}
