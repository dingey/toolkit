package com.di.toolkit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.di.toolkit.csv.CsvMapper;

/**
 * @author di:
 * @date 创建时间：2016年10月24日 下午11:00:50
 * @version
 */
public class CsvTest {
	public static void main(String[] args) {
		List<Person> ps=new ArrayList<>();
		Person p = new Person();
		p.setId(1);
		p.setName("Alice");
		p.setScore(5.123);
		p.setCreate(new Date());
		ps.add(p);
		ps.add(p);
		System.out.println(new CsvMapper().pojosToCsvWithHead(ps));

	}
}
