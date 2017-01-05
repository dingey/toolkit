package com.di.toolkit.img;

import java.awt.image.BufferedImage;

/**
 * @author di
 */
public class TwoColorCompare {

	public static Result compare(BufferedImage img, int range) {
		Result r = new Result();
		r.setWidth(img.getWidth());
		int y = img.getHeight() / 2;
		int count = 0;
		for (int i = 0; i < r.getWidth(); i++) {
			int[] a = RgbUtil.srgbToRgb(img.getRGB(i, y));
			int[] b = RgbUtil.srgbToRgb(img.getRGB(0, y));
			if (Math.abs(a[0] - b[0]) < range && Math.abs(a[1] - b[1]) < range && Math.abs(a[2] - b[2]) < range) {
				count++;
			}
		}
		r.setLeft(count);
		r.setRight(r.getWidth() - count);
		r.setLeftPercent((float) count / (float) r.getWidth());
		r.setRightPercent(1 - r.getLeftPercent());
		return r;
	}

	public static Result compare(BufferedImage img) {
		return compare(img, 5);
	}

	public static class Result {
		int width;
		int left;
		int right;
		float leftPercent;
		float rightPercent;

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getLeft() {
			return left;
		}

		public void setLeft(int left) {
			this.left = left;
		}

		public int getRight() {
			return right;
		}

		public void setRight(int right) {
			this.right = right;
		}

		public float getLeftPercent() {
			return leftPercent;
		}

		public void setLeftPercent(float leftPercent) {
			this.leftPercent = leftPercent;
		}

		public float getRightPercent() {
			return rightPercent;
		}

		public void setRightPercent(float rightPercent) {
			this.rightPercent = rightPercent;
		}

	}
}
