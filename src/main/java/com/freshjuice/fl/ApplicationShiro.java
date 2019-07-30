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

import com.freshjuice.fl.service.base.IResourceService;
import com.freshjuice.fl.service.base.IUserService;
import com.freshjuice.fl.shiro.CustomRealm;
import com.freshjuice.fl.shiro.filter.FlFormAuthenticationFilter;


@Configuration
public class ApplicationShiro {
	
	/*使用shiro认证和授权 (https://segmentfault.com/a/1190000013875092)

		规则之一
		ShiroFilterFactoryBean 对每一个配置的url生成一个过滤器链
		规则之二
		SecurityManager基于SessionManager管理Session,Session用于保存认证信息,将session缓存起来？缓存的session作为共享session?
		规则之三
		FilterChain的执行

		使用shiro的Authenticatin Authority
		单体应用，前后端不分离模式下的权限控制
		1、资源的抽象：对于页面上所有的请求url都当成一个资源
		
		FlFromAuthenticationFilter 重定义  onAccessDenied 覆盖FormAuthenticationFilter的逻辑
		1、增加对资源路径的判断逻辑
		2、将/login跳转和/login登陆确认的逻辑分开（FormAuthenticationFitler中通过POST请求区分）
		如果是/login登陆确认请求，将该请求直接转发，在controller中执行登陆确认逻辑，登陆成功或者失败可根据
		页面需要返回页面或者json（FormAuthenticationFilter中如果登陆成功，
		会重定向到页面，这样默认的实现逻辑不行）
		3、全部使用重定向（凡是页面上表单同步提交到后台的请求，处理该请求之后都应该重定向到页面，
		如果使用跳转，浏览器地址栏url不变，浏览器刷新将导致表单重复提交[当然，前端代码可以修改浏览器地址栏的地址啦]）
		

		手机号动态验证码认证实现
		
		flphone拦截器： 对/phoneLogin/*拦截
		1、/phoneLogin/getCredit  转发给controller处理 记录并返回credit
		2、/phoneLogin/login      转发给controller处理 验证手机号和credit，根据手机号获取用户信息，执行登陆
		（使用FlFormAuthenticationFilter兼容上述过程）
		需要考虑：1 自定义token 配置多个realm ？等等实现问题
		   还是说phone校验成功后，根据手机号查找到用户，模拟username 和 password
		
		第三方登陆支持： 需要第三方支持auth2，本系统作为client通过auth2去和第三方沟通得到accessToken，通过accessToken就可以访问第三方的信息
		根据该信息在本系统中抽象成本系统的username、password，即设置为Authentication成功

		异常处理
		shiro使用Filter，独立于DispatcherServlet的异常处理，所以需要提供异常处理
		
		shiro中基于username、password的密码加密

		cache的使用
		1 session的cache
		2 manager的cache
		3 permissions的cache
		
		配置多个realm的规则？？
		
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
