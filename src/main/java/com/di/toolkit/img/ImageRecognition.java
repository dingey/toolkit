package com.di.toolkit.img;

import java.awt.image.BufferedImage;

import com.di.toolkit.FileUtil;
import com.di.toolkit.JacksonUtil;
import com.di.toolkit.Str;
import com.di.toolkit.img.BufferedImageUtil;
import com.di.toolkit.img.Pixel;

/**
 * @author di
 */
public class ImageRecognition {
	private static double RATIO = 0.95;

	public static String parse(String dataPath, String targetPath, Double ratio) {
		String content = FileUtil.readAsString(dataPath, "GBK");
		TrainData td = JacksonUtil.jsonToPojo(content, TrainData.class);
		BufferedImage img = BufferedImageUtil.read(targetPath);
		Pixel background = BufferedImageUtil.getPixel(img, 0, 0);
		if (ratio == null)
			ratio = RATIO;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Pixel p = BufferedImageUtil.getPixel(img, x, y);
				if (!BufferedImageUtil.isSimilar(p, background, ImageTrain.range)) {
					for (Char c : td.getCs()) {
						Pixel p0 = c.getPixels().get(0);
						if (BufferedImageUtil.isSimilar(p, p0, ImageTrain.range)) {
							int count = 1;
							for (int i = 1; i < c.getPixels().size(); i++) {
								Pixel pi = c.getPixels().get(i);
								int x_ = pi.getX() - p0.getX();
								int y_ = pi.getY() - p0.getY();
								if ((x + x_) < img.getWidth() && (y + y_) < img.getHeight()) {
									Pixel p_ = BufferedImageUtil.getPixel(img, x + x_, y + y_);
									if (p_ != null && !BufferedImageUtil.isSimilar(background, p_, ImageTrain.range)) {
										count++;
									}
								}
							}
							double d = (double) count / (double) c.getKeyPoint();
							if (d > ratio) {
								System.out.println(c.getC() + " : " + count + " / " + c.getKeyPoint() + " = "
										+ (double) count / (double) c.getKeyPoint());
								return c.getC();
							}
						}
					}
				}
			}
		}
		return "";
	}

	public static String parse(String dataPath, String targetPath, int num, Double ratio) {
		String content = FileUtil.readAsString(dataPath, "GBK");
		TrainData td = JacksonUtil.jsonToPojo(content, TrainData.class);
		BufferedImage img = BufferedImageUtil.read(targetPath);
		Pixel background = BufferedImageUtil.getPixel(img, 0, 0);
		Str s = new Str();
		if (ratio == null)
			ratio = RATIO;
		int count0 = 0;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Pixel p = BufferedImageUtil.getPixel(img, x, y);
				if (!BufferedImageUtil.isSimilar(p, background, ImageTrain.range)) {
					for (Char c : td.getCs()) {
						Pixel p0 = c.getPixels().get(0);
						if (BufferedImageUtil.isSimilar(p, p0, ImageTrain.range)) {
							int count = 1;
							for (int i = 1; i < c.getPixels().size(); i++) {
								Pixel pi = c.getPixels().get(i);
								int x_ = pi.getX() - p0.getX();
								int y_ = pi.getY() - p0.getY();
								if ((x + x_) < img.getWidth() && (y + y_) < img.getHeight()) {
									Pixel p_ = BufferedImageUtil.getPixel(img, x + x_, y + y_);
									if (p_ != null && !BufferedImageUtil.isSimilar(background, p_, ImageTrain.range)) {
										count++;
									}
								}
							}
							double d = (double) count / (double) c.getKeyPoint();
							if (d > ratio) {
								System.out.println(c.getC() + " : " + count + " / " + c.getKeyPoint() + " = "
										+ (double) count / (double) c.getKeyPoint());
								s.add(c.getC());
								count0++;
							}
							if (count0 == num) {
								return s.toString();
							}
						}
					}
				}
			}
		}
		return s.toString();
	}
}
