package com.freshjuice.fl.utils;

import javax.servlet.http.HttpServletRequest;

public class FlWebUtils {
	
	/**
	 * 根据request.getHeader判断是否是ajax请求
	 * @param request
	 * @return
	 */
	public static boolean fAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if("XMLHttpRequest".equals(requestType)) return true;
		return false;
	}
	
}
