package mika.pro.service.rpc;


import mika.pro.domain.vo.ProductVO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 商品的一些远程调用服务
 */
@Service
public class ProductRPC {

    /**
     * Description  根据商品id获取商品信息 模拟远程调用 便于测试 直接创建一个商品
     * @param: productId
     */
    public ProductVO queryProductByProductId(String productId){
        return ProductVO.builder()
                .productId(productId)
                .productName("测试商品"+ RandomStringUtils.randomNumeric(4))
                .price(BigDecimal.valueOf(1000))
                .productDesc("测试商品ooooo")
                .build();
    }
}
