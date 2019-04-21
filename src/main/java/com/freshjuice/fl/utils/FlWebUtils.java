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
	
	/**
	 * 根据请求的accept做判断
	 * 1、如果accept中存在text/html，表示是html请求返回页面
	 * 否则返回json
	 * 
	 * 注： $.load调用底层使用的是$.ajax:dataTpye="html"
	 * 
	 */
	public static boolean fAjaxRequestAccept(HttpServletRequest request) {
		String accept = request.getHeader("Accept");
		if(accept != null && accept.contains("text/html")) return false;
		return true;
	}
	
	
}
