package com.sunflower.controller;

import com.sunflower.pojo.Admin;
import com.sunflower.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller //表示当前类是控制器层组件
@RequestMapping("/admin")
public class AdminAction {

    //注： 在所有的控制器(即界面层)一定会有业务逻辑层的对象；业务逻辑层一定会有数据访问层的对象

    //使用Spring的IOC注入业务逻辑层的对象
    @Autowired
    AdminService adminService;

    //实现登录判断，并进行相应的跳转
    @RequestMapping("/login")
    public String login(String name, String pwd, HttpServletRequest request) {
        Admin admin = adminService.login(name,pwd);  //调用业务逻辑层login方法，从数据库中获取admin用户
        if(admin != null) {
            //登录成功
            request.setAttribute("admin",admin); //如果正确，则将用户传到 main.jsp页面中 --->main.jsp的29行
            return "main";
        }else {
            //登录失败
            request.setAttribute("errmsg","用户名或者密码不正确" ); // 如果不正确，login.jsp直接使用EL表达式读取错误信息---》login.jsp49行
            return "login";
        }
    }

}
