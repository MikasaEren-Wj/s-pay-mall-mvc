package mika.pro.service;

import com.alipay.api.AlipayApiException;
import lombok.extern.slf4j.Slf4j;
import mika.pro.domain.req.ShopCartReq;
import mika.pro.domain.res.PayOrderRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * ErenMikasa
 * Date 2025/2/7
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Resource
    private OrderService orderService;

    @Test
    public void testOrder() throws AlipayApiException {
        ShopCartReq shopCartReq = ShopCartReq.builder()
                .userId("mikasa")
                .productId("1001")
                .build();
        PayOrderRes payOrderRes = orderService.createOrder(shopCartReq);
        log.info("创建订单：payOrderRes:{}", payOrderRes);
    }

    @Test
    public void testQuery() {
        List<String> strings = orderService.queryNoPayNotifyOrder();
        System.out.println(strings);
        List<String> strings1 = orderService.queryTimeOutOrder();
        System.out.println(strings1);
    }
}
