package com.di.toolkit.img;

import java.util.List;

/**
 * @author di
 */
public class TrainData {
	List<Char> cs;
	double version = 1.0;

	public TrainData() {
	}

	public TrainData(List<Char> chars) {
		super();
		this.cs = chars;
	}

	public List<Char> getCs() {
		return cs;
	}

	public void setChars(List<Char> chars) {
		this.cs = chars;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

}
