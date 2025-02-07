package mika.pro.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 微信登录中所需的AccessToken 封装为对象 作为返回给客户的结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeixinTokenRes {
    //文档：https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html
    //正常情况返回前两个信息
    private String access_token;// 获取到的凭证
    private String expires_in;// 凭证有效时间，单位：秒
    // 异常情况返回错误码和错误信息
    private String errcode;
    private String errmsg;
}
