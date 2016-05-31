package cn.remex.wechat.beans.paymch;

/**
 * 微信业务接口返回参数
 * Created by guoqi on 2016/3/23.
 */
public class WeChatPayResult {
    private String code;
    private String msg;
    private String prepay_id ;
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
