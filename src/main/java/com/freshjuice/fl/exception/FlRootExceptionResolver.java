package com.freshjuice.fl.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.freshjuice.fl.utils.FlWebUtils;

public class FlRootExceptionResolver implements HandlerExceptionResolver {
	private Logger logger = LoggerFactory.getLogger(FlRootExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		//boolean isJson = FlWebUtils.fAjaxRequestAccept(request);
		boolean isJson = FlWebUtils.fAjaxRequestAcceptExtend(request);
		String errMessage = "系统异常";
		if(UnauthorizedException.class == ex.getClass()) {
			errMessage = "无权限";
		} else if(UnauthenticatedException.class == ex.getClass()) {
			errMessage = "check权限前为Authentication";
		} else if(ex instanceof FlRootException) {
			errMessage = ex.getMessage();
		}
		logger.error(ex.getMessage());
		if(isJson) {
			ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
			mv.addObject("code", "500");
			mv.addObject("message", errMessage);
			return mv;
		} else {
			ModelAndView mv = new ModelAndView("redirect:/error");
			mv.addObject("errorMsg", errMessage);
			return mv;
		}
	}

}
