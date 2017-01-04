package com.di.toolkit;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.di.toolkit.img.ImageFind;

/**
 * @author di
 */
public class ImageUtil {
	public static String IMAGE_TYPE_GIF = "gif";// Graphics Interchange Format
	public static String IMAGE_TYPE_JPG = "jpg";// Joint Photographic Experts
												// Group
	public static String IMAGE_TYPE_JPEG = "jpeg";// Joint Photographic Experts
													// Group
	public static String IMAGE_TYPE_BMP = "bmp";// English Bitmap (bitmap)
												// shorthand, it is the Windows
												// operating system standard
												// image file format
	public static String IMAGE_TYPE_PNG = "png";// Portable Network Graphics
	public static String IMAGE_TYPE_PSD = "psd";// Photoshop

	/**
	 * save BufferedImage
	 * 
	 * @param image
	 *            BufferedImage
	 * @param fileType
	 *            image type
	 * @param path
	 *            file path
	 */
	public static void saveBufferedImage(BufferedImage image, String fileType, String path) {
		try {
			ImageIO.write(image, fileType, new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Color to black and white
	 * 
	 * @param src
	 *            BufferedImage
	 */
	public static BufferedImage gray(BufferedImage src) {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(cs, null);
		return op.filter(src, null);
	}

	public static boolean contains(BufferedImage large, BufferedImage small) {
		ImageFind ifd = new ImageFind(large, small);
		return ifd.contains();
	}
}
