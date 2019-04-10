package com.freshjuice.fl;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class FlWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	/**
	 * spring listener 上下文配置
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{ApplicationConfig.class, ApplicationTextConfig.class};
	}
	
	/**
	 * dispacher servlet 上下文配置
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{MvcConfig.class};
	}

	/**
	 * dispacher servlet 映射
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
	
	/**
	 * 
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		//配置StandardServletMultipartResolver的参数
		registration.setMultipartConfig(new MultipartConfigElement("/tmp/", 2097152, 4194304, 0));
	}
	
}
