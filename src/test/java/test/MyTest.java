package test;

import com.sunflower.mapper.ProductInfoMapper;
import com.sunflower.pojo.ProductInfo;
import com.sunflower.pojo.vo.ProductInfoVo;
import com.sunflower.utils.MD5Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * 密文测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class MyTest {

    @Test
    public void testMD5() {
        String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }

    @Autowired
    ProductInfoMapper mapper;

    @Test
    public void testSelectCondition(){
        ProductInfoVo vo = new ProductInfoVo();
        vo.setPname("4");
        vo.setTypeid(3);
        vo.setLprice(3000.0);
        vo.setHprice(3999.0);
        List<ProductInfo> list = mapper.selectCondition(vo);
        list.forEach(productInfo -> System.out.println(productInfo));

    }

}
