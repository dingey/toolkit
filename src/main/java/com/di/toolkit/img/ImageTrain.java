package com.di.toolkit.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.di.toolkit.FileUtil;
import com.di.toolkit.JacksonUtil;
import com.di.toolkit.img.BufferedImageUtil;
import com.di.toolkit.img.Pixel;

/**
 * @author di
 */
public class ImageTrain {
	public static int range = 60;

	public static void train(String path) {
		File f = new File(path);
		if (!f.isDirectory() || !f.exists()) {
			throw new RuntimeException("������ļ�Ӧ��ΪĿ¼���߲�����");
		} else {
			String[] files = f.list();
			List<Char> chars = new ArrayList<Char>();
			for (String n : files) {
				String pa = f.getAbsolutePath() + File.separator + n;
				String name = n.substring(0, n.lastIndexOf("."));
				if (pa.endsWith("jpg") || pa.endsWith("bmp") || pa.endsWith("png") || pa.endsWith("tif")) {
					BufferedImage img = BufferedImageUtil.read(pa);
					Pixel background = BufferedImageUtil.getPixel(img, 0, 0);
					int count = 0, minX = 0, minY = 0, maxX = 0, maxY = 0;
					List<Pixel> ps = new ArrayList<Pixel>();
					for (int x = 0; x < img.getWidth(); x++) {
						for (int y = 0; y < img.getHeight(); y++) {
							Pixel p = BufferedImageUtil.getPixel(img, x, y);
							if (!BufferedImageUtil.isSimilar(background, p, range)) {
								count++;
								if (minX == 0 || p.getX() < minX) {
									minX = p.getX();
								}
								if (minY == 0 || p.getY() < minY) {
									minY = p.getY();
								}
								if (maxX == 0 || maxX < p.getX()) {
									maxX = p.getX();
								}
								if (maxY == 0 || maxY < p.getY()) {
									maxY = p.getY();
								}
								ps.add(p);
							}
						}
					}
					List<Pixel> temps = new ArrayList<Pixel>();
					for (Pixel p : ps) {
						p.setX(p.getX() - minX);
						p.setY(p.getY() - minY);
						temps.add(p);
					}
					Char c = new Char();
					c.setHeight((maxY - minY));
					c.setWidth((maxX - minX));
					c.setC(name);
					c.setKeyPoint(count);
					c.setPixels(temps);
					chars.add(c);
					System.out.println(name + " -> " + pa + " " + count);
				}
			}
			TrainData td = new TrainData(chars);
			FileUtil.writeToFile(path + "train.data", JacksonUtil.pojoToJson(td));
		}
	}

}
