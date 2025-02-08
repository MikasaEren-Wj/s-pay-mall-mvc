package mika.pro.job;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import mika.pro.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * ErenMikasa
 * Date 2025/2/8
 * 定时任务扫描库表数据，查询订单状态为等待支付pay_wait,且30分钟内未支付的订单(所谓的掉单)
 * 检测支付宝是否正确接收处理这些支付单，若支付完成则变更支付状态。
 */
@Slf4j
@Component()
public class NoPayNotifyOrderJob {

    @Resource
    private OrderService orderService;

    @Resource
    private AlipayClient alipayClient;

    @Scheduled(cron = "0 0/1 * * * ?")  // 使用 @Scheduled 注解定时执行该方法，这里是每1分钟执行一次
    public void exec() {
        try {
            log.info("询订单状态为等待支付pay_wait,且30分钟内未支付，检测支付宝是否正确接收处理这些支付单");
            // 查询未处理支付通知的订单ID列表
            List<String> orderIds = orderService.queryNoPayNotifyOrder();

            // 如果没有未支付的订单，则直接返回，不做任何操作
            if (orderIds==null || orderIds.isEmpty()){
                log.info("暂时不存在30分钟内未支付的订单");
                return;
            }

            // 遍历未支付的订单ID
            for (String orderId : orderIds) {
                // 创建支付宝支付查询请求
                AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

                // 设置请求参数：订单号
                AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
                bizModel.setOutTradeNo(orderId);  // 设置查询的订单ID（商户订单号）
                request.setBizModel(bizModel);

                // 执行支付宝查询请求
                AlipayTradeQueryResponse alipayTradeQueryResponse = alipayClient.execute(request);

                // 获取返回的状态码
                String code = alipayTradeQueryResponse.getCode();

                // 如果支付状态为 10000，表示支付成功 由支付宝给出
                if ("10000".equals(code)) {
                    // 支付成功，调用订单服务更新订单支付状态
                    orderService.updateOrderPaySuccess(orderId);
                }
            }
        } catch (Exception e) {
            // 捕获异常并记录日志
            log.error("检测未接收到或未正确处理的支付回调通知失败", e);
        }
    }
}

