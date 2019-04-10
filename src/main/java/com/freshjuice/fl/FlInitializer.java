package com.freshjuice.fl;

import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

public class FlInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		
		/**
		 * 注册 servlet
		 */
		/*servletClz = servletContext.addServlet("name", ServletClass.class);
		servletClz.addMapping("/nnn/*");*/
		
		/**
		 * 注册 filter
		 */
		Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", 
				CharacterEncodingFilter.class);
		characterEncodingFilter.setInitParameter("encoding", "UTF-8");
		characterEncodingFilter.setInitParameter("forceEncoding", "true");
		characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
		
		/**
		 * shiroFilter
		 */
		Dynamic shiroFilter = servletContext.addFilter("shiroFilter", DelegatingFilterProxy.class);
		shiroFilter.setInitParameter("targetFilterLifecycle", "true");
		shiroFilter.setInitParameter("targetBeanName", "shiroFilterBean");
		shiroFilter.addMappingForUrlPatterns(null, false, "/*");
		
		
	}

}
