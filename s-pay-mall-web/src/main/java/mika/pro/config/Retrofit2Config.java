package mika.pro.config;

import lombok.extern.slf4j.Slf4j;
import mika.pro.service.weixin.IWeixinApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
/**
 * Description  对框架Retrofit2进行配置
 */
@Slf4j
@Configuration
public class Retrofit2Config {

    // 微信API请求地址首部https://api.weixin.qq.com/....
    private static final String BASE_URL = "https://api.weixin.qq.com/";

    //创建并返回一个Retrofit实例，设置基础URL为微信API地址，并添加JSON转换工厂
    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create()).build();
    }

    //使用已创建的Retrofit实例生成微信API服务接口实例。实例化的过程是代理操作
    @Bean
    public IWeixinApiService weixinApiService(Retrofit retrofit) {
        return retrofit.create(IWeixinApiService.class);
    }

}
