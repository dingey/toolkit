package com.di.toolkit;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author di
 */
public class HttpURLConnectionUtil {
	public static String post(String requestUrl, Map<String, Object> params) {
		String res = null;
		String BOUNDARY = "--boundary--";
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// conn.setRequestMethod("get");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			StringBuffer sb = new StringBuffer();
			if (params != null) {
				for (String key : params.keySet()) {
					Object v = params.get(key);
					if (v == null) {
						continue;
					}
					sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					if (v.getClass() == java.io.File.class) {
						File file = (File) v;
						String fileName = file.getName();
						String contentType = ContentTypeEnum.getMimeByFileExt(fileName);
						sb.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName
								+ "\"\r\n");
						sb.append("Content-Type:" + contentType + "\r\n\r\n");
						DataInputStream in = new DataInputStream(new FileInputStream(file));
						byte[] bufferOut = new byte[1024];
						while (in.read(bufferOut) != -1) {
							sb.append(new String(bufferOut));
						}
						in.close();
					} else {
						sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n")
								.append(String.valueOf(v));
						// out.write(sb.toString().getBytes());
					}
				}
			}
			sb.append("\r\n" + BOUNDARY + "\r\n");
			conn.setRequestProperty("Content-Length", sb.toString().getBytes().length + "");
			OutputStream out = conn.getOutputStream();
			out.write(sb.toString().getBytes());
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
