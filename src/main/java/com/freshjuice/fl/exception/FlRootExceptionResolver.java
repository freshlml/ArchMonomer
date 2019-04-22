package com.freshjuice.fl.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.freshjuice.fl.utils.FlWebUtils;

public class FlRootExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		boolean isJson = FlWebUtils.fAjaxRequestAccept(request);
		if(isJson) {
			ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
			mv.addObject("code", "500");
			mv.addObject("message", ex.getMessage());
			return mv;
		} else {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("errorMsg", ex.getMessage());
			return mv;
		}
	}

}
