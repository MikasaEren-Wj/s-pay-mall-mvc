package mika.pro.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import mika.pro.common.constants.Constants;
import mika.pro.dao.OrderDao;
import mika.pro.domain.po.OrderDetail;
import mika.pro.domain.req.ShopCartReq;
import mika.pro.domain.res.PayOrderRes;
import mika.pro.domain.vo.ProductVO;
import mika.pro.service.OrderService;
import mika.pro.service.rpc.ProductRPC;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 订单创建、支付服务实现类
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Value("${alipay.notify_url}")
    private String notifyUrl;//服务器异步通知页面路径
    @Value("${alipay.return_url}")
    private String returnUrl;//支付宝回调地址
    @Resource
    private AlipayClient alipayClient;
    @Resource
    private EventBus eventBus;//事件总线

    @Resource
    private OrderDao orderDao;

    @Resource
    private ProductRPC productRPC;

    /**
     * Description  根据用户id和商品id创建支付订单
     *
     * @param: shopCartReq 请求参数包含用户id和商品id
     * return
     */
    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) throws AlipayApiException {

        //1.首先查询当前用户是否存在未支付的订单
        OrderDetail orderUnPay = orderDao.queryOrderUnPay(OrderDetail.builder()
                .userId(shopCartReq.getUserId())
                .productId(shopCartReq.getProductId())
                .build());
        if (orderUnPay != null
                && Constants.OrderStatusEnum.PAY_WAIT.getCode().equals(orderUnPay.getStatus())) {
            //1.1如果订单已创建支付单 处于等待支付状态  则直接返回 去支付
            log.info("支付单已创建 处于等待支付状态:{}", orderUnPay);
            return PayOrderRes.builder()
                    .orderId(orderUnPay.getOrderId())
                    .payUrl(orderUnPay.getPayUrl())
                    .build();
        }
        if (orderUnPay != null
                && Constants.OrderStatusEnum.CREATE.getCode().equals(orderUnPay.getStatus())) {
            //1.2 订单已经有了 但还没有创建支付单 则需要创建支付单
            OrderDetail payOrder = doPayOrder(orderUnPay);
            log.info("创建好支付单:{}", payOrder);
            return PayOrderRes.builder()
                    .orderId(payOrder.getOrderId())
                    .payUrl(payOrder.getPayUrl())
                    .build();
        }

        //2.当前商品 用户并没有创建订单 则先创建订单
        log.info("当前商品 用户并没有创建订单，先进行订单创建");
        //远程调用框架获取商品信息
        ProductVO productVO = productRPC.queryProductByProductId(shopCartReq.getProductId());
        String orderId = RandomStringUtils.randomNumeric(16);//随机生成一个订单号
        OrderDetail buildOrder = OrderDetail.builder()
                .userId(shopCartReq.getUserId())
                .productId(shopCartReq.getProductId())
                .productName(productVO.getProductName())
                .orderId(orderId)
                .orderTime(new Date())
                .totalAmount(productVO.getPrice())
                .status(Constants.OrderStatusEnum.CREATE.getCode())
                .build();
        orderDao.insertOrder(buildOrder);//插入数据库
        //3.再创建支付单
        buildOrder=doPayOrder(buildOrder);

        return PayOrderRes.builder()
                .orderId(buildOrder.getOrderId())
                .payUrl(buildOrder.getPayUrl())
                .build();
    }

    /**
     * Description  创建支付单 对接支付宝沙箱支付
     *
     * @param: orderDetail
     */
    @Override
    public OrderDetail doPayOrder(OrderDetail orderDetail) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderDetail.getOrderId());
        bizContent.put("total_amount", orderDetail.getTotalAmount().toString());
        bizContent.put("subject", orderDetail.getProductName());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        log.info("支付宝支付表单:{}", form);
        OrderDetail buildPayOrder = OrderDetail.builder()
                .orderId(orderDetail.getOrderId())
                .payUrl(form)
                .status(Constants.OrderStatusEnum.PAY_WAIT.getCode())
                .build();
        orderDao.updateOrder(buildPayOrder);//同时去数据库中修改数据
        return buildPayOrder;
    }

    /**
     * Description  根据订单ID修改订单状态为支付成功
     * @param: orderId
     */
    @Override
    public void updateOrderPaySuccess(String orderId) {
        OrderDetail updateOrderSuccess = OrderDetail.builder()
                .orderId(orderId)
                .status(Constants.OrderStatusEnum.PAY_SUCCESS.getCode())
                .payTime(new Date())
                .build();
        orderDao.updateOrder(updateOrderSuccess);//修改订单状态

        eventBus.post(JSON.toJSONString(updateOrderSuccess));//发布事件 模拟MQ
    }

    /**
     * Description  查询掉单：订单状态为等待支付pay_wait,且创建时间已经超过1分钟的订单
     * return java.util.List<java.lang.String>
     */
    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderDao.queryNoPayNotifyOrder();
    }

    /**
     * Description  查询超时未支付订单
     * return java.util.List<java.lang.String>
     */
    @Override
    public List<String> queryTimeOutOrder() {
        return orderDao.queryTimeOutOrder();
    }

    /**
     * Description  关闭超时订单
     * @param: orderId
     * return void
     */
    @Override
    public void updateTimeOutOrder(String orderId) {
        OrderDetail timeOutOrder = OrderDetail.builder()
                .orderId(orderId)
                .status(Constants.OrderStatusEnum.CLOSE.getCode())
                .payTime(new Date())
                .build();
        orderDao.updateOrder(timeOutOrder);
    }
}
