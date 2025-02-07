package mika.pro.controller;

import lombok.extern.slf4j.Slf4j;
import mika.pro.common.constants.Constants;
import mika.pro.common.response.Response;
import mika.pro.service.ILoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 微信扫码登录接口
 * <a href="http://mikasa.natapp1.cc/api/v1/login/">...</a>..
 */
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/login/")
public class LoginController {

    @Resource
    private ILoginService loginService;

    /**
     * Description  请求微信公众号获取二维码ticket
     * return
     */
    @GetMapping("weixin_qrcode_ticket")
    public Response<String> weixinQrCodeTicket() {
        try {
            //1.获取二维码ticket
            String ticket = loginService.createQrCodeTicket();
            log.info("获取二维码ticket成功：{}",ticket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(ticket)
                    .build();
        } catch (Exception e) {
            log.error("获取二维码ticket失败",e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * Description  登录校验
     * @param: ticket
     */
    @GetMapping("check_login")
    public Response<String> checkLogin(@RequestParam String ticket){
        try {
            //根据二维码获取用户openid
            String openid = loginService.checkLogin(ticket);
            if(StringUtils.isNoneBlank(openid)){//不为空 则已经登录
                log.info("微信扫码已登录，ticket:{},openid:{}",ticket,openid);
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .info(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(openid)
                        .build();
            }
            //否则未登录
            log.info("微信扫码未登录，ticket:{}",ticket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.NO_LOGIN.getCode())
                    .info(Constants.ResponseCode.NO_LOGIN.getInfo())
                    .build();
        }catch (Exception e){
            log.error("扫码登录失败 ticket:{}", ticket, e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
