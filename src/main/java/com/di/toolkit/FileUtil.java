package com.di.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author di
 */
public class FileUtil {
	public static void writeToFile(String path, String content) {
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

	public static void save(File f, String nameAndPath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(f);
			out = new FileOutputStream(nameAndPath);
			byte[] bytes = new byte[1024];
			while (in.read(bytes) != -1) {
				out.write(bytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String readAsString(String path, String format) {
		String str = "";
		try {
			FileInputStream in = new FileInputStream(path);
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			str = new String(buffer, format == null ? "utf-8" : format);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
}
