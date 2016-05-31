package cn.remex.wechat.beans.messages;

/**
 * 微信模板具体
 * Created by guoqi on 2016/2/29.
 *
 * 消息类型 text image voice video music news mpnews  wxcard 响应节点不能为空
 */
public class CustomMsgData {
    public CustomMsgData(String content) {
        this.content = content;
    }

    private String content; //text's 文本内容
    private String media_id; //image's voice's video's mpnews's MEDIA_ID
    private String thumb_media_id; //voice's music's
    private String title; //voice's music's
    private String description; //voice's music's news's
    private String musicurl; //music's
    private String hqmusicurl; //music's
    private String url; //new's
    private String picurl; //new's
    private String card_id; //wxcard's
    private String card_ext; //wxcard's

    public String getCard_ext() {
        return card_ext;
    }

    public void setCard_ext(String card_ext) {
        this.card_ext = card_ext;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHqmusicurl() {
        return hqmusicurl;
    }

    public void setHqmusicurl(String hqmusicurl) {
        this.hqmusicurl = hqmusicurl;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getMusicurl() {
        return musicurl;
    }

    public void setMusicurl(String musicurl) {
        this.musicurl = musicurl;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getThumb_media_id() {
        return thumb_media_id;
    }

    public void setThumb_media_id(String thumb_media_id) {
        this.thumb_media_id = thumb_media_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
