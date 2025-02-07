package mika.pro.domain.req;

import lombok.*;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 微信登录二维码时需要的请求参数 封装为对象
 * <a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">...</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeixinQrCodeReq {

    // 二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段不填则默认为2592000
    private int expire_seconds;
    /*
     二维码类型，
     QR_SCENE为临时的整型参数值，
     QR_STR_SCENE为临时的字符串参数值，
     QR_LIMIT_SCENE为永久的整型参数值，
     QR_LIMIT_STR_SCENE为永久的字符串参数值
     */
    private String action_name;
    /*
     二维码详细信息
     */
    private ActionInfo action_info;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ActionInfo {// 内部类：二维码详细信息
        Scene scene;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Scene {// 内部类：场景信息
            int scene_id;//场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
            String scene_str;//场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ActionNameTypeVO {// 二维码类型枚举
        QR_SCENE("QR_SCENE", "临时的整型参数值"),
        QR_STR_SCENE("QR_STR_SCENE", "临时的字符串参数值"),
        QR_LIMIT_SCENE("QR_LIMIT_SCENE", "永久的整型参数值"),
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE", "永久的字符串参数值");

        private String code;
        private String info;
    }

}
