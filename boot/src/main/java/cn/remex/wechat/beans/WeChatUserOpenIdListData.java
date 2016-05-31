package cn.remex.wechat.beans;

import java.util.List;

/**
 * 微信获取用户信息 返回信息
 */
public class WeChatUserOpenIdListData{
    private List<String> openid;//关注该公众账号的总用户数

    public List<String> getOpenid() {
        return openid;
    }

    public void setOpenid(List<String> openid) {
        this.openid = openid;
    }
}
