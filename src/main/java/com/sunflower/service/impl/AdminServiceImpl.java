package com.sunflower.service.impl;

import com.sunflower.mapper.AdminMapper;
import com.sunflower.pojo.Admin;
import com.sunflower.pojo.AdminExample;
import com.sunflower.service.AdminService;
import com.sunflower.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务逻辑层的实现类
 */

@Service
public class AdminServiceImpl implements AdminService {

    //在业务逻辑层中一定会有数据访问层的对象
    @Autowired
    AdminMapper adminMapper;  //利用Spring的注解注入，从而创建对象

    @Override
    public Admin login(String name, String pwd) {

        //1.根据传入的用户名到数据库中查询相应的用户对象
          //1.1如果有条件，则一定要创建AdminExample的对象，用来封装条件
        AdminExample example = new AdminExample();
          //1.2如何添加条件？  分析：带条件的查询语句： select * from admin where a_name = 'admin'
          //1.2.1添加用户名a_name条件
        example.createCriteria().andANameEqualTo(name);//进入andANameEqualTo(name)底层源码看，发现实现的就是条件的拼接 a_name = name

        List<Admin> list = adminMapper.selectByExample(example);  //通过example条件进行查询，将查询的对象放入list集合中

        if(list.size() > 0) {
            Admin admin = list.get(0); //找到该用户，一般默认用户名唯一性.再进行密码的比对

            //2.如果查询到用户对象，再进行密码的比对：注意数据库中的密码的存储是密文。
            /**
             * 分析： admin.getPass ===> 是从数据库中获取出来的用户对象的密码，此时是密文。
             *       而 pwd ===> 从页面传来的密码是明文 000000；
             *       则此时在进行密码的对比时，要将传入的密码明文进行 MD5加密，加密后再进行对比。
             */
            String miPwd = MD5Util.getMD5(pwd);
            if(miPwd.equals(admin.getaPass())) {
                return admin;
            }
        }

        return null;
    }
}
