package mika.pro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 配置类读取yml中配置的支付宝沙箱信息
 */
@Data
@ConfigurationProperties(prefix = "alipay", ignoreInvalidFields = true)
public class AlipayConfigProperties {
    // 「沙箱环境」应用ID - 您的APPID，收款账号既是你的APPID对应支付宝账号。获取地址；https://open.alipay.com/develop/sandbox/app
    private String app_id;
    // 「沙箱环境」商户私钥，你的PKCS8格式RSA2私钥
    private String merchant_private_key;
    // 「沙箱环境」支付宝公钥
    private String alipay_public_key;
    // 「沙箱环境」服务器异步通知页面路径
    private String notify_url;
    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String return_url;
    // 「沙箱环境」
    private String gatewayUrl;
    // 签名方式
    private String sign_type;
    // 字符编码格式
    private String charset;
    // 传输格式
    private String format;
}
