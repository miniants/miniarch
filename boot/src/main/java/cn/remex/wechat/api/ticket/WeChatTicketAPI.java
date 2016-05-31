package cn.remex.wechat.api.ticket;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.util.Assert;
import cn.remex.core.util.JsonHelper;
import cn.remex.core.util.Judgment;
import cn.remex.wechat.beans.ticket.WeChatQrcodeTicket;
import cn.remex.wechat.beans.ticket.WeChatQrcodeTicketResult;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;

import java.util.HashMap;


/**
 * Created by guoqi on 2016/3/1.
 */
class WeChatTicketAPI {
    /**
     * 、为了满足用户渠道推广分析和用户帐号绑定等场景的需要，公众平台提供了生成带参数二维码的接口。使用该接口可以获得多个带不同场景值的二维码，用户扫描后，公众号可以接收到事件推送。
     * 1、临时二维码，是有过期时间的，最长可以设置为在二维码生成后的30天（即2592000秒）后过期，但能够生成较多数量。临时二维码主要用于帐号绑定等不要求二维码永久保存的业务场景
     * 2、永久二维码，是无过期时间的，但数量较少（目前为最多10万个）。永久二维码主要用于适用于帐号绑定、用户来源统计等场景。
     * 用户扫描带场景值二维码时，可能推送以下两种事件：
     * 如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
     * 如果用户已经关注公众号，在用户扫描后会自动进入会话，微信也会将带场景值扫描事件推送给开发者。
     * @param weChatQrcodeTicket
     * @return
     */
    static WeChatQrcodeTicketResult qrcodeTicket(WeChatQrcodeTicket weChatQrcodeTicket){
        Assert.notNullAndEmpty(WeChatConfig.getAccess_token(), ServiceCode.WEIXIN_ARGS_INVALID, "access_token 不能为空~！");
        Assert.notNullAndEmpty(weChatQrcodeTicket, ServiceCode.WEIXIN_ARGS_INVALID, "weChatQrcodeTicket 不能为空~！");
        RemexConstants.logger.info("微信->生成带参数的二维码  qrcodeTicket");
        HashMap<String,Object> map = new HashMap<>();
        HashMap<String,String> map2 = new HashMap<>();
        if(!Judgment.nullOrBlank(weChatQrcodeTicket.getExpire_seconds())){
            map.put("expire_seconds",weChatQrcodeTicket.getExpire_seconds());
        }
        if(!Judgment.nullOrBlank(weChatQrcodeTicket.getScene_str())){
            map2.put("scene_str",weChatQrcodeTicket.getScene_str());
        }
        if(!Judgment.nullOrBlank(weChatQrcodeTicket.getScene_id())){
            map2.put("scene_id",weChatQrcodeTicket.getScene_id());
        }
        map.put("action_name",weChatQrcodeTicket.getAction_name());
        HashMap<String,Object> map3 = new HashMap<>();
        map3.put("scene",map2);
        map.put("action_info",map3);
        String json =  JsonHelper.toJsonString(map);
        RemexConstants.logger.info("微信->生成带参数的二维码  发送参数："+json);
        String result = HttpHelper.sendXml(WeChatAPIConfig.qrcodeTicketCreate + "&access_token=" + WeChatConfig.getAccess_token(),json);
        RemexConstants.logger.info("微信->生成带参数的二维码   接收参数："+result);
        return JsonHelper.toJavaObject(result,WeChatQrcodeTicketResult.class);
    }

}
