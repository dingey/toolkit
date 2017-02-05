package com.di.toolkit.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

/**
 * @author di
 */
public class BufferedImageUtil {
	public static final int BLACK_RGB = -16777216;
	public static final int WHITE_RGB = -1;

	public static Pixel getPixel(BufferedImage img, int x, int y) {
		try {
			Object data = img.getRaster().getDataElements(x, y, null);
			Pixel p = new Pixel(x, y);
			p.setRed(img.getColorModel().getRed(data));
			p.setGreen(img.getColorModel().getGreen(data));
			p.setBlue(img.getColorModel().getBlue(data));
			return p;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
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

	public static BufferedImage read(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void save(BufferedImage img, String path) {
		try {
			ImageIO.write(img, path.substring(path.lastIndexOf(".") + 1), new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage binary(BufferedImage img, int range) {
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Pixel p = getPixel(img, i, j);
				if (((255 - p.getRed()) <= range) && ((255 - p.getGreen()) <= range)
						&& ((255 - p.getBlue()) <= range)) {
					bi.setRGB(i, j, WHITE_RGB);
				} else {
					bi.setRGB(i, j, BLACK_RGB);
				}
			}
		}
		return bi;
	}

	public static BufferedImage readByRelativePath(String relativePath) {
		try {
			String pathname = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
			return ImageIO.read(new File(pathname + relativePath));
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isSimilar(Pixel p1, Pixel p2, int range) {
		if (Math.abs(p1.getRed() - p2.getRed()) < range && Math.abs(p1.getRed() - p2.getRed()) < range
				&& Math.abs(p1.getRed() - p2.getRed()) < range) {
			return true;
		}
		return false;
	}
}
