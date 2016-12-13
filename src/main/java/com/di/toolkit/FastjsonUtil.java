package com.di.toolkit;

import com.alibaba.fastjson.JSON;

/**
 * @author di
 */
public class FastjsonUtil {
	public static String toJSONString(Object o) {
		return JSON.toJSONString(o);
	}

	public static <T> T parseObject(String jsonString, Class<T> c) {
		return JSON.parseObject(jsonString, c);
	}
}
