package com.di.toolkit.img;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * find screen picture location.
 */
public class ImageFind {

	BufferedImage screenShotImage;
	BufferedImage keyImage; // Find the destination image

	int scrShotImgWidth; // Screen Shot Width
	int scrShotImgHeight; // Screenshot height

	int keyImgWidth; // Find the width of the destination image
	int keyImgHeight; // Find the height of the target image

	int[][] screenShotImageRGBData; // Screenshots RGB data
	int[][] keyImageRGBData; // Find the destination picture RGB data

	int[][][] findImgData; // Find the result, the target icon is located on the
							// screen shot of the coordinate data

	public ImageFind() {
	}

	public ImageFind(String keyImagePath) {
		screenShotImage = this.getFullScreenShot();
		keyImage = this.getBfImageFromPath(keyImagePath);
		screenShotImageRGBData = this.getImageGRB(screenShotImage);
		keyImageRGBData = this.getImageGRB(keyImage);
		scrShotImgWidth = screenShotImage.getWidth();
		scrShotImgHeight = screenShotImage.getHeight();
		keyImgWidth = keyImage.getWidth();
		keyImgHeight = keyImage.getHeight();
		this.findImage();
	}

	public ImageFind(BufferedImage screenShotImage, BufferedImage keyImage) {
		this.keyImage = keyImage;
		this.screenShotImage = screenShotImage;
		screenShotImageRGBData = this.getImageGRB(screenShotImage);
		keyImageRGBData = this.getImageGRB(keyImage);
		scrShotImgWidth = screenShotImage.getWidth();
		scrShotImgHeight = screenShotImage.getHeight();
		keyImgWidth = keyImage.getWidth();
		keyImgHeight = keyImage.getHeight();
		this.findImage();
	}

	/**
	 * Full Screen Capture
	 * 
	 * @return Returns the BufferedImage
	 */
	public BufferedImage getFullScreenShot() {
		BufferedImage bfImage = null;
		int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		try {
			Robot robot = new Robot();
			bfImage = robot.createScreenCapture(new Rectangle(0, 0, width, height));
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return bfImage;
	}

	/**
	 * Reads the target image from the local file
	 * 
	 * @param keyImagePath
	 *            - Picture Absolute path
	 * @return The BufferedImage object for the local image
	 */
	public BufferedImage getBfImageFromPath(String keyImagePath) {
		BufferedImage bfImage = null;
		try {
			bfImage = ImageIO.read(new File(keyImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bfImage;
	}

	/**
	 * Gets the RGB array of pictures according to BufferedImage
	 * 
	 * @param bfImage
	 *            BufferedImage
	 * @return
	 */
	public int[][] getImageGRB(BufferedImage bfImage) {
		int width = bfImage.getWidth();
		int height = bfImage.getHeight();
		int[][] result = new int[height][width];
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				// use getRGB (w,
				// h) Get the color value of the point is ARGB, and in practical
				// use of the RGB, so need to be converted into RGB RGB, that
				// bufImg.getRGB (w,
				// h) & 0xFFFFFF.
				result[h][w] = bfImage.getRGB(w, h) & 0xFFFFFF;
			}
		}
		return result;
	}

	/**
	 * Find pictures
	 */
	public void findImage() {
		findImgData = new int[keyImgHeight][keyImgWidth][2];
		// Traverse the screenshot pixel data
		for (int y = 0; y < scrShotImgHeight - keyImgHeight; y++) {
			for (int x = 0; x < scrShotImgWidth - keyImgWidth; x++) {
				// According to the size of the target graph, the four corners
				// of the target map are mapped to the four points on the
				// screenshot to determine whether the corresponding four points
				// in the screenshot are the same as those in the four corners
				// of the graph B. If the screenshots are the same, All points
				// in the upper mapping range are compared with all points in
				// the target graph.
				if ((keyImageRGBData[0][0] ^ screenShotImageRGBData[y][x]) == 0
						&& (keyImageRGBData[0][keyImgWidth - 1] ^ screenShotImageRGBData[y][x + keyImgWidth - 1]) == 0
						&& (keyImageRGBData[keyImgHeight - 1][keyImgWidth - 1]
								^ screenShotImageRGBData[y + keyImgHeight - 1][x + keyImgWidth - 1]) == 0
						&& (keyImageRGBData[keyImgHeight - 1][0]
								^ screenShotImageRGBData[y + keyImgHeight - 1][x]) == 0) {

					boolean isFinded = isMatchAll(y, x);
					// If the comparison results are identical, the image is
					// found, and the position coordinate data that is found is
					// filled into the array of lookup results.
					if (isFinded) {
						for (int h = 0; h < keyImgHeight; h++) {
							for (int w = 0; w < keyImgWidth; w++) {
								findImgData[h][w][0] = y + h;
								findImgData[h][w][1] = x + w;
							}
						}
						return;
					}
				}
			}
		}
	}

	/**
	 * Determine whether all the points within the target map map on the screen
	 * shot correspond to the dots.
	 * 
	 * @param y
	 *            - The y coordinate of the screen shot that matches the pixel
	 *            point in the top-left corner of the target graph
	 * @param x
	 *            - The x-coordinate of the screen shot that matches the pixel
	 *            in the top-left corner of the destination graph
	 * @return
	 */
	public boolean isMatchAll(int y, int x) {
		int biggerY = 0;
		int biggerX = 0;
		int xor = 0;
		for (int smallerY = 0; smallerY < keyImgHeight; smallerY++) {
			biggerY = y + smallerY;
			for (int smallerX = 0; smallerX < keyImgWidth; smallerX++) {
				biggerX = x + smallerX;
				if (biggerY >= scrShotImgHeight || biggerX >= scrShotImgWidth) {
					return false;
				}
				xor = keyImageRGBData[smallerY][smallerX] ^ screenShotImageRGBData[biggerY][biggerX];
				if (xor != 0) {
					return false;
				}
			}
			biggerX = x;
		}
		return true;
	}

	/**
	 * Outputs the coordinate data that is found
	 */
	public void printFindData() {
		// for (int y = 0; y < keyImgHeight; y++) {
		// for (int x = 0; x < keyImgWidth; x++) {
		// System.out.print("(" + this.findImgData[y][x][0] + ", " +
		// this.findImgData[y][x][1] + ")");
		// }
		// System.out.println("");
		// }
		System.out.println("(" + this.findImgData[0][0][1] + "," + this.findImgData[0][0][0] + ")->("
				+ this.findImgData[keyImgHeight - 1][keyImgWidth - 1][1] + ","
				+ this.findImgData[keyImgHeight - 1][keyImgWidth - 1][0] + ")");
	}

	public boolean contains() {
		if (this.findImgData[keyImgHeight - 1][keyImgWidth - 1][1] > this.findImgData[0][0][1]
				&& this.findImgData[keyImgHeight - 1][keyImgWidth - 1][0] > this.findImgData[0][0][0]) {
			return true;
		}
		return false;
	}
}