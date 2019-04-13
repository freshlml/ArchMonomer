package com.freshjuice.fl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.sql.DataSource;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.freshjuice.fl.dao.resource.IResourceDao;
import com.freshjuice.fl.dao.resource.impl.ResourceDaoImpl;
import com.freshjuice.fl.service.resource.IResourceService;
import com.freshjuice.fl.service.resource.impl.ResourceServiceImpl;
import com.freshjuice.fl.shiro.filter.FlFormAuthenticationFilter;

@Configuration
@ComponentScan(basePackages = {"com.**.service", "com.**.dao", "com.**.filter"})
/*@ComponentScan(basePackages = "com.freshjuice.fl", 
useDefaultFilters = false, 
excludeFilters = {
		@ComponentScan.Filter(type=FilterType.ANNOTATION, value={Controller.class})  
})*/
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableTransactionManagement(proxyTargetClass=true)
@Import(value={ApplicationDao.class, ApplicationShiro.class})
public class ApplicationConfig {
	
	//https://hanqunfeng.iteye.com/blog/2113820 零配置参照博客
	
	
	
}
