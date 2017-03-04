package com.di.toolkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import org.junit.Test;

import com.di.toolkit.img.TwoColorCompare;
import com.di.toolkit.img.TwoColorCompare.Result;

/**
 * @author di
 */
public class TwoColorCompareTest {
	@Test
	public void test() {
//		t1();
//		t2();
	}

	public void t1() {
		long s0 = new Date().getTime();
		try {
			BufferedImage img = ImageIO.read(new File("C:/Users/Administrator/Desktop/hp.png"));
			Result r = TwoColorCompare.compare(img, 6);
			System.out.println(r.getLeftPercent());
			System.out.println(r.getRightPercent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		long s1 = new Date().getTime();
		System.out.println("compare : " + (s1 - s0));
	}

	public void t2() {
		long s0 = new Date().getTime();
		try {
			BufferedImage img = ImageIO.read(new File("C:/Users/Administrator/Desktop/hp.png"));
			Result r = TwoColorCompare.compare_(img, 6);
			System.out.println(r.getLeftPercent());
			System.out.println(r.getRightPercent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		long s1 = new Date().getTime();
		System.out.println("compare_ : " + (s1 - s0));
	}
}
