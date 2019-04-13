package com.freshjuice.fl.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class FlRootExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		//根据request 中 Accept 头判断 是返回view 还是 json
		String accept = request.getHeader("Accept");
		System.out.println(accept);
		boolean isJson = true;
		if(accept != null && accept.contains("text/html")) {
			isJson = false;
		}
		if(isJson) {
			ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
			mv.addObject("code", "500");
			mv.addObject("message", ex.getMessage());
			return mv;
		} else {
			return new ModelAndView("error");
		}
	}

}
