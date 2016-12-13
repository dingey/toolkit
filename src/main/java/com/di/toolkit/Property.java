package com.di.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author di
 */
public class Property {
	private HashMap<String, String> props;
	private String fileName;

	Property(String fileName) {
		if (fileName.indexOf(".") == -1) {
			fileName = fileName + ".properties";
		}
		this.init(fileName);
	}

	public void init(String fileName) {
		Properties prop = new Properties();
		String path = "";
		try {
			path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		path = path + fileName;

		try {
			prop.load(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			System.err.println(path + " not found");
			try {
				if (path.indexOf("test-classes") != -1) {
					path = path.replaceFirst("test-classes", "classes");
				}
				prop.load(new FileInputStream(new File(path)));
			} catch (IOException e1) {
				System.err.println(path + " not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		props = new HashMap<>();
		for (Object key : prop.keySet()) {
			props.put((String) key, prop.getProperty((String) key));
		}
	}

	public String get(String propKey) {
		return props.get(propKey);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
