package mika.pro.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 获取带参数的二维码 即二维码对象 作为返回结果
 * <a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">...</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeixinQrCodeRes {
    // ticket 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
    private String ticket;
    // 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    private String url;
    // 二维码有效时间，以秒为单位。 最大不超过2592000（即30天）
    private Long expire_seconds;
}
