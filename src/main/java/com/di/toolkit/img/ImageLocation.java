package com.di.toolkit.img;

import java.awt.image.BufferedImage;

/**
 * @author di
 */
public class ImageLocation {
	public static int[] locate(String smallpath, String bigpath) {
		return locate(BufferedImageUtil.read(smallpath), BufferedImageUtil.read(bigpath));
	}

	public static int[] locate(BufferedImage small, BufferedImage big) {
		int[] res = new int[4];
		for (int x = 0; x < big.getWidth(); x++) {
			for (int y = 0; y < big.getHeight(); y++) {
				int count = 0, count0 = 0;
				for (int x0 = 0; x0 < small.getWidth(); x0++) {
					for (int y0 = 0; y0 < small.getHeight(); y0++) {
						if ((x + x0) < big.getWidth() && (y + y0) < big.getHeight()) {
							Pixel p0 = BufferedImageUtil.getPixel(small, x0, y0);
							Pixel p1 = BufferedImageUtil.getPixel(big, x + x0, y + y0);
							count++;
							if (BufferedImageUtil.isSimilar(p1, p0, 20)) {
								count0++;
							}
						}
					}
				}
				if (100 * count0 / count > 98) {
					res[0] = x;
					res[1] = y;
					res[2] = x + small.getWidth();
					res[3] = y + small.getHeight();
					return res;
				}
			}
		}
		return res;
	}
}
