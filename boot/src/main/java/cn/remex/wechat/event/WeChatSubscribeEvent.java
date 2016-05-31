package cn.remex.wechat.event;

import cn.remex.core.RemexEvent;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/9 0009.
 */
public class WeChatSubscribeEvent extends RemexEvent {
    private static final long serialVersionUID = -3406052972273516105L;
    private String openId;
    private String sceneKey;
    public WeChatSubscribeEvent(String source, String openId, String sceneKey) {
        super(source, openId, sceneKey);
        this.openId = openId;
        this.sceneKey = sceneKey;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSceneKey() {
        return sceneKey;
    }

    public void setSceneKey(String sceneKey) {
        this.sceneKey = sceneKey;
    }
}
