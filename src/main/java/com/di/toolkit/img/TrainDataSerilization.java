package com.di.toolkit.img;

import java.util.ArrayList;
import java.util.List;

import com.di.toolkit.Str;

/**
 * @author di
 */
public class TrainDataSerilization {
	public static String serilize(TrainData trainData) {
		Str s = new Str();
		for (Char c : trainData.getCs()) {
			s.add(c.getC()).add(" ").add(c.getKeyPoint()).add(" ").add(c.getWidth()).add(" ").add(c.getHeight())
					.add("[");
			for (Pixel p : c.getPixels()) {
				s.add(p.getX()).add(",").add(p.getY()).add(",").add(p.getRed()).add(",").add(p.getGreen()).add(",")
						.add(p.getBlue()).add(" ");
			}
			s.add("];");
		}
		return s.toString();
	}

	public static TrainData deSerilize(String trainData) {
		TrainData td = new TrainData();
		List<Char> cs = new ArrayList<>();
		for (String s : trainData.split(";")) {
			Char c = new Char();
			c.setC(s.substring(0, s.indexOf(" ")));
			s = s.substring(s.indexOf(" ") + 1);
			c.setKeyPoint(Integer.valueOf(s.substring(0, s.indexOf(" "))));
			s = s.substring(s.indexOf(" ") + 1);
			c.setWidth(Integer.valueOf(s.substring(0, s.indexOf(" "))));
			s = s.substring(s.indexOf(" ") + 1);
			c.setHeight(Integer.valueOf(s.substring(0, s.indexOf("["))));
			s = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
			List<Pixel> ps = new ArrayList<>();
			for (String s0 : s.split(" ")) {
				Pixel p = new Pixel();
				p.setX(Integer.valueOf(s0.split(",")[0]));
				p.setY(Integer.valueOf(s0.split(",")[1]));
				p.setRed(Integer.valueOf(s0.split(",")[2]));
				p.setGreen(Integer.valueOf(s0.split(",")[3]));
				p.setBlue(Integer.valueOf(s0.split(",")[4]));
				ps.add(p);
			}
			c.setPixels(ps);
			cs.add(c);
		}
		td.setChars(cs);
		return td;
	}

}
