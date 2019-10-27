package com.freshjuice.fl.web.interceptor;

import com.freshjuice.fl.base.entity.PriorityResource;
import com.freshjuice.fl.exception.FlResourceExistsException;
import com.freshjuice.fl.base.service.IResourceService;
import com.freshjuice.fl.utils.FlWebUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该interceptor只应用于RequestMappingHandlerMapping
 * 1 判断请求url是否在resource表中定义（AuthenticationFilter中在未认证时判断了请求url的exists,此interceptor弥补FlFormAuthentication认证的exits判断
 *    此处存在的一个隐藏BUG:FlFormAuthentication中基类PathMathingFilter，而此处没有patg matcher的过滤，不过这也应该不算bug，顶多算不一致），抛出异常形式
 * 2 该interceptor获取执行链中的Controller实例，判断HandlerMethod上是否有@ResponseBody注解或者Controller上是否RestController,从而记录是否返回json格式
 *
 */
public class FlBaseInterceptor implements HandlerInterceptor {

    private IResourceService resourceService;

    public IResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求url exists
        String requestPath = FlWebUtils.getRequestUri(request);
        String applicationPath = FlWebUtils.getPathWithinApplication(request);
        PriorityResource resource = resourceService.getFResourceOfPath(applicationPath);
        if(resource == null) throw new FlResourceExistsException("请求资源[" + requestPath + "]不存在");

        if(handler.getClass() == HandlerMethod.class) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RestController beanAnno = handlerMethod.getBean().getClass().getAnnotation(RestController.class);
            if(beanAnno != null) {//mark ajax
                request.setAttribute(FlWebUtils.FULL_AJAX_MARK, "1");
            } else {
                ResponseBody methodAnno = handlerMethod.getMethodAnnotation(ResponseBody.class);
                if(methodAnno != null) { //mark ajax
                    request.setAttribute(FlWebUtils.FULL_AJAX_MARK, "1");
                } else { //mark view
                    request.setAttribute(FlWebUtils.FULL_AJAX_MARK, "0");
                }
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
