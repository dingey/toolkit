package com.di.toolkit.img;

import java.awt.image.BufferedImage;

/**
 * @author di
 */
public class BufferedImageUtil {
	public static Pixel getPixel(BufferedImage img, int x, int y) {
		Object data = img.getRaster().getDataElements(x, y, null);
		Pixel p = new Pixel(x, y);
		p.setRed(img.getColorModel().getRed(data));
		p.setGreen(img.getColorModel().getGreen(data));
		p.setBlue(img.getColorModel().getBlue(data));
		return p;
	}

	public static void setColor(BufferedImage img, int x, int y, int red, int green, int blue) {
		int rgb = (red * 256 + green) * 256 + blue;
		if (rgb > 8388608) {
			rgb = rgb - 16777216;
		}
		img.setRGB(x, y, rgb);
	}

	public static BufferedImage binary(BufferedImage img) {
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				bi.setRGB(i, j, rgb);
			}
		}
		return bi;
	}

	public static BufferedImage gray(BufferedImage img) {
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int rgb = img.getRGB(i, j);
				bi.setRGB(i, j, rgb);
			}
		}
		return bi;
	}
}
