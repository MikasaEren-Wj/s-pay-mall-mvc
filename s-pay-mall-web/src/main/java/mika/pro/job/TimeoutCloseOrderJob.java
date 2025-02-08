package mika.pro.job;

import lombok.extern.slf4j.Slf4j;
import mika.pro.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * ErenMikasa
 * Date 2025/2/8
 * 定时任务：查询订单支付超时未支付的订单，然后修改订单状态为超时
 * 默认超时30分钟未支付为超时订单 进行关闭
 */
@Slf4j
@Component
public class TimeoutCloseOrderJob {
    @Resource
    private OrderService orderService;

    @Scheduled(cron = "0 0/10 * * * ?")// 每 10 分钟执行一次
    public void exec() {
        try{
            log.info("开始执行定时任务：查询订单支付超时未支付的订单，然后修改订单状态为超时");
            List<String> timeOutOrder=orderService.queryTimeOutOrder();//查询超时未支付订单
            if(timeOutOrder==null || timeOutOrder.isEmpty()){
                log.info("没有超时未支付的订单");
                return ;
            }
            //否则存在超时订单
            log.info("存在超时未支付的订单:{}",timeOutOrder);
            for (String orderId : timeOutOrder) {//修改订单状态为关闭
                orderService.updateTimeOutOrder(orderId);
            }
        } catch(Exception e){
            log.error("定时任务，超时30分钟订单关闭失败", e);
        }

    }
}
