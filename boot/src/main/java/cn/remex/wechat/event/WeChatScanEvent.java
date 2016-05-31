package cn.remex.wechat.event;

import cn.remex.core.RemexEvent;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/9 0009.
 */
public class WeChatScanEvent extends RemexEvent {
    private static final long serialVersionUID = -5467836967924859761L;
    private String openId;
    private String eventKey;
    private String tickets;
    public WeChatScanEvent(String source, String openId, String eventKey,String tickets) {
        super(source, openId, eventKey, tickets);
        this.openId = openId;
        this.eventKey = eventKey;
        this.tickets = tickets;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTickets() {
        return tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }
}
