package com.freshjuice.fl.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.freshjuice.fl.service.DemoService;

@Controller("demoController")
@RequestMapping("/dd")
public class DemoController {
	@Autowired
	private DemoService demoService;
	private Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		if(subject != null && subject.isAuthenticated()) {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("index");
			return modelAndView;
		}
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
        //根据shiro返回的异常类路径判断，抛出指定异常信息
        if(exceptionClassName!=null){
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                throw new RuntimeException("账号不存在");
            } else if (IncorrectCredentialsException.class.getName().equals(
                    exceptionClassName)) {
                throw new RuntimeException("用户名/密码错误");
            } else if("randomCodeError".equals(exceptionClassName)){
                throw new RuntimeException("验证码错误 ");
            }else {
                try {
					throw new Exception("认证失败");
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        }
        ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/login");
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/demo")
	public Map<String, String> demo(String id) {
		Map<String, String> map = demoService.getById(id);
		return map;
	}
	
	@RequestMapping("/addDemo")
	@ResponseBody
	public String addDemo() {
		Map<String, String> addDemo = new HashMap<String, String>();
		addDemo.put("userId", "6");
		demoService.addDemo(addDemo);
		return "success";
	}
	
	@RequestMapping("/auth")
	public ModelAndView indexJsp(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Subject subject = SecurityUtils.getSubject();
			System.out.println(subject);
			Session s = subject.getSession();
			Collection<Object> keys = s.getAttributeKeys();
			System.out.println(s);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		modelAndView.setViewName("WEB-INF/jsps/auth");
		modelAndView.addObject("name", "name-string");
		return modelAndView;
	}
	
	@ExceptionHandler(value=RuntimeException.class)
	public ModelAndView exy(Exception e, 
			HttpServletRequest request, HttpServletResponse response) {
		
		String accept = request.getHeader("Accept");
		System.out.println(accept);
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
			return new ModelAndView("error");
		}
	}
	
	
	
}
