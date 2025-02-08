package mika.pro.service;

import com.alipay.api.AlipayApiException;
import mika.pro.domain.po.OrderDetail;
import mika.pro.domain.req.ShopCartReq;
import mika.pro.domain.res.PayOrderRes;

import java.util.List;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 订单创建、支付服务接口
 */

public interface OrderService {
    /**
     * Description  接收购物车 创建一个支付订单返回
     * @param: shopCartReq
     */
    PayOrderRes createOrder(ShopCartReq shopCartReq) throws AlipayApiException;

    /**
     * Description  对订单创建支付单 并修改订单状态和调用支付宝沙箱生成支付地址返回
     * @param: orderDetail
     */
    OrderDetail doPayOrder(OrderDetail orderDetail) throws AlipayApiException;

    /**
     * Description  根据订单ID 修改订单状态为支付成功
     * @param: orderId
     */
    void updateOrderPaySuccess(String orderId);

    /**
     * Description  查询掉单
     * return java.util.List<java.lang.String>
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * Description 查询超时未支付订单
     * return java.util.List<java.lang.String>
     */
    List<String> queryTimeOutOrder();

    /**
     * Description  关闭超时订单
     * @param: orderId
     */
    void updateTimeOutOrder(String orderId);
}
