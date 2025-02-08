package mika.pro.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 创建订单的请求参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartReq {

    private String userId;//下单用户ID
    private String productId;//下单商品ID
}
