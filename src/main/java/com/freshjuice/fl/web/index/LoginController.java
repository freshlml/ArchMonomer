package com.freshjuice.fl.web.index;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.freshjuice.fl.utils.FlWebUtils;


@Controller
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	//login 跳转
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("WEB-INF/jsps/login");
		modelAndView.addObject("errorMsg", request.getParameter("errorMsg"));
		return modelAndView;
	}
	
	//login 登陆确认
	@RequestMapping("/loginConfirm")
	public ModelAndView loginConfirm(String username, String password, String rememberMe,
			HttpServletRequest request, HttpServletResponse response) {
		
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, WebUtils.isTrue(request, rememberMe), request.getRemoteHost());
		try {
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			//authentication success
			//如果该登陆确认请求是ajax请求，则返回json格式数据，提示authentication成功
			if(FlWebUtils.fAjaxRequest(request)) {
				ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
    			mv.addObject("code", "200");
    			mv.addObject("message", "authentication 成功!!!");
    			return mv;
			} else {
				//否则，重定向到主页 （如果页面传递了重定向地址，则重定向到该地址）
				ModelAndView modelAndView = new ModelAndView();
        		modelAndView.setViewName("redirect:/index");
        		return modelAndView;
			}
		} catch(AuthenticationException e) { //authentication failure
			String errorMsg;
			if (UnknownAccountException.class == e.getClass()) {
                errorMsg = "账号不存在";
            } else if (IncorrectCredentialsException.class == e.getClass()) {
                errorMsg = "用户名/密码错误";
            } else {
				errorMsg = "认证失败";
            }
			//如果该登陆确认请求是ajax请求，则返回json格式数据，提示authentication失败
			if(FlWebUtils.fAjaxRequest(request)) {
				ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
    			mv.addObject("code", "403");
    			mv.addObject("message", errorMsg);
    			return mv;
			} else { //否则，重定向到login页面，提示错误信息
				ModelAndView modelAndView = new ModelAndView();
        		modelAndView.setViewName("redirect:/login");
        		modelAndView.addObject("errorMsg", errorMsg);
        		return modelAndView;
			}
			
		} catch(Exception e) {
			ModelAndView mv = new ModelAndView("error");
			mv.addObject("errorMsg", e.getMessage());
			return mv;
		}
		
	}
	
	//根据手机号生成验证码，保存在session，返回
	@RequestMapping("/phoneLogin/getCredit")
	public Map<String, String> phoneGetCredit(String phoneNum) {
		return null;
	}
	
	//phone login登陆确认
	@RequestMapping("/phoneLogin/login")
	public ModelAndView phoneLogin(String phoneNum, String phoneCredit) {
		return null;
	}
}
