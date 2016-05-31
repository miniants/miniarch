package cn.remex.wechat.models;

import cn.remex.db.rsql.model.ModelableImpl;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/10 0010.
 */
public class WeChatMsg extends ModelableImpl {
    private String toUserName;
    private String fromUserName;
    private long wxCreateTime;
    private String msgType;
    private String content;
    private String msgId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getWxCreateTime() {
        return wxCreateTime;
    }

    public void setWxCreateTime(long wxCreateTime) {
        this.wxCreateTime = wxCreateTime;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }
}
