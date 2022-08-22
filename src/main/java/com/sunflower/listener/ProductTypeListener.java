package com.sunflower.listener;

import com.sunflower.pojo.ProductType;
import com.sunflower.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * ServletContextListener 是一个全局的监听器，用于获取全部的商品类型列表，存入到当前的得全局的作用域中。
 *   本身可以在controller中获取所有商品类型的列表，可以通过依赖注入的方式得到，但是，现在我们在监听器中，而string框架注册时也是以监听器的方式注册，
 * 而ContextLoaderListener实现的也是ServletContextListener，即就是他们两实现的是同一个监听器，没有办法保证哪一个监听器先被创建，哪一个先被执行，
 * 所以不能使用Spring容器的依赖注入，我们使用手工来获取当前的Spring容器。然后从容器中取出想要的ProductTypeServiceImpl的对象。
  */

@WebListener  //注册当前的监听器
public class ProductTypeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //手工从spring容器中取出ProductTypeServiceImpl的对象
          //1.获取spring的容器
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_*.xml");
          //2.获取业务逻辑层的对象
        ProductTypeService productTypeService = (ProductTypeService) context.getBean("ProductTypeServiceImpl");
         //获取所有商品的类别
        List<ProductType> typeList = productTypeService.getAll();
         //再放入到全局应用作用域中，供新增页面、修改页面、前台的查询功能提供全部商品类别集合
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
