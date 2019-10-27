package com.freshjuice.fl.shiro;

import java.util.List;

import com.freshjuice.fl.base.entity.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.freshjuice.fl.base.service.IResourceService;
import com.freshjuice.fl.base.service.IUserService;

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

		UserPrincipal userPrincipal = (UserPrincipal) paramPrincipalCollection.getPrimaryPrincipal(); //only one principal exists

		//String principal = (String) paramPrincipalCollection.getPrimaryPrincipal();
		//if(userPrincipal == null) return null;  //如果无认证信息，但是该资源进行Authorize(这当属不正常情况)
		if(PrincipalEnum.USERNAME.getValue().equals(userPrincipal.getType().getValue())) {
			List<String> permissions = resourceService.getPermissionsOfUserByUn(userPrincipal.getUsername());

			SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
			simpleAuthorizationInfo.addStringPermissions(permissions);

			return simpleAuthorizationInfo;
		}
		return null;
	}

	/**
	 * super.supports的实现是UsernamePasswordToken及其子类 返回true；这里覆盖默认实现，只处理UsernamePasswordToken
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token.getClass() == UsernamePasswordToken.class;
	}

	/**
	 *
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token)
			throws AuthenticationException {

		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = usernamePasswordToken.getUsername();
		//String password = userService.getPswdOfUserByUn(username);
		User user = userService.getUserByUn(username);

		//if(password == null) return null;
		if(user == null) throw new UnknownAccountException("用户名: [" + username + "]不存在");

		return new SimpleAuthenticationInfo(new UserPrincipal(username, user.getPhone(), PrincipalEnum.USERNAME),
				user.getUserPswd(),
				this.getName());
	}

	

}
