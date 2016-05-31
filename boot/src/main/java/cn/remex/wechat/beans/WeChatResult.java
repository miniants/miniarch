package cn.remex.wechat.beans;

/**
 * 微信返回 系统字段
 * Created by guoqi on 2016/3/1.
 */
public class WeChatResult {

    private String errcode;
    private String errmsg;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
