package com.di.toolkit;

/**
 * @author di:
 * @date 创建时间：2016年10月24日 下午11:00:50
 * @version
 */
public class CsvTest {
	public static void main(String[] args) {
		Person p = new Person();
		p.setId(1);
		p.setName("Alice");
		System.out.println(ClassUtil.getFieldValue("name", p));
		System.out.println(ClassUtil.getFieldValueByGetMethod("name", p));
	}
}
