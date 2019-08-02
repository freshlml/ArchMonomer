package com.freshjuice.fl.web.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.freshjuice.fl.utils.FlWebUtils;


@Controller
public class IndexController {
	
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@RequestMapping("/pr1")
	@RequiresPermissions(value={"pr1"})
	public ModelAndView pr1() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/pr111");
		return modelAndView;
	}
	
	@RequestMapping("/pr1-c1")
	public ModelAndView pr1_c1() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/pr1_c1");
		return modelAndView;
	}
	
	@RequestMapping("/pr1-c2")
	@RequiresPermissions(value={"pr1-c2"})
	public ModelAndView pr1_c2() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/pr1_c2");
		return modelAndView;
	}
	
	@RequestMapping("/pr2")
	@RequiresPermissions(value={"pr2"})
	public ModelAndView pr2() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/pr2");
		return modelAndView;
	}
	
	@RequestMapping("/pr3")
	@ResponseBody
	public Map<String, String> pr3() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pr3", "pr3");
		return map;
	}
	
	
	/**
	 * 主页
	 * @return
	 */
	@RequestMapping(path={"/", "/index"}, method={RequestMethod.GET})
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");
		Subject subject = SecurityUtils.getSubject();
		Object principal = null;
		if(subject != null && subject.isAuthenticated()) {
			principal = subject.getPrincipal();
		}
		mv.addObject("pricipal", principal);
		return mv;
	}
	
	@RequestMapping("/error")
	public ModelAndView err(String errorMsg) {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("errorMsg", errorMsg);
		return mv;
	}
	
	/*@ExceptionHandler(value=RuntimeException.class)
	public ModelAndView exy(Exception e,
			HttpServletRequest request, HttpServletResponse response) {

		boolean isJson = FlWebUtils.fAjaxRequestAccept(request);
		if(isJson) {
			ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
			mv.addObject("code", "500");
			mv.addObject("message", e.getMessage());
			return mv;
		} else {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("errorMsg", e.getMessage());
			return mv;
		}
	}*/
}
