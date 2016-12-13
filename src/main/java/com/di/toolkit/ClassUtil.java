package com.di.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
}
