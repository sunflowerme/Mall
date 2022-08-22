package com.sunflower.service;

import com.github.pagehelper.PageInfo;
import com.sunflower.pojo.ProductInfo;
import com.sunflower.pojo.vo.ProductInfoVo;

import java.util.List;

public interface ProductInfoService {

    //显示全部商品，不分页
    List<ProductInfo> getAll();

    //分页功能的开发
    PageInfo splitPage(int pageNum,int pageSize);   //可以看数据库语言分页需要什么条件，即可知道参数如何写

    //增加商品
    int save(ProductInfo info);

    //更新时，需要在数据库中按照主键id查询商品
    ProductInfo getById(int pid);

    //更新商品
    int update(ProductInfo info);

    //删除单个商品
    int delete(Integer pid);

    //批量删除商品
    int deleteBatch(String []ids);

    //多条件商品查询
    List<ProductInfo> selectCondition(ProductInfoVo vo);

////    多条件查询分页
//    public PageInfo splitPageVo(ProductInfoVo vo, int pageSize);

}
