package com.example.helloshiro.config;

import com.example.helloshiro.model.UserDemo;
import com.example.helloshiro.service.IUserDemoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UserRealm extends AuthorizingRealm {
    private final transient Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserDemoService iUserDemoService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("授权方法进来了->principalCollection:{}");
        // 因为非正常退出，即没有显式调用 SecurityUtils.getSubject().logout()
        // (可能是关闭浏览器，或超时)，但此时缓存依旧存在(principals)，所以会自己跑到授权方法里。
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            doClearCache(principalCollection);
            SecurityUtils.getSubject().logout();
            return null;
        }

        UserDemo userDemo = (UserDemo) principalCollection.getPrimaryPrincipal();
        if (userDemo == null) {
            return null;
        }
        // 根据用户信息从库中拿权限和角色信息放入SimpleAuthorizationInfo实体
        // 同一用户可以有多权限，多角色
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 权限
        authorizationInfo.addStringPermission("user:add");
        // 角色
        authorizationInfo.addRole("admin");

        return authorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("认证方法进来了->doGetAuthenticationInfo");

        //连接真实数据库

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        if(!StringUtils.hasLength(usernamePasswordToken.getUsername())){
            //这里返回null,会直接抛出UnknownAccountException异常
            return null;
        }
        UserDemo userDemo = iUserDemoService.getByName(usernamePasswordToken.getUsername());
        if (userDemo == null) {
            //这里返回null,会直接抛出UnknownAccountException异常
            return null;
        }

        return new SimpleAuthenticationInfo(userDemo, userDemo.getPassword(), super.getName());
    }
}
