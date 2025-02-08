package mika.pro.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 订单详细表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {

    private Long id;
    private String userId;// 用户ID
    private String productId;// 商品ID
    private String productName;// 商品名称
    private String orderId;// 订单ID
    private Date orderTime;// 下单时间
    private BigDecimal totalAmount;// 订单总额
    private String status;// 订单状态
    private String payUrl;// 支付信息
    private Date payTime;// 支付时间
    private Date createTime;
    private Date updateTime;
}
