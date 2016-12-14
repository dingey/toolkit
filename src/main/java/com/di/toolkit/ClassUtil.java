package com.di.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author di
 */
public class ClassUtil {
	public static <T> Object invokeMethod(Method m, T o, Object... args) {
		try {
			return m.invoke(o, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getBeanFieldsMap(Object o) {
		Map<String, Object> m = new HashMap<>();
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				m.put(f.getName(), f.get(o));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return m;
	}

	public static <T> Object getFieldValue(Field f, T t) {
		try {
			f.setAccessible(true);
			return f.get(t);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> Object getFieldValue(String fieldName, T t) {
		try {
			Field f = t.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(t);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> Object getFieldValueByGetMethod(Field f, T t) {
		Method m = null;
		try {
			try {
				m = t.getClass().getDeclaredMethod("get" + StringUtil.firstCharUpper(f.getName()));
			} catch (NoSuchMethodException e) {
				try {
					m = t.getClass().getDeclaredMethod("get" + f.getName());
				} catch (NoSuchMethodException e1) {
					try {
						m = t.getClass().getDeclaredMethod("get"
								+ StringUtil.firstCharUpper(StringUtil.trimUnderlinedFirstCharUpper(f.getName())));
					} catch (NoSuchMethodException e2) {
						try {
							m = t.getClass().getDeclaredMethod(f.getName());
						} catch (NoSuchMethodException e3) {
							e3.printStackTrace();
						}
					}
				}
			}
			if (m != null) {
				return m.invoke(t);
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> Object getFieldValueByGetMethod(String fieldName, T t) {
		try {
			return getFieldValueByGetMethod(t.getClass().getDeclaredField(fieldName), t);
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> void setObjectFieldsValue(Map<String, Object> m, T o) {
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
				} else if (f.getType() == char.class || f.getType() == java.lang.Character.class) {
					f.set(o, m.get(f.getName()));
				} else if (f.getType() == java.lang.String.class) {
					f.set(o, m.get(f.getName()).toString());
				} else if (f.getType() == java.util.Date.class) {
					if (m.get(f.getName()).getClass() == java.lang.String.class) {
						try {
							f.set(o, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(m.get(f.getName()).toString()));
						} catch (ParseException e) {
							try {
								f.set(o, new SimpleDateFormat("yyyy-MM-dd").parse(m.get(f.getName()).toString()));
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							e.printStackTrace();
						}
					} else {
						f.set(o, m.get(f.getName()));
					}
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
						setObjectFieldsValue(m0, o0);
						os_.add(o0);
					}
					f.set(o, os_);
				} else if (f.getType() instanceof Object) {
					Object fo;
					fo = f.getType().newInstance();
					Map<String, Object> m0 = (Map<String, Object>) m.get(f.getName());
					setObjectFieldsValue(m0, fo);
					f.set(o, fo);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
