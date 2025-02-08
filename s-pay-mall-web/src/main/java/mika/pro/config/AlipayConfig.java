package mika.pro.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 启动AlipayConfigProperties类 将AlipayClient注册为Bean对象到Spring容器中
 */
@Configuration
@EnableConfigurationProperties(AlipayConfigProperties.class)
public class AlipayConfig {

    @Bean("alipayClient")//注册为Bean对象
    public AlipayClient alipayClient(AlipayConfigProperties properties) {
        return new DefaultAlipayClient(
                properties.getGatewayUrl(),
                properties.getApp_id(),
                properties.getMerchant_private_key(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getAlipay_public_key(),
                properties.getSign_type());
    }
}
