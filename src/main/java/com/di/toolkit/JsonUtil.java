package com.di.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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
		int ang1 = 0;// {
		int ang2 = 0;// }
		int squa1 = 0;// [
		int squa2 = 0;// ]
		int quot = 0;// "
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

	public static List<String> splitList(String str) {
		int off = 0;
		int ang1 = 0;// {
		int ang2 = 0;// }
		int squa1 = 0;// [
		int squa2 = 0;// ]
		int quot = 0;// "
		List<Integer> is = new ArrayList<Integer>();
		List<String> ss = new ArrayList<String>();
		while (off < str.length()) {
			char c = str.charAt(off);
			switch (c) {
			case '"':
				if (off == 0 || str.charAt(off - 1) != '\\') {
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
				if (quot % 2 == 0 && ang1 == ang2 && squa1 == squa2 && str.charAt(off - 1) == '}'
						&& str.charAt(off + 1) == '{') {
					is.add(off);
				}
				break;
			default:
				break;
			}
			off++;
		}
		if (is.size() > 1) {
			ss.add(str.substring(0, is.get(0)));
			for (int i = 0; i < is.size() - 1; i++) {
				ss.add(str.substring(is.get(i) + 1, is.get(i + 1)));
			}
			ss.add(str.substring(is.get(is.size() - 1) + 1));
		} else if (is.size() == 1) {
			ss.add(str.substring(0, is.get(0)));
			ss.add(str.substring(is.get(0) + 1));
		} else {
			ss.add(str);
		}
		return ss;
	}

	private static Map<String, Object> toMap(String str) {
		HashMap<String, Object> m = new HashMap<>();
		String s0 = str.substring(str.indexOf("{") + 1, str.lastIndexOf("}")).trim();
		for (String s : split(s0)) {
			m.put(s.substring(0, s.indexOf(":") - 1).replaceAll("\"", "").trim(),
					val(s.substring(s.indexOf(":") + 1).trim()));
		}
		return m;
	}

	private static List<Object> toList(String str) {
		String s0 = str.substring(str.indexOf("[") + 1, str.lastIndexOf("]")).trim();
		List<Object> ls = new ArrayList<>();
		if (s0.startsWith("{")) {
			for (String s1 : splitList(s0)) {
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
		StringBuilder s = new StringBuilder("{");
		try {
			if (o.getClass() == java.util.Map.class || o.getClass() == java.util.HashMap.class) {
				Map<?, ?> m = (Map<?, ?>) o;
				for (Object key : m.keySet()) {
					Object val = m.get(key);
					if (val.getClass() == java.lang.String.class) {
						s.append("\"").append(key).append("\":\"").append(val).append("\",");
					} else if (val.getClass() == java.lang.Byte.class || val.getClass() == java.lang.Short.class
							|| val.getClass() == java.lang.Integer.class || val.getClass() == java.lang.Long.class
							|| val.getClass() == java.lang.Double.class || val.getClass() == java.lang.Float.class
							|| val.getClass() == java.lang.Boolean.class || val.getClass() == boolean.class
							|| val.getClass() == byte.class || val.getClass() == short.class
							|| val.getClass() == int.class || val.getClass() == long.class
							|| val.getClass() == double.class || val.getClass() == float.class) {
						s.append("\"").append(key).append("\":").append(val).append(",");
					} else if (val.getClass() == java.util.Date.class) {
						s.append("\"").append(key).append("\":\"")
								.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(val)).append("\",");
					} else {
						s.append("\"").append(key).append("\":").append(toJson(val)).append(",");
					}
				}
				s = new StringBuilder(s.toString().substring(0, s.length() - 1));
				s.append("}");
				return s.toString();
			}
			Field[] fs = o.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				if (f.get(o) == null)
					continue;
				if (f.getType() == byte.class || f.getType() == java.lang.Byte.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getByte(o))).append(",");
				} else if (f.getType() == short.class || f.getType() == java.lang.Short.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getShort(o))).append(",");
				} else if (f.getType() == int.class || f.getType() == java.lang.Integer.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getInt(o))).append(",");
				} else if (f.getType() == long.class || f.getType() == java.lang.Long.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getLong(o))).append(",");
				} else if (f.getType() == double.class || f.getType() == java.lang.Double.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getDouble(o))).append(",");
				} else if (f.getType() == float.class || f.getType() == java.lang.Float.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getFloat(o))).append(",");
				} else if (f.getType() == boolean.class || f.getType() == java.lang.Boolean.class) {
					s.append("\"").append(f.getName()).append("\":").append(String.valueOf(f.getBoolean(o)))
							.append(",");
				} else if (f.getType() == java.util.Date.class) {
					s.append("\"").append(f.getName()).append("\":\"")
							.append(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(f.get(o))).append("\",");
				} else if (f.getType() == java.util.Collection.class || f.getType() == java.util.List.class
						|| f.getType() == java.util.ArrayList.class) {
					List<?> os = (List<?>) f.get(o);
					StringBuilder s1 = new StringBuilder();
					for (Object object : os) {
						s1.append(toJson(object)).append(",");
					}
					s.append("\"").append(f.getName()).append("\":[")
							.append(s1.toString().substring(0, s1.length() - 1)).append("],");
				} else if (f.getType() == java.lang.String.class) {
					s.append("\"").append(f.getName()).append("\":\"").append((String) f.get(o)).append("\",");
				} else if (f.getType() instanceof Object) {
					s.append("\"").append(f.getName()).append("\":").append(toJson(f.get(o))).append(",");
				}
			}
			s = new StringBuilder(s.toString().substring(0, s.length() - 1));
			s.append("}");
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		return s.toString();
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
