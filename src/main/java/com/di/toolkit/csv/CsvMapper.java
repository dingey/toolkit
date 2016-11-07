package com.di.toolkit.csv;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.di.toolkit.ClassUtil;
import com.di.toolkit.StringUtil;
import com.di.toolkit.data.annotation.DataColumn;
import com.di.toolkit.data.annotation.DateFormat;
import com.di.toolkit.data.annotation.DecimalFormat;

/**
 * @author di:
 * @date 创建时间：2016年10月24日 下午10:46:46
 * @version
 */
public class CsvMapper {
	public static <T> String pojoToCsvWithoutHead(T t) {
		StringBuilder s = new StringBuilder();
		for (Field f : t.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(DataColumn.class)) {
				if (f.isAnnotationPresent(DateFormat.class)) {
					s.append(new SimpleDateFormat(f.getAnnotation(DateFormat.class).pattern())
							.format(ClassUtil.getFieldValueByGetMethod(f, t))).append(",");
				} else if (f.isAnnotationPresent(DecimalFormat.class)) {
					if (f.getType() == java.lang.Long.class || f.getType() == long.class) {
						s.append(new java.text.DecimalFormat(f.getAnnotation(DecimalFormat.class).pattern())
								.format(new Date((long) ClassUtil.getFieldValueByGetMethod(f, t)))).append(",");
					} else {
						s.append(new java.text.DecimalFormat(f.getAnnotation(DecimalFormat.class).pattern())
								.format(ClassUtil.getFieldValueByGetMethod(f, t))).append(",");
					}
				} else {
					s.append(ClassUtil.getFieldValueByGetMethod(f, t)).append(",");
				}
			}
		}
		return s.toString();
	}

	public static <T> String getPojoToCsvHead(T t) {
		StringBuilder s = new StringBuilder();
		for (Field f : t.getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(DataColumn.class)) {
				if (f.getAnnotation(DataColumn.class).name().isEmpty()) {
					s.append(f.getName()).append(",");
				} else {
					s.append(f.getAnnotation(DataColumn.class).name()).append(",");
				}
			}
		}
		return s.toString();
	}

	public static <T> String pojosToCsvWithoutHead(List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		StringBuilder s = new StringBuilder();
		for (T t : list) {
			s.append(pojoToCsvWithoutHead(t));
			s.append(StringUtil.lineSeparator);
		}
		return s.toString();
	}

	public static <T> String pojosToCsvWithHead(List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		StringBuilder s = new StringBuilder();
		s.append(getPojoToCsvHead(list.get(0)));
		s.append(StringUtil.lineSeparator);
		for (T t : list) {
			s.append(pojoToCsvWithoutHead(t));
			s.append(StringUtil.lineSeparator);
		}
		return s.toString();
	}
}
