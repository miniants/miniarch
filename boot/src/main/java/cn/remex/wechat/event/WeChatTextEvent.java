package cn.remex.wechat.event;

import cn.remex.core.RemexEvent;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/10 0010.
 */
public class WeChatTextEvent extends RemexEvent {
    private static final long serialVersionUID = 1856737131248340181L;
    private String fromUserName;
    private String content;
    private long createTime;
    private String msgId;


    public WeChatTextEvent(String source, String fromUserName, String content,long createTime, String msgId) {
        super(source, fromUserName, content, createTime, msgId);
        this.fromUserName = fromUserName;
        this.content = content;
        this.createTime = createTime;
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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
}
