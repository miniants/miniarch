package cn.remex.wechat.models;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/10 0010.
 */
public class WeChatNotify extends ModelableImpl {
    private static final long serialVersionUID = 3833445818722642044L;
	private String msgType;
	private String fromUserName;
	private String toUserName;
	private long msgTime;
	private String event;
	@Column(length = 1024)
	private String eventKey;
	private String ticket;

	public WeChatNotify(String msgType,String fromUserName,String toUserName,long msgTime, String event, String eventKey,String ticket) {
		this.msgType = msgType;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.msgTime = msgTime;
		this.event = event;
		this.eventKey = eventKey;
		this.ticket = ticket;
	}

    public WeChatNotify() {
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
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

	public long getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(long msgTime) {
		this.msgTime = msgTime;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
}
