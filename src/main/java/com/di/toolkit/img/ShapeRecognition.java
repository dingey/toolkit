package com.di.toolkit.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.di.toolkit.FileUtil;
import com.di.toolkit.Str;

/**
 * @author di
 */
public class ShapeRecognition {
	public static int range = 60;
	private static List<SimpleShape> shapes;
	static {

	}

	public static void train(String path) {
		File f = new File(path);
		if (!f.isDirectory() || !f.exists()) {
			throw new RuntimeException("");
		} else {
			String[] files = f.list();
			List<SimpleShape> shapes = new ArrayList<SimpleShape>();
			for (String n : files) {
				String pa = f.getAbsolutePath() + File.separator + n;
				if (new File(pa).isDirectory()) {
					continue;
				}
				String name = n.substring(0, n.lastIndexOf("."));
				if (pa.endsWith("jpg") || pa.endsWith("bmp") || pa.endsWith("png") || pa.endsWith("tif")) {
					BufferedImage img = BufferedImageUtil.read(pa);
					Pixel background = BufferedImageUtil.getPixel(img, 0, 0);
					int count = 0, minX = 0, minY = 0, maxX = 0, maxY = 0;
					List<SimplePoint> ps = new ArrayList<SimplePoint>();
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
								SimplePoint sp = new SimplePoint();
								sp.setX(x);
								sp.setY(y);
								ps.add(sp);
							}
						}
					}
					List<SimplePoint> temps = new ArrayList<SimplePoint>();
					for (SimplePoint p : ps) {
						p.setX(p.getX() - minX);
						p.setY(p.getY() - minY);
						temps.add(p);
					}
					SimpleShape ss = new SimpleShape();
					ss.setHeight((maxY - minY));
					ss.setWidth((maxX - minX));
					ss.setName(name);
					ss.setCount(count);
					ss.setPoints(temps);
					shapes.add(ss);
					System.out.println(name + " -> " + pa + " " + count);
				}
			}
			FileUtil.writeToFile(path + "train.sd", serilize(shapes));
		}
	}

	public static String parseBest(String dataPath, BufferedImage img) {
		String content = FileUtil.readAsString(dataPath, "GBK");
		List<SimpleShape> td = deserilize(content);
		HashMap<String, Double> map = new HashMap<>();
		Pixel background = BufferedImageUtil.getPixel(img, 0, 0);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Pixel p = BufferedImageUtil.getPixel(img, x, y);
				if (!BufferedImageUtil.isSimilar(p, background, ImageTrain.range)) {
					for (SimpleShape c : td) {
						SimplePoint p0 = c.getPoints().get(0);
						SimplePoint p1 = c.getPoints().get(1);
						Pixel $p = BufferedImageUtil.getPixel(img, x + (p1.getX() - p0.getX()),
								y + (p1.getY() - p0.getY()));
						if (!BufferedImageUtil.isSimilar(p, $p, range)) {
							int count = 1;
							for (int i = 1; i < c.getPoints().size(); i++) {
								SimplePoint pi = c.getPoints().get(i);
								int x_ = pi.getX() - p0.getX();
								int y_ = pi.getY() - p0.getY();
								if ((x + x_) < img.getWidth() && (y + y_) < img.getHeight()) {
									Pixel p_ = BufferedImageUtil.getPixel(img, x + x_, y + y_);
									if (p_ != null && !BufferedImageUtil.isSimilar(background, p_, range)) {
										count++;
									}
								}
								double d = (double) count / (double) c.getCount();
								if (map.get(c.getName()) == null
										|| (map.get(c.getName()) != null && map.get(c.getName()) < d)) {
									map.put(c.getName(), d);
								}
							}
						}
					}
				}
			}
		}
		double max = 0;
		String key = " ";
		for (String s : map.keySet()) {
			if (map.get(s) > max) {
				max = map.get(s);
				key = s;
			}
		}
		if (key.indexOf("L") != -1 && map.get("U") != null && map.get("U") > 0.9) {
			if (map.get("D") != null && map.get("D") > 0.9) {
				return "D";
			} else if (map.get("U") != null && map.get("U") > 0.9) {
				return "U";
			}
		} else {
			System.out.println("best match is " + key + " ratio is " + max * 100 + "%");
		}
		return key.substring(0, 1);
	}

	private static String serilize(List<SimpleShape> sh) {
		Str s = new Str();
		for (SimpleShape ss : shapes) {
			s.add(ss.getName()).add(" ").add(ss.getWidth()).add(" ").add(ss.getHeight()).add(" ").add(ss.getCount())
					.add(":");
			for (SimplePoint p : ss.getPoints()) {
				s.add(p.getX()).add(",").add(p.getY()).add(" ");
			}
			s.add(";");
		}
		return s.toString();
	}

	private static List<SimpleShape> deserilize(String res) {
		List<SimpleShape> tmps = new ArrayList<>();
		for (String s : res.split(";")) {
			s = s.trim();
			if (!s.isEmpty()) {
				SimpleShape ss = new SimpleShape();
				String s0 = s.split(":")[0];
				ss.setName(s0.split(" ")[0]);
				ss.setWidth(Integer.valueOf(s0.split(" ")[1]));
				ss.setHeight(Integer.valueOf(s0.split(" ")[2]));
				ss.setCount(Integer.valueOf(s0.split(" ")[3]));
				String s1 = s.split(":")[1];
				List<SimplePoint> sps = new ArrayList<>();
				for (String s_ : s1.split(" ")) {
					SimplePoint sp = new SimplePoint();
					sp.setX(Integer.valueOf(s_.split(",")[0]));
					sp.setY(Integer.valueOf(s_.split(",")[1]));
					sps.add(sp);
				}
				ss.setPoints(sps);
				tmps.add(ss);
			}
		}
		return tmps;
	}

	static class SimplePoint {
		private int x;
		private int y;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

	}

	static class SimpleShape {
		private String name;
		private int width;
		private int height;
		private int count;
		List<SimplePoint> points;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public List<SimplePoint> getPoints() {
			return points;
		}

		public void setPoints(List<SimplePoint> points) {
			this.points = points;
		}

	}
}
