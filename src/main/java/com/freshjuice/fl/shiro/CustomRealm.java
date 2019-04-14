package com.freshjuice.fl.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.freshjuice.fl.service.resource.IResourceService;
import com.freshjuice.fl.service.user.IUserService;

public class CustomRealm extends AuthorizingRealm {
	
	private IUserService userService;
	private IResourceService resourceService;
	
	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public IResourceService getResourceService() {
		return resourceService;
	}
	public void setResourceService(IResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection paramPrincipalCollection) {
		
		String pricipal = (String) paramPrincipalCollection.getPrimaryPrincipal();
		
		if(pricipal == null) return null;
		//根据用户，加载其权限信息
		List<String> permissions = resourceService.getPermissionsOfUserByUn(pricipal);
		
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        
        return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token)
			throws AuthenticationException {
		
		String principal = (String) token.getPrincipal(); //用户输入的用户凭证
		String credencial = userService.getPswdOfUserByUn(principal);
		
		if(credencial == null) return null; //返回null 将抛出UnknownAccountException
		
		return new SimpleAuthenticationInfo(principal, credencial, this.getName());
	}

	

}
