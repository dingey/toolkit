package com.di.toolkit.img;

import java.util.List;

import com.di.toolkit.img.Pixel;

/**
 * @author di
 */
public class Char {
	private String c;
	private int keyPoint;
	private int width;
	private int height;
	private List<Pixel> pixels;

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getKeyPoint() {
		return keyPoint;
	}

	public void setKeyPoint(int keyPoint) {
		this.keyPoint = keyPoint;
	}

	public List<Pixel> getPixels() {
		return pixels;
	}

	public void setPixels(List<Pixel> pixels) {
		this.pixels = pixels;
	}

}
