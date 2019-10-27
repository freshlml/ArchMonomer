package com.freshjuice.fl.shiro.phone;

import com.freshjuice.fl.base.entity.User;
import com.freshjuice.fl.base.service.IResourceService;
import com.freshjuice.fl.base.service.IUserService;
import com.freshjuice.fl.shiro.PrincipalEnum;
import com.freshjuice.fl.shiro.UserPrincipal;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

public class PhoneRealm extends AuthorizingRealm {

    private IUserService userService;
    private IResourceService resourceService;
    public IUserService getUserService() {
        return userService;
    }

    public IResourceService getResourceService() {
        return resourceService;
    }

    public void setResourceService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        UserPrincipal userPrincipal = (UserPrincipal) principalCollection.getPrimaryPrincipal(); //only one principal exists

        //String principal = (String) paramPrincipalCollection.getPrimaryPrincipal();
        //if(userPrincipal == null) return null;  //如果无认证信息，但是该资源进行Authorize(这当属不正常情况)
        if(PrincipalEnum.PHONE.getValue().equals(userPrincipal.getType().getValue())) {
            List<String> permissions = resourceService.getPermissionsOfUserByUn(userPrincipal.getUsername());

            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addStringPermissions(permissions);

            return simpleAuthorizationInfo;
        }
        return null;

    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token.getClass() == PhoneToken.class;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        PhoneToken token = (PhoneToken) authenticationToken;
        String phone = token.getPhone();

        User user = userService.getUserByPhone(phone);

        if(user == null) throw new UnknownAccountException("手机号: [" + phone + "]不存在");

        return new SimpleAuthenticationInfo(new UserPrincipal(user.getUserName(), user.getPhone(), PrincipalEnum.PHONE),
                token.getPhoneCredit(),
                this.getName());

    }
}
