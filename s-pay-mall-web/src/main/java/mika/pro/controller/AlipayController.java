package mika.pro.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import mika.pro.common.constants.Constants;
import mika.pro.common.response.Response;
import mika.pro.domain.req.ShopCartReq;
import mika.pro.domain.res.PayOrderRes;
import mika.pro.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 通过支付宝沙箱实现订单支付
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/alipay/")//http://mikasa.natapp1.cc/api/v1/alipay
public class AlipayController {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Resource
    private OrderService orderService;

    //创建支付单 进行支付
    @PostMapping("create_pay_order")//http://mikasa.natapp1.cc/api/v1/alipay/create_pay_order
    public Response<String> createOrder(@RequestBody ShopCartReq shopCartReq) {
        try {
            log.info("商品下单：{}", shopCartReq);
            PayOrderRes payOrderRes = orderService.createOrder(shopCartReq);
            log.info("创建支付单成功：{}", payOrderRes);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(payOrderRes.getPayUrl())
                    .build();
        } catch (Exception e) {
            log.error("商品下单，根据商品ID创建支付单失败:{}", shopCartReq, e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    //支付后 支付宝回调接口
    @PostMapping("alipay_notify_url")//http://mikasa.natapp1.cc/api/v1/alipay/alipay_notify_url
    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        try {
            log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));

            if (!request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                return "false";
            }

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
            // 支付宝验签
            if (!checkSignature) {
                return "false";
            }

            // 验签通过
            log.info("支付回调，交易名称: {}", params.get("subject"));
            log.info("支付回调，交易状态: {}", params.get("trade_status"));
            log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
            log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
            log.info("支付回调，交易金额: {}", params.get("total_amount"));
            log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
            log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
            log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
            log.info("支付回调，支付回调，更新订单 {}", tradeNo);

            //支付成功 修改订单状态
            orderService.updateOrderPaySuccess(tradeNo);

            return "success";
        } catch (Exception e) {
            log.error("支付回调，处理失败", e);
            return "false";
        }
    }
}
