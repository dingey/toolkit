package com.di.toolkit;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author di
 */
public class HttpURLConnectionUtil {
	public static String postMultipart(String requestUrl, Map<String, Object> params) {
		String res = null;
		String BOUNDARY = "--boundary--";
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
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
					sb.append("\r\n").append(BOUNDARY).append("\r\n");
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

	public static <T extends Map<String, ?>> String get(String url, T args) {
		StringBuilder sb = new StringBuilder();
		for (String key : args.keySet()) {
			try {
				sb.append(URLEncoder.encode(key, "utf-8")).append("=")
						.append(URLEncoder.encode(args.get(key).toString(), "utf-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (url.startsWith("https")) {
			return https(url, "get", sb.toString().substring(0, sb.toString().length() - 1));
		}
		return sendGet(url, sb.toString().substring(0, sb.toString().length() - 1));
	}

	public static <T extends Map<String, ?>> String post(String url, T args) {
		StringBuilder sb = new StringBuilder();
		for (String key : args.keySet()) {
			try {
				sb.append(URLEncoder.encode(key, "utf-8")).append("=")
						.append(URLEncoder.encode(args.get(key).toString(), "utf-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (url.startsWith("https")) {
			return https(url, "post", sb.toString().substring(0, sb.toString().length() - 1));
		}
		return sendPost(url, sb.toString().substring(0, sb.toString().length() - 1));
	}

	public static String sendGet(String url, String params) {
		String result = null;
		BufferedReader in = null;
		try {
			String urlName = url + "?" + params;
			URL realUrl;
			realUrl = new URL(urlName);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible;MSIE)");
			conn.connect();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static String sendPost(String url, String params) {
		PrintWriter out = null;
		BufferedReader br = null;
		String result = "";
		try {
			URL realURL = new URL(url);
			URLConnection conn = realURL.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0(compatible;MSIE)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(params);
			out.flush();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("程序出现异常" + e);
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String https(String requestUrl, String requestMethod, String paramsStr) {
		String res = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			// if ("GET".equalsIgnoreCase(requestMethod))
			httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != paramsStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(paramsStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			res = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	static class MyX509TrustManager implements X509TrustManager {
		X509TrustManager sunJSSEX509TrustManager;

		MyX509TrustManager() throws Exception {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("trustedCerts"), "passphrase".toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
			tmf.init(ks);
			TrustManager tms[] = tmf.getTrustManagers();
			for (int i = 0; i < tms.length; i++) {
				if (tms[i] instanceof X509TrustManager) {
					sunJSSEX509TrustManager = (X509TrustManager) tms[i];
					return;
				}
			}
			throw new Exception("Couldn't initialize");
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			try {
				sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
			} catch (CertificateException excep) {
			}
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			try {
				sunJSSEX509TrustManager.checkServerTrusted(arg0, arg1);
			} catch (CertificateException excep) {
			}
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return sunJSSEX509TrustManager.getAcceptedIssuers();
		}
	}
}
