package mika.pro.service.impl;

import com.google.common.cache.Cache;
import com.google.errorprone.annotations.Var;
import lombok.extern.slf4j.Slf4j;
import mika.pro.domain.req.WeixinQrCodeReq;
import mika.pro.domain.res.WeixinQrCodeRes;
import mika.pro.domain.res.WeixinTokenRes;
import mika.pro.domain.vo.WeixinTemplateMessageVO;
import mika.pro.service.ILoginService;
import mika.pro.service.weixin.IWeixinApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 微信登录实现类 实现对应接口的方法
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    //从配置文件中注入微信公众号的配置信息
    @Value("${weixin.config.app-id}")
    private String appid;//微信公众号的appid
    @Value("${weixin.config.app-secret}")
    private String appSecret;//微信公众号的appSecret
    @Value("${weixin.config.template_id}")
    private String template_id;//模板消息id

    //这里使用Cache来本地存储微信登录过程的AccessToken ticket openid等信息 当然也可以用redis来存储
    @Resource
    private Cache<String,String> weixinAccessToken;//将用户appid和AccessToken绑定
    @Resource
    private Cache<String,String> openidToken;//将二维码ticket和用户唯一标识openid绑定

    @Resource
    private IWeixinApiService weixinApiService;;

    /**
     * Description  获取二维码ticket
     * return
     */
    @Override
    public String createQrCodeTicket() throws IOException {
        //1.首先尝试根据appid获取AccessToken 看之前是否登录过或者AccessToken是否过期
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if(accessToken==null){//为空 则之前未登录过或者已过期
            //2.如果为空 则调用微信接口获取AccessToken
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            if (weixinTokenRes == null) {//如果没有获取到 则抛出异常
                throw new RuntimeException("Failed to get WeixinTokenRes");
            }
            accessToken =weixinTokenRes.getAccess_token();
            //获取到accessToken 将其和appid绑定
            weixinAccessToken.put(appid,accessToken );
        }
        log.info("获取到AccessToken：{}", accessToken);
        //3.根据accessToken和ticket获取二维码图片
        //先封装一下请求参数对象
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeReq.ActionNameTypeVO.QR_STR_SCENE.getCode())
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                .build())
                        .build())
                .build();
        //调用weixinApiService 传入参数获取ticket
        Call<WeixinQrCodeRes> call = weixinApiService.createQrCode(accessToken, weixinQrCodeReq);
        WeixinQrCodeRes weixinQrCodeRes = call.execute().body();
        if (weixinQrCodeRes == null) {//如果没有获取到二维码对象 则抛出异常
            throw new RuntimeException("Failed to get weixinQrCodeRes");
        }
        return weixinQrCodeRes.getTicket();//返回二维码ticket
    }

    /**
     * Description  根据二维码ticket获取用户openid 验证是否登录
     * @param: ticket
     */
    @Override
    public String checkLogin(String ticket) {
        return openidToken.getIfPresent(ticket);
    }

    /**
     * Description  登录后保存登录状态 并发送模板消息
     * @param: ticket 二维码ticket
     * @param: openid 用户唯一标识
     */
    @Override
    public void saveLoginState(String ticket, String openid) throws IOException {
        //1.先将ticket和openid绑定
        openidToken.put(ticket, openid);

        //2.然后尝试根据appid获取AccessToken 看之前是否登录过或者AccessToken是否过期
        String accessToken = weixinAccessToken.getIfPresent(appid);
        if(accessToken==null){//为空 则之前未登录过或已过期
            //如果为空 则调用微信接口获取AccessToken
            Call<WeixinTokenRes> call = weixinApiService.getToken("client_credential", appid, appSecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            if (weixinTokenRes == null) {//如果没有获取到 则抛出异常
                throw new RuntimeException("Failed to get WeixinTokenRes");
            }
            //获取到accessToken 将其和appid绑定
            weixinAccessToken.put(appid, weixinTokenRes.getAccess_token());
        }
        //4.给扫码登录用户发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);

        WeixinTemplateMessageVO templateMessageDTO = new WeixinTemplateMessageVO(openid, template_id);
        templateMessageDTO.setUrl("https://weixin.qq.com/");
        templateMessageDTO.setData(data);
        log.info("发送模板消息：{}", templateMessageDTO);
        Call<Void> call = weixinApiService.sendMessage(accessToken, templateMessageDTO);
        call.execute();
    }

}
