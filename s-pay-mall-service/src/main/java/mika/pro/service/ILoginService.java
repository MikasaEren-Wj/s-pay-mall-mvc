package mika.pro.service;

import java.io.IOException;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 扫码微信登录服务
 */

public interface ILoginService {

    String createQrCodeTicket() throws IOException;// 创建二维码凭证ticket

    String checkLogin(String ticket);// 检测登录

    //保存登录状态 只有ticket+openid才能标识唯一用户是否登录
    void saveLoginState(String ticket, String openid) throws IOException;
}
