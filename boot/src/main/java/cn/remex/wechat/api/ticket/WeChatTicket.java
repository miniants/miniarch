package cn.remex.wechat.api.ticket;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.DateHelper;
import cn.remex.core.util.Judgment;
import cn.remex.wechat.beans.ticket.WeChatQrcodeTicket;
import cn.remex.wechat.beans.ticket.WeChatQrcodeTicketResult;
import cn.remex.wechat.beans.ticket.QrcodeUrl;
import cn.remex.wechat.config.WeChatAPIConfig;

import java.util.Date;

/**
 * Created by guoqi on 2016/4/12.
 */
public class WeChatTicket {



    private static final String QROCODE_EXPRIE_SECONDS = "200"; //微信临时有效默认时间


    /**
     * 微信 生成 永久字符串二维码
     * @param scene_str 二维码场景内容
     * @return
     */
    public static QrcodeUrl qrcodeLimitStrUrl(String scene_str){
        RemexConstants.logger.info("微信二维码内容："+scene_str);
        Assert.notNullAndEmpty(scene_str, ServiceCode.WEIXIN_ARGS_INVALID,"微信二维码内容！");
        WeChatQrcodeTicket weChatQrcodeTicket = new WeChatQrcodeTicket();
        weChatQrcodeTicket.setAction_name("QR_LIMIT_STR_SCENE");
        weChatQrcodeTicket.setScene_str(scene_str);
        WeChatQrcodeTicketResult weChatQrcodeTicketResult = WeChatTicketAPI.qrcodeTicket(weChatQrcodeTicket);
        return obtinQrcoeUrl(weChatQrcodeTicketResult);
    }

    /**
     *  微信生成 临时二维码
     * @param scene_id 只能是int的整数
     * @param expire_seconds 有效时间。 默认200s
     * @return
     */
    public static QrcodeUrl qrcodeSceneUrl(String scene_id,String expire_seconds){
        RemexConstants.logger.info("微信二维码内容："+scene_id);
        Assert.notNullAndEmpty(scene_id, ServiceCode.WEIXIN_ARGS_INVALID,"微信二维码内容！");
        WeChatQrcodeTicket weChatQrcodeTicket = new WeChatQrcodeTicket();
        weChatQrcodeTicket.setAction_name("QR_SCENE");
        weChatQrcodeTicket.setScene_id(scene_id);
        weChatQrcodeTicket.setExpire_seconds(Judgment.nullOrBlank(expire_seconds)?QROCODE_EXPRIE_SECONDS:expire_seconds);
        WeChatQrcodeTicketResult weChatQrcodeTicketResult = WeChatTicketAPI.qrcodeTicket(weChatQrcodeTicket);
        return obtinQrcoeUrl(weChatQrcodeTicketResult);
    }



    private static QrcodeUrl obtinQrcoeUrl(WeChatQrcodeTicketResult weChatQrcodeTicketResult){
        if(Judgment.nullOrBlank(weChatQrcodeTicketResult.getErrcode())){
            QrcodeUrl qrcodeUrl = new QrcodeUrl();
            qrcodeUrl.setUrl(weChatQrcodeTicketResult.getUrl());
            qrcodeUrl.setExpire_seconds(weChatQrcodeTicketResult.getExpire_seconds());
            qrcodeUrl.setTicketUrl(WeChatAPIConfig.showqrcode + "ticket=" + weChatQrcodeTicketResult.getTicket());
            qrcodeUrl.setTicket(weChatQrcodeTicketResult.getTicket());
            qrcodeUrl.setCreateTime(new Date().getTime());
            RemexConstants.logger.info("获取的二维码参数：url="+weChatQrcodeTicketResult.getUrl()+";ticketUrl="+qrcodeUrl.getTicketUrl()+";ticket="+qrcodeUrl.getTicket());
            return qrcodeUrl;
        }
        return null;
    }

}
