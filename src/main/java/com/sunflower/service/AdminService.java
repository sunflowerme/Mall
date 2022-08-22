package com.sunflower.service;

import com.sunflower.pojo.Admin;

public interface AdminService {
    //完成登录判断
    Admin login(String name, String pwd); //访问了一次数据库，就直接把访问到的信息以对象的形式返回给控制器，以备后期使用




}
