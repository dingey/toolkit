package com.di.toolkit;

import org.junit.Test;

import com.di.toolkit.img.ImageRecognition;
import com.di.toolkit.img.ImageTrain;

/**
 * @author di
 */
public class RecognitionTest {
	@Test
	public void test() {
		// train();
		reconition();
	}

	public void train() {
		ImageTrain.train("D:/ocr/train2/");
	}

	public void reconition() {
		// System.out.println(ImageRecognition.parse("D:/ocr/train3/train.data",
		// "D:/ocr/train3/a1.jpg"));
		System.out.println(ImageRecognition.parse("D:/ocr/train3/train.data", "D:/ocr/train3/a.jpg", 3, null));
	}
}
