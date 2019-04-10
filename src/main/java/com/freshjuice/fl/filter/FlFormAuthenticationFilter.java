package com.freshjuice.fl.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshjuice.fl.dto.resource.PriorityResource;
import com.freshjuice.fl.service.resource.IResourceService;

@Component("flFormAuthenticationFilter")
public class FlFormAuthenticationFilter extends FormAuthenticationFilter {
	@Autowired
	private IResourceService resourceService;
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		
		//对于过来的一个请求，查询该url是否需要认证，不需要认证，返回true,需要认证，如果是ajax，提示未登录，如果是页面跳转请求，交给super
		if (!isLoginRequest(request, response)) {
			PriorityResource priorityResource = resourceService.getFResourceOfPath(getPathWithinApplication(request));
			if(priorityResource != null) { //priorityResource 为 null，交给super去处理认证
				if("0".equals(priorityResource.getAuthf())) {
					return true;
				} else if("0".equals(priorityResource.getShowf())) {
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json; charset=UTF-8");
					Map<String, String> map = new HashMap<String, String>();
					map.put("code", "403");
					map.put("msg", "请先登录");
					ObjectMapper objectMapper = new ObjectMapper();
		            response.getWriter().write(objectMapper.writeValueAsString(map));
		            return false;
				}
			}
		}
		return super.onAccessDenied(request, response);
	}
	
}
