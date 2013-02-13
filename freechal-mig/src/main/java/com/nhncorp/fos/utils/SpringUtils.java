/*
 * @(#) SpringUtils.java 2013. 1. 29 
 *
 * Copyright 2013 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.nhncorp.fos.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * spring 을 편하게 사용하기 위한 utility 들
 * @author Hwang Seong-wook
 * @since 2013. 01. 29.
 */
public class SpringUtils {
	/**
	 * 현재 요쳥의 {@link HttpServletRequest} 값을 반환한다.
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes attr = RequestContextHolder.getRequestAttributes();
		if (null == attr || !ServletRequestAttributes.class.isInstance(attr)) {
			return null;
		}
		return ServletRequestAttributes.class.cast(attr).getRequest();
	}
}