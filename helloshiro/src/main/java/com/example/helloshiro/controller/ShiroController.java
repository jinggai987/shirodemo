package com.example.helloshiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShiroController {

    @RequestMapping("/")
    public String toIndex(Model model){
        model.addAttribute("qqq", "World");
        return "index";
    }

    @RequestMapping("/usr/add")
    public String add(){
        return "usr/add";
    }

    @RequestMapping("/usr/update")
    public String update(){
        return "usr/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(String username, String password, Model model){
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            // 执行登陆
            currentUser.login(token);
            model.addAttribute("qqq", username);
            return "index";
        } catch (UnknownAccountException uae) {
            model.addAttribute("msg","用户名错误");
            return "login";
        } catch (IncorrectCredentialsException ice) {
            model.addAttribute("msg","密码错误");
            return "login";
        }


    }

}
