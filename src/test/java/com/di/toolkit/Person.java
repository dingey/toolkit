package com.di.toolkit;

import java.util.Date;
import com.di.toolkit.data.annotation.DataColumn;
import com.di.toolkit.data.annotation.DateFormat;
import com.di.toolkit.data.annotation.DecimalFormat;

/**
 * @author di:
 * @date 创建时间：2016年10月24日 下午10:56:29
 * @version
 */
public class Person {
	@DataColumn
	int id;
	@DataColumn
	String name;
	@DataColumn
	int age;
	@DataColumn
	@DecimalFormat
	double score;
	@DataColumn
	@DateFormat(pattern="yyyy年MM月dd日 hh:mm:ss")
	Date create;

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

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
