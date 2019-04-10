package com.freshjuice.fl.web.index;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
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

import com.freshjuice.fl.web.DemoController;

@Controller
public class IndexController {
	
	private Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	@RequestMapping("/pr1")
	@RequiresPermissions(value={"pr1"})
	public ModelAndView pr1() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/pr1");
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
	
	//login 地址 和 FormAuthenticationFilter中submit地址
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		if(subject != null && subject.isAuthenticated()) {
			return index();
		}
		
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
        //根据shiro返回的异常类路径判断，抛出指定异常信息
        String errorMsg = null;
		if(exceptionClassName != null){
			//如果exceptionClassName不为null，表示是shiro认证失败的逻辑
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                errorMsg = "账号不存在";
            } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
                errorMsg = "用户名/密码错误";
            } else {
				errorMsg = "认证失败";
            }
            
            //如果login页面表单提交是ajax请求，则返回json格式错误信息
            if("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))){
            	ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
    			mv.addObject("code", "000");
    			mv.addObject("message", errorMsg);
    			return mv;
            } else {
            //否则跳转login，封装错误信息
            	ModelAndView modelAndView = new ModelAndView();
        		modelAndView.setViewName("WEB-INF/jsps/login");
        		modelAndView.addObject("errorMsg", errorMsg);
        		return modelAndView;
            }
        }
		
        //跳转login页面逻辑
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/login");
		modelAndView.addObject("errorMsg", errorMsg);
		return modelAndView;
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
	
	@ExceptionHandler(value=RuntimeException.class)
	public ModelAndView exy(Exception e, 
			HttpServletRequest request, HttpServletResponse response) {
		String accept = request.getHeader("Accept");
		boolean isJson = true;
		if(accept != null && accept.contains("text/html")) {
			isJson = false;
		}
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
	}
}
