package cn.remex.wechat.beans.ticket;

import cn.remex.wechat.beans.WeChatResult;


/**
 * 微信生成带参数的二维码
 * Created by guoqi on 2016/3/1.
 */
public class WeChatQrcodeTicketResult extends WeChatResult {
    private String ticket;
    private String expire_seconds;
    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(String expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
