package com.di.toolkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author di
 */
public class FileUtil {
	public void writeToFile(String path, String content) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
		}
	}
}
