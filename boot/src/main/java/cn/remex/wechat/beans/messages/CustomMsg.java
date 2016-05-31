package cn.remex.wechat.beans.messages;

/**
 * 微信模板具体
 * Created by guoqi on 2016/2/29.
 */
public class CustomMsg {
    private String touser; //值
    private String msgtype; //消息类型 text image voice video music news mpnews  wxcard响应节点不能为空

    private CustomMsgData text; //发送文本消息
    private CustomMsgData image; //发送图片消息
    private CustomMsgData voice; //发送语音消息
    private CustomMsgData video; //发送视频消息
    private CustomMsgData music; //发送音乐消息
    private CustomMsgData news; //发送图文消息（点击跳转到外链） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
    private CustomMsgData mpnews; //发送图文消息（点击跳转到图文消息页面） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
    private CustomMsgData wxcard; //发送卡券


    public CustomMsgData getImage() {
        return image;
    }

    public void setImage(CustomMsgData image) {
        this.image = image;
    }

    public CustomMsgData getMpnews() {
        return mpnews;
    }

    public void setMpnews(CustomMsgData mpnews) {
        this.mpnews = mpnews;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public CustomMsgData getMusic() {
        return music;
    }

    public void setMusic(CustomMsgData music) {
        this.music = music;
    }

    public CustomMsgData getNews() {
        return news;
    }

    public void setNews(CustomMsgData news) {
        this.news = news;
    }

    public CustomMsgData getText() {
        return text;
    }

    public void setText(CustomMsgData text) {
        this.text = text;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public CustomMsgData getVideo() {
        return video;
    }

    public void setVideo(CustomMsgData video) {
        this.video = video;
    }

    public CustomMsgData getVoice() {
        return voice;
    }

    public void setVoice(CustomMsgData voice) {
        this.voice = voice;
    }

    public CustomMsgData getWxcard() {
        return wxcard;
    }

    public void setWxcard(CustomMsgData wxcard) {
        this.wxcard = wxcard;
    }
}
