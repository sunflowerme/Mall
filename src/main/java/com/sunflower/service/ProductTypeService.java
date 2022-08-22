package com.sunflower.service;

import com.sunflower.pojo.ProductType;

import java.util.List;

public interface ProductTypeService {

    //查询商品的所有类别
    List<ProductType> getAll();

}
