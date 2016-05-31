package cn.remex.wechat.event;

import cn.remex.core.RemexEvent;
import cn.remex.wechat.beans.paymch.PayNotify;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/10 0010.
 */
public class WeChatPayNotifyEvent extends RemexEvent {
    private static final long serialVersionUID = 7981977769240987596L;
    private PayNotify payNotify;

    public WeChatPayNotifyEvent(String source, PayNotify payNotify) {
        super(source, payNotify);
        this.payNotify = payNotify;
    }

    public PayNotify getPayNotify() {
        return payNotify;
    }

    public void setPayNotify(PayNotify payNotify) {
        this.payNotify = payNotify;
    }
}
