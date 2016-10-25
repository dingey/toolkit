package com.di.toolkit;

import java.util.Date;

import com.di.toolkit.csv.CsvMapper;

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
		p.setScore(5.123);
		p.setCreate(new Date());
		System.out.println(new CsvMapper().pojoToCsvWithoutHead(p));

	}
}
