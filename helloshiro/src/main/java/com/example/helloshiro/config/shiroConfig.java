package com.example.helloshiro.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class shiroConfig {

    /**
     * ShiroFilterFactoryBean
     * 设置安全管理器
     *
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecuritManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //获得所有filter
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        //配置拦截器,具体拦截内容见拦截Filter
        filters.put("shiroAuthorized", new ShiroAuthorizedFilter());
//        shiroFilterFactoryBean.setFilters(filters);
        /**
         * 添加shiro内置过滤器
         * anon:无需认证就可访问
         * authc:必须认证才能访问
         * user:必须拥有记住我功能才能访问
         * perms:拥有对某个资源权限才能访问
         * role:拥有某个角色权限才能访问
         */

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/usr/add", "perms[user:add]");
        filterChainDefinitionMap.put("/usr/*", "authc");
        filterChainDefinitionMap.put("/toLogin", "anon");
        filterChainDefinitionMap.put("/", "anon");
        //对所有URL请求走该权限拦截
        filterChainDefinitionMap.put("/**", "shiroAuthorized");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //设置登陆请求
        shiroFilterFactoryBean.setLoginUrl("/toLogin");
        return shiroFilterFactoryBean;
    }


    /**
     * 不同的redis集群 使用不同的IRedisManager实现类.
     *
     * @return
     */
    @Bean
    public IRedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
//        redisManager.setPassword("");
//        redisManager.setDatabase(0);
        redisManager.setTimeout(5000);
        return redisManager;
    }

    @Bean
    public RedisCacheManager redisCacheManager(IRedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        redisCacheManager.setExpire(3600);
        return redisCacheManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO(IRedisManager redisManager) {
        //获取REDISSESSION
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
        // 通过复写shiro的SessionDAO同样达到将shiro管理的session保存到redis集群的目的
        defaultSessionManager.setSessionDAO(redisSessionDAO);
        defaultSessionManager.setGlobalSessionTimeout(1800000L);
        // 禁用url传递jsessionId后 url不会传递比如?jsessionid=xxxx
        defaultSessionManager.setSessionIdUrlRewritingEnabled(false);
        // 删除失效session
        defaultSessionManager.setSessionValidationSchedulerEnabled(true);
        return defaultSessionManager;
    }


    /**
     * DefaultWebSecurityManager
     *
     * @param userRealm
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm,
                                                               @Qualifier("defaultWebSessionManager") SessionManager sessionManager,
                                                               @Qualifier("redisCacheManager") CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    /**
     * 获取realm对象，需要自己自定义
     * 用户认证都在这个方法里
     *
     * @return
     */
    @Bean
    public UserRealm userRealm(CacheManager redisCacheManager) {
        UserRealm userRealm = new UserRealm();
        userRealm.setCacheManager(redisCacheManager);

        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("permissionCache");
        return userRealm;
    }


}
