package com.example.helloshiro.config;

import com.example.helloshiro.model.UserDemo;
import com.example.helloshiro.service.IUserDemoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {
    private final transient Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserDemoService iUserDemoService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权方法进来了->principalCollection:{}");

        SimpleAuthorizationInfo authorizationInfo= new SimpleAuthorizationInfo();
        //给用户授权
        authorizationInfo.addStringPermission("user:add");


        return authorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("认证方法进来了->doGetAuthorizationInfo");

        //连接真实数据库

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        UserDemo userDemo = iUserDemoService.getByName(usernamePasswordToken.getUsername());
        if(userDemo == null){
            //这里返回null,会直接抛出UnknownAccountException异常
            return null;
        }

        return new SimpleAuthenticationInfo("",userDemo.getPassword(),"");
    }
}
