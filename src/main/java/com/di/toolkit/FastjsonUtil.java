package com.di.toolkit;

import com.alibaba.fastjson.JSON;

/**
 * @author di:
 * @date 创建时间：2016年10月23日 下午4:38:14
 * @version
 */
public class FastjsonUtil {
	public static String toJSONString(Object o) {
		return JSON.toJSONString(o);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object parseObject(String jsonString, Class c) {
		return JSON.parseObject(jsonString, c);
	}
}
