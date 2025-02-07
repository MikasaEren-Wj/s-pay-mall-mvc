package mika.pro.service.weixin;

import mika.pro.domain.req.WeixinQrCodeReq;
import mika.pro.domain.res.WeixinQrCodeRes;
import mika.pro.domain.res.WeixinTokenRes;
import mika.pro.domain.vo.WeixinTemplateMessageVO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 微信登录过程中的需要的接口服务  使用框架retrofit2
 */

public interface IWeixinApiService {

    /**
     * Description  获取Access token
     * <a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">...</a>
     * @param: grant_type  获取access_token填写client_credential
     * @param: appid  第三方用户唯一凭证
     * @param: secret  第三方用户唯一凭证密钥，即appSecret
     * return retrofit2.Call<mika.pro.domain.res.WeixinTokenRes>
     */
    @GET("cgi-bin/token")//https://api.weixin.qq.com/cgi-bin/token
    Call<WeixinTokenRes> getToken(@Query("grant_type") String grant_type,
                                  @Query("appid") String appid,
                                  @Query("secret") String appSecret);

    /**
     * Description  获取二维码ticket
     * @param: access_token
     * @param: expire_seconds
     * @param: action_name
     * @param: action_info
     * return retrofit2.Call<mika.pro.domain.res.WeixinQrCodeRes>
     */
    @POST("cgi-bin/qrcode/create")//https://api.weixin.qq.com/cgi-bin/token
    Call<WeixinQrCodeRes> createQrCode(@Query("access_token") String access_token,
                                       @Body WeixinQrCodeReq weixinQrCodeReq);

    /**
     * 发送微信公众号模板消息
     * <a href="https://mp.weixin.qq.com/debug/cgi-bin/readtmpl?t=tmplmsg/faq_tmpl">...</a>
     * @param accessToken  getToken 获取的 token 信息
     * @param weixinTemplateMessageVO 微信模板消息对象
     * @return 应答结果
     */
    @POST("cgi-bin/message/template/send")//https://api.weixin.qq.com/cgi-bin/message/template/send
    Call<Void> sendMessage(@Query("access_token") String accessToken, @Body WeixinTemplateMessageVO weixinTemplateMessageVO);

}
