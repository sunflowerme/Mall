package com.sunflower.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunflower.mapper.ProductInfoMapper;
import com.sunflower.pojo.ProductInfo;
import com.sunflower.pojo.ProductInfoExample;
import com.sunflower.pojo.vo.ProductInfoVo;
import com.sunflower.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    //业务逻辑层一定会有数据访问层对象
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample()); //此时没有条件，就直接new一个空的，不用追加任何的条件
    }

    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //首先分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);
        //进行pageInfo的数据封装
        //进行有条件的查询操作，必须要创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序，按照注解降序进行排序
        //SELECT * FROM product_info ORDER BY p_id DESC
        example.setOrderByClause("p_id DESC");
        //设置完排序后，取集合，但是需要注意一定要在取集合之前，设置PageHelper.startPage(pageNum,pageSize);
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //将查到的集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(int pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(Integer pid) {
        int num = 0;
        try {
            num = productInfoMapper.deleteByPrimaryKey(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

}
