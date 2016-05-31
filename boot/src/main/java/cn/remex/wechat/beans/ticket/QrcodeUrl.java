package cn.remex.wechat.beans.ticket;

/**
 * Created by guoqi on 2016/3/1.
 */
public class QrcodeUrl {
    private String ticketUrl; // ticketurl  凭借此url可以在有效时间内换取二维码。
    private String expire_seconds; //有效时间
    private String url; // 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    private String ticket;
    private long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
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
