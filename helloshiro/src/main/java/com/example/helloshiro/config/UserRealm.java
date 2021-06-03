package com.example.helloshiro.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRealm extends AuthorizingRealm {
    private final transient Logger log = LoggerFactory.getLogger(this.getClass());
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权方法进来了->principalCollection:{}");
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("认证方法进来了->doGetAuthorizationInfo");

        String name = "zhangsan";
        String password = "zhangsan";

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        if(!name.equals(usernamePasswordToken.getUsername())){
            //这里返回null,会直接抛出UnknownAccountException异常
            return null;
        }

        return new SimpleAuthenticationInfo("",password,"");
    }
}
