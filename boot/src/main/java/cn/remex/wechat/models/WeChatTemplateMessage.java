package cn.remex.wechat.models;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

/**
 * 微信模板信息发送信息
 * Created by guoqi on 2016/2/29.
 */
public class WeChatTemplateMessage extends ModelableImpl {
    private static final long serialVersionUID = -5627170460273312793L;
    private String touser; //微信 openId

    private String template_id; //模板id 在公众号获取

    private String url; //消息链接

    private String topcolor; //顶部颜色
    @Column(length = 2000)
    private String data;
    //微信返回信息
    private String errcode; //返回码
    private String errmsg; // 返回信息
    private String msgid;

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

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
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

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
