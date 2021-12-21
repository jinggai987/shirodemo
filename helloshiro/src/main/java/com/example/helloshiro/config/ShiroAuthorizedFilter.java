package com.example.helloshiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义的Shiro拦截器. 作用是覆盖onPreHandle方法. 只有密码校验通过且有权限访问时,才返回true.
 */
public class ShiroAuthorizedFilter extends PathMatchingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroAuthorizedFilter.class);

    /**
     * 是否启用全地址URI会拦截 true 启用 false 不启用
     */
    public static final boolean isqy = true;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        // 获得访问请求地址
        String servletPath = ((ShiroHttpServletRequest) request).getServletPath();
        // 是否启用全地址拦截
        if (servletPath.length() > 1) {
            if (!isqy) {
                String[] servletPaths = servletPath.split("/");
                servletPath = servletPaths[1];
            } else {
                servletPath = servletPath.substring(1);
            }
        }
        // 是否登录
        boolean isAuthenticated = subject.isAuthenticated();
        if (!isAuthenticated) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect("/toLogin");
//            resp.setStatus(HttpServletResponse.SC_OK);
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json;charset=utf-8");
//            resp.getWriter().print("未登录");
            return false;
        }
        // 进行权限拦截 地址是否有权限访问
        boolean isPermitted = subject.isPermitted(servletPath);
        if (!isPermitted) {
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            resp.getWriter().print("无权限");
            return false;
        }
        return true;

    }


}