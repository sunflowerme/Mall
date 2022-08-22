package com.sunflower.service.impl;

import com.sunflower.mapper.ProductTypeMapper;
import com.sunflower.pojo.ProductType;
import com.sunflower.pojo.ProductTypeExample;
import com.sunflower.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    //业务逻辑层一定会有数据访问层的对象,然而对象的创建以及注入交给spring，使用注解@Autowired
    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
}
