package com.freshjuice.fl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshjuice.fl.shiro.*;
import com.freshjuice.fl.shiro.phone.PhoneCredentialsMatcher;
import com.freshjuice.fl.shiro.phone.PhoneRealm;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.freshjuice.fl.service.base.IResourceService;
import com.freshjuice.fl.service.base.IUserService;
import com.freshjuice.fl.shiro.filter.FlFormAuthenticationFilter;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class ApplicationShiro {

	/*shiro 架构core组件 结合源码调试和pdf，nothing probleam
    SecurityManager
    Authenticator
	Subject
	SessionManager
	SessionDao
	Authrizer
    Realm
    CacheManager
    Cryptography
	*/

	/*web support 组件，用于集成web环境
	* ShiroFilterFactoryBean 生成一个过滤器链
	*  如 /** flauthc,permission("perm-name") 将在/**请求url上引应用Authentictor和Authorizer拦截器
	*  其中Authentictor系列拦截器为认证拦截器，Authorizer系列拦截器为授权拦截器
	*
	* FlFromAuthenticationFilter继承自FromAuthenticationFilter
	* 1 增加资源路径的判断逻辑
	* 2 将login loginconfirm等请求直接转发给Controller
	*
    * 前后端不分离模式下的权限控制
    * 1、资源的抽象：对于页面上所有的请求url都当成一个资源
    *
    * 两种Authentication方式
    *  CustomRealm UsernamePasswordToken 用户名密码token
    *  PhoneRealm PhoneToken 手机号验证码token
    *  定义FlMultiRealmSuccessfulStrategy: 多realm时的选择策略
    *  PhoneRealm中定义CredentialsMatcher: 对验证码验证
    *  Authentication过程AuthenticationInfo中Principal统一封装成UserPrincipal对象
	*/

   /*

		session 高级 manager

		异常处理
		shiro使用Filter，before 于 DispatcherServlet的异常处理，所以需要提供异常处理

		cache的使用
		1 session的cache
		2 manager的cache
		3 permissions的cache

		使用缓存时 realm的doGetAuthenticationInfo方法中不能存在校验的代码
		使用缓存时 realm的name是否覆盖

		通过数据库查找数据，使用数据库的缓存，不使用shiro的缓存支持，或者两者存在一致性

        同一个浏览器，重新登陆问题

		logout实践

		ssl拦截器实践

		注解形式Authorizating原理，调试Authorizating
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
	public CustomRealm customRealm() {
		CustomRealm customRealm = new CustomRealm();
		customRealm.setUserService(userService);
		customRealm.setResourceService(resourceService);
		return customRealm;
	}

	/**
	 * phone realm credentialsMatcher
	 */
	@Bean
	public PhoneCredentialsMatcher phoneCredentialsMatcher() {
		return new PhoneCredentialsMatcher();
	}
	/**
	 *phone realm
	 */
	@Bean
	public PhoneRealm phoneRealm() {
		PhoneRealm phoneRealm = new PhoneRealm();
		phoneRealm.setUserService(userService);
		phoneRealm.setResourceService(resourceService);
		phoneRealm.setCredentialsMatcher(phoneCredentialsMatcher());
		return phoneRealm;
	}

	@Autowired
	private RedisTemplate<Serializable, Session> redisTemplateRedisSession;

	/**
	 * session Id 生成器
	 * @return
	 */
	@Bean
	public SessionIdGenerator sessionIdGenerator() {
	    return new JavaUuidSessionIdGener();
	}
	/**
	 * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
	 * MemorySessionDAO 直接在内存中ConcurrentHashMap进行会话维护
	 * EnterpriseCacheSessionDAO  利用缓存功能维护会话，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
	 * @return
	 */
	@Bean
	public SessionDAO sessionDao() {
		FlCacheSessionDAO flCacheSessionDAO = new FlCacheSessionDAO();
		flCacheSessionDAO.setCacheManager(new AbstractCacheManager() {
			protected Cache<Serializable, Session> createCache(String name) throws CacheException {
				return new ShiroRedisCache(name, redisTemplateRedisSession);
			}
		});
	    //设置session缓存的名字 默认为 shiro-activeSessionCache
		flCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
	    //sessionId生成器
		flCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
	    return flCacheSessionDAO;
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
		
		defaultWebSessionManager.setGlobalSessionTimeout(3600000); //session对象失效时间设置为1h
		defaultWebSessionManager.setDeleteInvalidSessions(true); //删除invalid session
		defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false); //url后面去掉;sessionId参数
		defaultWebSessionManager.setSessionValidationSchedulerEnabled(true); //开启会话调度器：定期检查会话是否过期
		return defaultWebSessionManager;
	}

	/**
	 * 多realm时的strategy
	 */
	@Bean
	public FlMultiRealmSuccessfulStrategy flMultiRealmSuccessfulStrategy() {
		return new FlMultiRealmSuccessfulStrategy();
	}

	/**
	 * ModularRealmAuthenticator
	 */
	@Bean
	public ModularRealmAuthenticator modularRealmAuthenticator() {
		ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
		authenticator.setAuthenticationStrategy(flMultiRealmSuccessfulStrategy());
		return authenticator;
	}

	/**
	 * shiro管理器，Authentication Authority 管理
	 * 注入 Realm  SessionManager
	 *
	 * @return
	 */
	@Bean
	public SecurityManager securityManager(CustomRealm customRealm, PhoneRealm phoneRealm,
			DefaultWebSessionManager defaultWebSessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

		securityManager.setAuthenticator(modularRealmAuthenticator()); //设置authenticator
		securityManager.setSessionManager(defaultWebSessionManager);  //设置session manager

		List<Realm> realmList = new ArrayList<Realm>();
		realmList.add(customRealm);
		realmList.add(phoneRealm);
		securityManager.setRealms(realmList);
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
		filterChainDefinitionMap.put("/statics/**", "anon");
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
