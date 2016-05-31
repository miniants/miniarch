package cn.remex.wechat.beans;

/**
 * 微信获取用户信息 返回信息
 * Created by guoqi on 2016/2/26.
 */
public class WeChatUserOpenIdList extends WeChatResult{
    private String total;//关注该公众账号的总用户数
    private String count;//拉取的OPENID个数，最大值为10000
    private WeChatUserOpenIdListData data;//	列表数据，OPENID的列表
    private String next_openid;//拉取列表的最后一个用户的OPENID

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public WeChatUserOpenIdListData getData() {
        return data;
    }

    public void setData(WeChatUserOpenIdListData data) {
        this.data = data;
    }

    public String getNext_openid() {
        return next_openid;
    }

    public void setNext_openid(String next_openid) {
        this.next_openid = next_openid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
