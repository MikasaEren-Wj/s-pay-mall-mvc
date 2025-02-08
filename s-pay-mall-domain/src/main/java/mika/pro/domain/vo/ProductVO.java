package mika.pro.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 商品信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVO {
    /** 商品ID */
    private String productId;
    /** 商品名称 */
    private String productName;
    /** 商品描述 */
    private String productDesc;
    /** 商品价格 */
    private BigDecimal price;
}
