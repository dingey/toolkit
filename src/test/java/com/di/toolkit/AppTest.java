package com.di.toolkit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) {
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		System.out.println(path);
		path = ClassesUtil.class.getClassLoader().getResource("").getPath();
		System.out.println(path);
		path = path.replaceFirst("test-classes", "classes");
		System.out.println(path);
	}
}
