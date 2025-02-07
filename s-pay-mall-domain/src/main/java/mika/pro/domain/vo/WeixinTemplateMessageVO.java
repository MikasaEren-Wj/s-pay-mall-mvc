package mika.pro.domain.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * ErenMikasa
 * Date 2025/2/6
 * 微信模板消息
 * <a href="https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index">...</a>
 */
public class WeixinTemplateMessageVO {
    private String touser = "OPENID";// 接收者（用户）的 openid
    private String template_id = "TEMPLATE_ID";// 所需下发的模板消息id
    private String url = "https://weixin.qq.com/";// 点击模版卡片后的跳转地址
    private Map<String, Map<String,String>> data=new HashMap<>();// 模版内容，不填则下发空模版

    public WeixinTemplateMessageVO(String touser,String template_id){
        this.touser = touser;
        this.template_id = template_id;
    }

    public void put(TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }

    public static void put(Map<String, Map<String, String>> data, TemplateKey key, String value) {
        data.put(key.getCode(), new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }


    public enum TemplateKey {
        USER("user","用户ID");//枚举常量

        private String code;
        private String desc;

        TemplateKey(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

}
