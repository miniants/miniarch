package cn.remex.wechat.api;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.util.Assert;
import cn.remex.core.util.JsonHelper;
import cn.remex.db.ContainerFactory;
import cn.remex.wechat.beans.messages.CustomMsg;
import cn.remex.wechat.beans.messages.CustomMsgData;
import cn.remex.wechat.beans.messages.TemplateMessage;
import cn.remex.wechat.beans.messages.WeChactMessageResult;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;
import cn.remex.wechat.models.WeChatMsg;
import cn.remex.wechat.models.WeChatTemplateMessage;


/**
 * 微信消息接口
 * Created by guoqi on 2016/2/29.
 */
public class WeChatMessageAPI {
    /**
     * 微信模板消息接口
     * @param templateMessage
     * @return
     */
    public static boolean messageTemplateSend(TemplateMessage templateMessage){
        String access_token = WeChatConfig.getAccess_token();
        Assert.notNull(access_token,  ServiceCode.FAIL,"null access_token");
        Assert.notNull(templateMessage,  ServiceCode.FAIL,"empty templateMessage");

        WeChatTemplateMessage weChatTemplateMessage = new WeChatTemplateMessage();
        weChatTemplateMessage.setTouser(templateMessage.getTouser());
        weChatTemplateMessage.setUrl(templateMessage.getUrl());
        weChatTemplateMessage.setTemplate_id(templateMessage.getTemplate_id());
        weChatTemplateMessage.setTopcolor(templateMessage.getTopcolor());
        String data = JsonHelper.toJsonString(templateMessage);
        weChatTemplateMessage.setData(data);

        WeChatMsg weChatMsg = new WeChatMsg();
        weChatMsg.setFromUserName(WeChatConfig.appid);
        weChatMsg.setToUserName(templateMessage.getTouser());
        weChatMsg.setContent(templateMessage.getData().get("first").getValue()); //first key的内容是主要显示内容
        weChatMsg.setWxCreateTime(System.currentTimeMillis());

        try {
            RemexConstants.logger.info("Send WeChat Template Message, the template is ："+templateMessage.getTemplate_id());
            String result = HttpHelper.sendXml(WeChatAPIConfig.templatMsgSend + "&access_token=" + access_token, data);
            RemexConstants.logger.info("Send WeChat Template Message, the result is ：" + result);
            WeChactMessageResult weChectMessageResult = JsonHelper.toJavaObject(result, WeChactMessageResult.class);
            weChatTemplateMessage.setErrcode(weChectMessageResult.getErrcode());
            weChatTemplateMessage.setErrmsg(weChectMessageResult.getErrmsg());
            weChatTemplateMessage.setMsgid(weChectMessageResult.getMsgid());

            weChatMsg.setErrcode(weChectMessageResult.getErrcode());
            weChatMsg.setErrmsg(weChectMessageResult.getErrmsg());
        }finally {
            ContainerFactory.getSession().store(weChatTemplateMessage);
            ContainerFactory.getSession().store(weChatMsg);
        }

        return "0".equals(weChatTemplateMessage.getErrcode());
    }


    /**
     * 发送普通文本消息，用户发生以下事件
     * 1、用户发送信息
     2、点击自定义菜单（仅有点击推事件、扫码推事件、扫码推事件且弹出“消息接收中”提示框这3种菜单类型是会触发客服接口的）
     3、关注公众号
     4、扫描二维码
     5、支付成功
     6、用户维权

     则48h以内可以发送消息给用户
     * @param openId
     * @param msgContent
     * @return
     */
    public static boolean sendCustomWeChatMsg(String openId, String msgContent) {
        String access_token = WeChatConfig.getAccess_token();
        Assert.notNull(access_token, ServiceCode.FAIL, "invalid access_token");
        Assert.notNull(msgContent, ServiceCode.FAIL, "invalid msgContent");
        CustomMsg customMsg = new CustomMsg();
        customMsg.setTouser(openId);
        customMsg.setMsgtype("text");
        customMsg.setText(new CustomMsgData(msgContent));
        String data = JsonHelper.toJsonString(customMsg);

        WeChatMsg weChatMsg = new WeChatMsg();
        weChatMsg.setFromUserName(WeChatConfig.appid);
        weChatMsg.setToUserName(openId);
        weChatMsg.setContent(msgContent);
        weChatMsg.setWxCreateTime(System.currentTimeMillis());

        try {
            RemexConstants.logger.info("send text WeChatMsg openId="+openId+";msgContent="+msgContent);
            String result = HttpHelper.sendXml(WeChatAPIConfig.customMsgSend + access_token,data);
            RemexConstants.logger.info("send text WeChatMsg result："+result);
            WeChactMessageResult weChectMessageResult = JsonHelper.toJavaObject(result, WeChactMessageResult.class);
            weChatMsg.setErrcode(weChectMessageResult.getErrcode());
            weChatMsg.setErrmsg(weChectMessageResult.getErrmsg());
        }finally {
            ContainerFactory.getSession().store(weChatMsg);
            return "0".equals(weChatMsg.getErrcode());
        }
    }
}
