package com.di.toolkit;

import com.di.toolkit.csv.annotation.CsvColumn;
import com.di.toolkit.csv.annotation.CsvDateFormat;

/** 
* @author  di: 
* @date 创建时间：2016年10月24日 下午10:56:29 
* @version
*/
public class Person {
	@CsvDateFormat(pattern = "")
	@CsvColumn(name = "")
	int id;
	@CsvColumn(name="")
	String name;
	@CsvColumn(name="")
	int age;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
