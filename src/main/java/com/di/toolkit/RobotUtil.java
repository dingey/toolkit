package com.di.toolkit;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author di
 */
public class RobotUtil {
	private static Robot robot;
	static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	public static BufferedImage screenshots() {
		return getRobot().createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
	}

	public static BufferedImage screenshots(int x, int y, int width, int height) {
		return getRobot().createScreenCapture(new Rectangle(x, y, width, height));
	}

	public static void mouseMove(int x, int y) {
		getRobot().mouseMove(x, y);
	}

	public static void mouseClick() {
		getRobot().mousePress(KeyEvent.BUTTON1_DOWN_MASK);
		getRobot().mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
	}

	public static void moveClick(int x, int y) {
		mouseMove(x, y);
		mouseClick();
	}

	public static void moveDbClick(int x, int y) {
		mouseMove(x, y);
		mouseClick();
		mouseClick();
	}

	public static void moveRightClick(int x, int y) {
		mouseMove(x, y);
		getRobot().mousePress(KeyEvent.BUTTON3_DOWN_MASK);
		getRobot().mouseRelease(KeyEvent.BUTTON3_DOWN_MASK);
	}

	public static void mouseDrag(int x1, int y1, int x2, int y2) {
		mouseMove(x1, y1);
		getRobot().mousePress(KeyEvent.BUTTON1_DOWN_MASK);
		mouseMove(x2, y2);
		getRobot().mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
	}

	public static void press(int keycode) {
		getRobot().keyPress(keycode);
		getRobot().keyRelease(keycode);
	}

	public static void pressWithAlt(int keycode) {
		getRobot().keyPress(KeyEvent.VK_ALT);
		getRobot().keyPress(keycode);
		getRobot().keyRelease(keycode);
		getRobot().keyRelease(KeyEvent.VK_ALT);
	}

	public static void pressWithShift(int keycode) {
		getRobot().keyPress(KeyEvent.VK_SHIFT);
		getRobot().keyPress(keycode);
		getRobot().keyRelease(keycode);
		getRobot().keyRelease(KeyEvent.VK_SHIFT);
	}

	public static void pressWithCtrl(int keycode) {
		getRobot().keyPress(KeyEvent.VK_CONTROL);
		getRobot().keyPress(keycode);
		getRobot().keyRelease(keycode);
		getRobot().keyRelease(KeyEvent.VK_CONTROL);
	}

	public static void press(int... keycodes) {
		for (int i : keycodes) {
			press(i);
		}
	}

	public static Robot getRobot() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		return robot;
	}

}
