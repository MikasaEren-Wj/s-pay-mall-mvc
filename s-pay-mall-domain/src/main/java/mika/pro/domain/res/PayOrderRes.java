package mika.pro.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mika.pro.common.constants.Constants;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 订单支付后返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayOrderRes {
    private String userId;//支付用户id
    private String orderId;//订单id
    private String payUrl;//支付链接
    private Constants.OrderStatusEnum orderStatusEnum;//订单状态
}
