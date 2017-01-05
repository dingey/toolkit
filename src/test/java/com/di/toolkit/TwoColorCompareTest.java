package com.di.toolkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
		try {
			BufferedImage img = ImageIO.read(new File("C:/Users/Administrator/Desktop/hp.png"));
			Result r = TwoColorCompare.compare(img,6);
			System.out.println(r.getLeftPercent());
			System.out.println(r.getRightPercent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
