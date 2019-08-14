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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
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
import com.freshjuice.fl.dao.base.IResourceDao;
import com.freshjuice.fl.dao.base.impl.ResourceDaoImpl;
import com.freshjuice.fl.service.base.IResourceService;
import com.freshjuice.fl.service.base.impl.ResourceServiceImpl;
import com.freshjuice.fl.shiro.filter.FlFormAuthenticationFilter;

@Configuration
@ComponentScan(basePackages = {"com.**.service", "com.**.dao", "com.**.filter"})
/*@ComponentScan(basePackages = "com.freshjuice.fl", 
useDefaultFilters = false, 
excludeFilters = {
		@ComponentScan.Filter(type=FilterType.ANNOTATION, value={Controller.class})  
})*/
@EnableAspectJAutoProxy(proxyTargetClass=true)  //enable @Aspectj
@EnableTransactionManagement(proxyTargetClass=true) //enable transaction 注解
@EnableCaching(proxyTargetClass=true) //enale caching
@Import(value={ApplicationDao.class, ApplicationShiro.class, ApplicationCache.class})
public class ApplicationConfig {
	
	//https://hanqunfeng.iteye.com/blog/2113820 零配置参照博客
	
	/*创建bean时，指定@Profile,表示该bean的有效空间，通过spring.profiles.active激活对应的profile （ArchIsomer中配置了prod,dev的原理就是这）*/
    /*@Bean
    @Profile("dev")
    public DataSource devDataSource() {
        return null;
    }
    @Bean
    @Profile("prod")
    public DataSource prodDataSource() {
        return null;
    }*/
    /*
    * spring.profiles.active
    * 1.作为DispatcherServlet的参数设置
    * 2.作为web应用的上下文参数设置
    * 3.JVM系统属性设置
    * 4.在集成测试类上使用@ActiveProfiles注解设置
    * 5.使用环境变量设置
    */

    /*条件化bean @Conditional*/

    /*bean的歧义性 @Primary @Qualifier*/

}
