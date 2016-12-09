package com.di.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author di
 */
public class JsonUtil {
	public static List<String> split(String json) {
		int off = 0;
		int ang1 = 0;// 尖括号{
		int ang2 = 0;// 尖括号}
		int squa1 = 0;// 方括号[
		int squa2 = 0;// 方括号]
		int quot = 0;// 引号
		List<Integer> is = new ArrayList<>();
		List<String> ss = new ArrayList<>();
		while (off < json.length()) {
			char c = json.charAt(off);
			switch (c) {
			case '"':
				if (off == 0 || json.charAt(off - 1) != '\\') {
					quot++;
				}
				break;
			case '{':
				if (quot % 2 == 0) {
					ang1++;
				}
				break;
			case '}':
				if (quot % 2 == 0) {
					ang2++;
				}
				break;
			case '[':
				if (quot % 2 == 0) {
					squa1++;
				}
				break;
			case ']':
				if (quot % 2 == 0) {
					squa2++;
				}
				break;
			case ',':
				if (quot % 2 == 0 && ang1 == ang2 && squa1 == squa2) {
					is.add(off);
				}
				break;
			default:
				break;
			}
			off++;
		}
		if (is.size() > 1) {
			ss.add(json.substring(0, is.get(0)));
			for (int i = 0; i < is.size() - 1; i++) {
				ss.add(json.substring(is.get(i) + 1, is.get(i + 1)));
			}
			ss.add(json.substring(is.get(is.size() - 1) + 1));
		} else if (is.size() == 1) {
			ss.add(json.substring(0, is.get(0)));
			ss.add(json.substring(is.get(0) + 1));
		} else {
			ss.add(json);
		}
		return ss;
	}

	private static List<String> sptList(String str) {
		List<String> ls = new ArrayList<>();
		int of = 0;
		int of_ = 0;
		while (of != -1) {
			int next = str.indexOf(",", of + 1);
			char c = str.charAt(next + 1);
			if (c == '{' && next != -1) {
				of_ = next;
				String s0 = str.substring(of == 0 ? 0 : (of + 1), next);
				ls.add(s0);
			} else if (next == -1) {
				String s0 = str.substring(of_ + 1);
				ls.add(s0);
			}
			of = next;
		}
		return ls;
	}

	private static Map<String, Object> toMap(String str) {
		HashMap<String, Object> m = new HashMap<>();
		String s0 = str.substring(str.indexOf("{") + 1, str.lastIndexOf("}"));
		for (String s : split(s0)) {
			m.put(s.substring(0, s.indexOf(":") - 1).replaceAll("\"", "").trim(),
					val(s.substring(s.indexOf(":") + 1).trim()));
		}
		return m;
	}

	private static List<Object> toList(String str) {
		String s0 = str.substring(str.indexOf("[") + 1, str.lastIndexOf("]"));
		List<Object> ls = new ArrayList<>();
		if (s0.startsWith("{")) {
			for (String s1 : sptList(s0)) {
				ls.add(toMap(s1));
			}
		} else if (s0.startsWith("\"")) {
			for (String s : s0.split(",")) {
				ls.add(s.replaceAll("\"", ""));
			}
		}
		return ls;
	}

	private static Object val(String str) {
		if (str.startsWith("\"")) {
			return str.replaceAll("\"", "");
		} else if (str.startsWith("{")) {
			return toMap(str);
		} else if (str.startsWith("[")) {
			return toList(str);
		}
		return str;
	}

	public static String toJson(Object o) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(String json, Class<T> cl) {
		T o = null;
		try {
			if (json.startsWith("{")) {
				Map<String, Object> map = toMap(json);
				if (cl == Map.class || cl == HashMap.class) {
					return (T) map;
				} else {
					o = cl.newInstance();
					set(map, o);
				}
			} else if (json.startsWith("[")) {
				List<Object> list = toList(json);
				return (T) list;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	private static <T> void set(Map<String, Object> m, T o) {
		try {
			for (Field f : o.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (m.get(f.getName()) == null) {
					continue;
				}
				if (f.getType() == boolean.class || f.getType() == java.lang.Boolean.class) {
					f.set(o, Boolean.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == byte.class || f.getType() == java.lang.Byte.class) {
					f.set(o, Byte.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == short.class || f.getType() == java.lang.Short.class) {
					f.set(o, Short.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == int.class || f.getType() == java.lang.Integer.class) {
					f.set(o, Integer.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == long.class || f.getType() == java.lang.Long.class) {
					f.set(o, Long.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == double.class || f.getType() == java.lang.Double.class) {
					f.set(o, Double.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == float.class || f.getType() == java.lang.Float.class) {
					f.set(o, Float.valueOf(m.get(f.getName()).toString()));
				} else if (f.getType() == java.lang.String.class) {
					f.set(o, m.get(f.getName()).toString());
				} else if (f.getType() == java.util.List.class || f.getType() == java.util.ArrayList.class) {
					Type type = f.getGenericType();
					ParameterizedType pt = (ParameterizedType) type;
					Type type2 = pt.getActualTypeArguments()[0];
					String typeName = type2.getTypeName();
					List<Object> os = (List<Object>) m.get(f.getName());
					List<Object> os_ = new ArrayList<>();
					for (Object oo : os) {
						Map<String, Object> m0 = (Map<String, Object>) oo;
						Object o0 = Class.forName(typeName).newInstance();
						set(m0, o0);
						os_.add(o0);
					}
					f.set(o, os_);
				} else if (f.getType() instanceof Object) {
					Object fo;
					fo = f.getType().newInstance();
					Map<String, Object> m0 = (Map<String, Object>) m.get(f.getName());
					set(m0, fo);
					f.set(o, fo);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
