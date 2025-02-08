package mika.pro.listener;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ErenMikasa
 * Date 2025/2/8
 * 监听支付成功回调消息
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Subscribe
    public void onMessage(String paySuccessOrder) {
        log.info("订单支付成功：{}，收到支付成功消息，可以做接下来的事情，如；发货、充值、开户员、返利", paySuccessOrder);
    }
}
