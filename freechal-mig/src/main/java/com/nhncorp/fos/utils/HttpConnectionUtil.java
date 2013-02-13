/*
 * @(#) HttpConnectionUtil.java 2012. 12. 13 
 *
 * Copyright 2012 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.nhncorp.fos.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * HTTP 통신을 통하여 text 혹은 XML Document 를 가져오는 util.
 * 
 * @author hwang
 * @since 2012. 12. 13.
 */
public class HttpConnectionUtil {
	/**
	 * sso_user 정보를 저장할 쿠키명
	 */
	public static final String COOKIE_NM_SSO_USER = "SSO_USER";
	/**
	 * smsession 정보를 저장할 쿠키명
	 */
	public static final String COOKIE_NM_SMSESSION = "SMSESSION";

	private static Logger logger = Logger.getLogger(HttpConnectionUtil.class);

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.<br>
	 * GET 방식이며, 기본 타임아웃은 접속 10초, 응답 10초이다.
	 * 
	 * @param url
	 *            접속하려는 URL
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(String url) throws SocketTimeoutException,
		Exception {
		return getText(url, null, null);
	}

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.<br>
	 * GET 방식이며, 기본 타임아웃은 접속 10초, 응답 10초이다.
	 * 
	 * @param url
	 *            접속하려는 URL
	 * @param ssoUser
	 *            SSO_USER 쿠키값. 보통 사번이다. 사용하지 않으면 null.
	 * @param smSession
	 *            SMSESSION 쿠키값. SSO 인증을 위한 값. 사용하지 않으면 null.
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(String url, String ssoUser, String smSession) throws SocketTimeoutException,
		Exception {
		return getText(url, ssoUser, smSession, 10000, 10000);
	}

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.<br>
	 * GET 방식이다.
	 * 
	 * @param url
	 *            접속하려는 URL
	 * @param ssoUser
	 *            SSO_USER 쿠키값. 보통 사번이다. 사용하지 않으면 null.
	 * @param smSession
	 *            SMSESSION 쿠키값. SSO 인증을 위한 값. 사용하지 않으면 null.
	 * @param connectTimeout
	 *            연결 타임아웃 시간. 단위는 millisecond.
	 * @param readTimeout
	 *            응답 타임아웃 시간. 단위는 millisecond.
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(String url, String ssoUser, String smSession,
		int connectTimeout, int readTimeout) throws SocketTimeoutException,
		Exception {
		HttpConnectionInfo info = new HttpConnectionInfo(url, connectTimeout,
			readTimeout);
		info.addCookie(COOKIE_NM_SSO_USER, ssoUser);
		info.addCookie(COOKIE_NM_SMSESSION, smSession);
		return getText(info);
	}

	/**
	 * URL 에 접속하여 나오는 string 을 반환한다.
	 * 
	 * @param info
	 *            접속에 필요한 정보를 가지고 있는 도메인
	 * @return
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws Exception
	 */
	public static String getText(HttpConnectionInfo info) throws SocketTimeoutException,
		Exception {
		if (null == info) {
			return null;
		}
		InputStream is = getInputStream(info);
		if (is != null) {
			return is2str(is);
		}
		logger.warn("No data on this URL...");
		return null;
	}

	/**
	 * 접속 정보를 기반으로 접속하여 connection 을 반환한다.
	 * 
	 * @param info
	 * @return
	 * @throws MalformedURLException
	 *             URL 이 잘못된 경우
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws IOException
	 *             일반적인 IO 예외
	 */
	public static HttpURLConnection getConnection(HttpConnectionInfo info) throws MalformedURLException,
		SocketTimeoutException,
		IOException {
		if (null == info) {
			return null;
		}
		String url = info.getUrl();
		if ("GET".equals(info.getRequestMethod())) {
			url = info.getFullUrl();
		}
		HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
		conn.setConnectTimeout(info.getConnectTimeout());
		conn.setReadTimeout(info.getReadTimeout());
		conn.setRequestMethod(info.getRequestMethod());
		conn.setRequestProperty("Content-Language", info.getEncoding());
		if (null != info.getHeaderMap()) {
			for (String key : info.getHeaderMap().keySet()) {
				conn.setRequestProperty(key, info.getHeaderMap().get(key));
			}
		}
		if (null != info.getCookieMap()) {
			StringBuilder cookie = new StringBuilder();
			for (String key : info.getCookieMap().keySet()) {
				cookie.append(key).append("=").append(
					info.getCookieMap().get(key)).append(";");
			}
			conn.setRequestProperty("Cookie", cookie.toString());
		}
		String requestBody = info.getRequestBody();
		if ("POST".equals(info.getRequestMethod()) && requestBody.length() > 0) {
			conn.setFixedLengthStreamingMode(requestBody.getBytes(info.getEncoding()).length);
			conn.setDoOutput(true);
			OutputStreamWriter osw = new OutputStreamWriter(
				conn.getOutputStream(), info.getEncoding());
			osw.write(requestBody);
			osw.flush();
			osw.close();
		}
		conn.connect();
		return conn;
	}

	/**
	 * 접속 정보를 기반으로 접속하여 inputStream 을 반환한다.<br>
	 * 접속이 실패하면 null 을 반환.<br>
	 * 반환된 inputStream 은 꼭 close 를 해주어야 한다.
	 * 
	 * @param info
	 * @return
	 * @throws MalformedURLException
	 *             URL 이 잘못된 경우
	 * @throws SocketTimeoutException
	 *             타임아웃에 걸렸을 경우
	 * @throws IOException
	 *             일반적인 IO 예외
	 */
	public static InputStream getInputStream(HttpConnectionInfo info) throws MalformedURLException,
		SocketTimeoutException,
		IOException {
		if (null == info) {
			return null;
		}
		HttpURLConnection conn = getConnection(info);
		if (conn != null) {
			return conn.getInputStream();
		}
		return null;
	}

	/**************************************************
	 * 
	 * Private Methods
	 * 
	 **************************************************/

	/**
	 * InputStream 을 string 으로 변환하여 반환한다.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 *             일반적인 IO 예외
	 */
	private static String is2str(InputStream is) throws IOException {
		if (null == is) {
			return null;
		}
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		char[] cbuf = new char[1];
		StringBuilder sb = new StringBuilder();
		while (isr.read(cbuf) > 0) {
			sb.append(cbuf);
		}
		isr.close();
		is.close();
		return sb.toString();
	}
}