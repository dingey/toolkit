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
	private static String[] spt(String str) {
		List<String> ls = new ArrayList<>();
		int of = 0;
		int of_ = 0;
		while (of != -1) {
			int next = str.indexOf(",", of + 1);
			char c = str.charAt(next + 1);
			if (c == '"' && next != -1) {
				of_ = next;
				String s0 = str.substring(of == 0 ? 0 : (of + 1), next);
				ls.add(s0);
			} else if (next == -1) {
				String s0 = str.substring(of_ + 1);
				ls.add(s0);
			}
			of = next;
		}
		String[] l = new String[ls.size()];
		for (int i = 0; i < l.length; i++) {
			l[i] = ls.get(i);
		}
		return l;
	}

	private static String[] sptList(String str) {
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
		String[] l = new String[ls.size()];
		for (int i = 0; i < l.length; i++) {
			l[i] = ls.get(i);
		}
		return l;
	}

	private static Map<String, Object> toMap(String str) {
		HashMap<String, Object> m = new HashMap<>();
		String s0 = str.substring(str.indexOf("{") + 1, str.lastIndexOf("}"));
		for (String s : spt(s0)) {
			m.put(s.substring(0, s.indexOf(":") - 1).replaceAll("\"", "").trim(),
					val(s.substring(s.indexOf(":") + 1).trim()));
		}
		return m;
	}

	private static List<Object> toList(String str) {
		String s0 = str.substring(str.indexOf("[") + 1, str.lastIndexOf("]") - 1);
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
				if (f.getType() == byte.class) {
					f.set(o, (byte) m.get(f.getName()));
				} else if (f.getType() == short.class) {
					f.set(o, (short) m.get(f.getName()));
				} else if (f.getType() == int.class) {
					f.set(o, (int) m.get(f.getName()));
				} else if (f.getType() == long.class) {
					f.set(o, (long) m.get(f.getName()));
				} else if (f.getType() == java.lang.String.class) {
					f.set(o, m.get(f.getName()));
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
