package com.di.toolkit.img;

/**
 * @author di
 */
public class RgbUtil {
	public static int[] srgbToRgb(int srgb) {
		int[] rgb = new int[3];
		rgb[0] = (srgb & 0xff0000) >> 16;
		rgb[1] = (srgb & 0xff00) >> 8;
		rgb[2] = (srgb & 0xff);
		return rgb;
	}
}
