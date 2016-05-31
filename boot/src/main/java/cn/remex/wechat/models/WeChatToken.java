package cn.remex.wechat.models;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

/**
 * Created by guoqi on 2016/3/21.
 */
public class WeChatToken extends ModelableImpl {

    private static final long serialVersionUID = -215316555767882962L;
    @Column(length = 50)
    private String appid;
    @Column(length = 200)
    private String accessToken;
    private String accessTime; // 时间
    private boolean valid;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
