package cn.remex.wechat.event;

import cn.remex.core.RemexEvent;
import org.dom4j.Document;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/10 0010.
 */
public class WeChatEvent extends RemexEvent {

    private static final long serialVersionUID = -8075223158399010033L;
    private final String msgType;
    private final String fromUserName;
    private final String toUserName;
    private final long createTime;
    private final String event;
    private final String eventKey;
	private final String ticket;
    private final Document content;

    public WeChatEvent(String source,  String msgType,String fromUserName,String toUserName,long createTime, String event, String eventKey,String ticket, Document content) {
        super(source);
        this.msgType = msgType;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.createTime = createTime;
        this.event = event;
        this.eventKey = eventKey;
	    this.ticket = ticket;
        this.content = content;
    }

    public Document getContent() {
        return content;
    }

    public String getEvent() {
        return event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public String getMsgType() {
        return msgType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getToUserName() {
        return toUserName;
    }

	public String getTicket() {
		return ticket;
	}
}
