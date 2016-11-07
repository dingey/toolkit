package com.di.toolkit;

import java.util.List;

import com.di.toolkit.csv.CsvMapper;

/**
 * @author di
 */
public class CsvUtil {
	public <T> String pojoToCsvWithoutHead(T t) {
		return CsvMapper.pojoToCsvWithoutHead(t);
	}

	public <T> String pojosToCsvWithoutHead(List<T> list) {
		return CsvMapper.pojosToCsvWithoutHead(list);
	}

	public static <T> String pojosToCsvWithHead(List<T> list) {
		return CsvMapper.pojosToCsvWithHead(list);
	}
}
