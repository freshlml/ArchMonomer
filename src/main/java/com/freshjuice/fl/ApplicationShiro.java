package com.freshjuice.fl;

import java.util.LinkedHashMap;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.freshjuice.fl.service.resource.IResourceService;
import com.freshjuice.fl.service.user.IUserService;
import com.freshjuice.fl.shiro.CustomRealm;
import com.freshjuice.fl.shiro.filter.FlFormAuthenticationFilter;


@Configuration
public class ApplicationShiro {
	
	/*使用shiro认证和授权 (https://segmentfault.com/a/1190000013875092)

		规则之一
		ShiroFilterFactoryBean 对每一个配置的url生成一个过滤器链
		规则之二
		SecurityManager基于SessionManager管理Session,Session用于保存认证信息
		规则之三
		FilterChain的执行
		
		ajax问题： 对于ajax请求，如果没有认证信息，FormAuthenticationFilter拦截后将执行login的重定向，重定向的页面html将返回给ajax
		1、将ajax记录在系统的资源表中，标记是否需要认证
		2、重写FormAuthenticationFilter拦截器，对url进行判断
		如果不是/login，则
			查询该url是否需要认证，不需要认证，返回true,需要认证，如果是ajax，提示未登录，如果是页面跳转请求，交给super
		
		使用shiro的Authenticatin Authority
		单体应用，前后端不分离模式下的权限控制
		1、资源的抽象：对于页面上所有的请求url都当成一个资源
		
		手机号动态验证码认证实现
		动态密码登陆设置hidden值，phone_login_kkk
		
		flphone拦截器： 对/login请求  并且是post请求 并且  hidden值为phone_login_kkk的 拦截  (或者对/phoneLogin/*拦截)
		验证，手机号和动态验证码是否正确，如果正确，根据手机号获取用户信息，设置为Authenticated，重定向到上一个页面
		                        如果不正确，记录异常信息，继续执行
		
		
		
		第三方登陆支持： 需要第三方支持auth2，本系统作为client通过auth2去和第三方沟通得到accessToken，通过accessToken就可以访问第三方的信息
		根据该信息在本系统中抽象成本系统的username、password，即设置为Authentication成功
		
		
		shiro的cacheManager使用，后续实践
		
		
	*/
	
	
	/**
	 * Realm组件，用于Authentication and Authority 回调
	 * @return
	 */
	@Autowired
	private IUserService userService;
	@Autowired
	private IResourceService resourceService;
	@Bean
	@Primary
	public AuthorizingRealm customRealm() {
		CustomRealm customRealm = new CustomRealm();
		customRealm.setUserService(userService);
		customRealm.setResourceService(resourceService);
		return customRealm;
	}
	/**
	 * session Id 生成器
	 * @return
	 */
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
	    return new JavaUuidSessionIdGenerator();
	}
	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
	 * MemorySessionDAO 直接在内存中进行会话维护
	 * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 * @return
	 */
	@Bean
	public SessionDAO sessionDao() {
	    EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
	    //使用resisCacheManager  关于缓存session 应该还有更过的细节需要考虑
	//enterpriseCacheSessionDAO.setCacheManager(redisCacheManager());
	    //设置session缓存的名字 默认为 shiro-activeSessionCache
	    enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
	    //sessionId生成器
	    enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
	    return enterpriseCacheSessionDAO;
	}
	/**
	 * Session in Cookie
	 * @return
	 */
	@Bean
	public SimpleCookie sessionIdCookie(){
	    //这个参数是cookie的名称
	    SimpleCookie simpleCookie = new SimpleCookie("sid");
	    //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
	    simpleCookie.setHttpOnly(true);
	    simpleCookie.setPath("/");
	    simpleCookie.setMaxAge(-1); //设置写给浏览器的sessionId的有效时间为-1即会话中有效
	                                //注明：1、一般可以设置session过期时间为6h,写给浏览器的过期时间为6h
	                                //但是这个一般通过rememberMe实现，即通过写一个remeberMecookie值（设置较长有效期），shiro支持根据该rememberMe执行认证
	    return simpleCookie;
	}
	/**
	 * SessionManager 定义session管理器
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager defaultWebSessionManager(SimpleCookie sessionIdCookie,
			SessionDAO sessionDao) {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		//设置SessionListener  Session操作中一些事件的监听
		defaultWebSessionManager.setSessionIdCookie(sessionIdCookie); //设置SessionInCookie  设置 session写给cookie的格式
		//设置SeesionDao
		defaultWebSessionManager.setSessionDAO(sessionDao);
		//设置cacheManager cacheManager应该有更过细节
	//defaultWebSessionManager.setCacheManager(redisCacheManager());
		
		defaultWebSessionManager.setGlobalSessionTimeout(21600000); //session对象失效时间设置为6h
		defaultWebSessionManager.setDeleteInvalidSessions(true); //删除invalid session
		defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false); //url后面去掉;sessionId参数
		defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
		return defaultWebSessionManager;
	}
	
	/**
	 * shiro管理器，Authentication Authority 管理
	 * 注入 Realm  SessionManager
	 * 
	 * @param realm
	 * @return
	 */
	@Bean
	public SecurityManager securityManager(AuthorizingRealm realm,
			DefaultWebSessionManager defaultWebSessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm);
		securityManager.setSessionManager(defaultWebSessionManager);  //设置session manager
	//cacheManager 应该有更多细节
	//securityManager.setCacheManager(redisCacheManager());
		return securityManager;
	}
	
    /*shiro FilterFactoryBean*/
    @Bean
	public ShiroFilterFactoryBean shiroFilterBean(SecurityManager securityManager,
			FlFormAuthenticationFilter flFormAuthenticationFilter) {
    	
		ShiroFilterFactoryBean shiroFilterBean = new ShiroFilterFactoryBean();
		shiroFilterBean.setSecurityManager(securityManager);  //security manager
		shiroFilterBean.setLoginUrl("/login");  //login 重定向地址 (并且form表单提交地址: 如果使用FormAuthenticationFilter默认规则的话)
		shiroFilterBean.setUnauthorizedUrl("/unauthorized"); //无权限时 重定向地址
		Map<String, javax.servlet.Filter> filters = new LinkedHashMap<String, javax.servlet.Filter>();
		filters.put("flauthc", flFormAuthenticationFilter); //添加FlFormAuthenticationFilter
		shiroFilterBean.setFilters(filters);
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		filterChainDefinitionMap.put("/css/**", "anon");   //AnonymousFilter 配置不需要认证的url
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/", "anon");
		filterChainDefinitionMap.put("/index", "anon");
		filterChainDefinitionMap.put("/error", "anon");
		
		/*filterChainDefinitionMap.put("/auu", "perms[auu]"); 配置形式配置 url需要什么权限*/
		
		filterChainDefinitionMap.put("/**", "flauthc");
		
		shiroFilterBean.setFilterChainDefinitionMap(filterChainDefinitionMap);//设置 filter执行链
		return shiroFilterBean;
	}
    /*shiro FilterFactoryBean*/
    
	/*Authority advisor 配置给MvcConfig: @RequiresPermissions用在controller上*/
	/*@Bean
	public AuthorizationAttributeSourceAdvisor
			authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = 
				new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	@Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}*/
}
