package cn.remex.wechat.wechatservice;

import cn.remex.RemexConstants;
import cn.remex.core.RemexApplication;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.FileHelper;
import cn.remex.core.util.Judgment;
import cn.remex.web.service.BusinessService;
import cn.remex.wechat.config.WeChatConfig;
import cn.remex.wechat.event.WeChatEvent;
import cn.remex.wechat.event.WeChatScanEvent;
import cn.remex.wechat.event.WeChatSubscribeEvent;
import cn.remex.wechat.event.WeChatTextEvent;
import cn.remex.wechat.event.WeChatUnsubscribeEvent;
import cn.remex.wechat.utils.SHA1;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 微信回调地址
 * Created by guoqi on 2016/2/26.
 */
@BusinessService
public class WeChatCommService {
    /**
     * 验证服务器地址的有效性
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @BusinessService(bodyParamName = "xml", needAuth = false)
    public String dispatchWeChat(String signature, String timestamp, String nonce, String echostr, String xml) {
        if (!Judgment.nullOrBlank(echostr)) {  //微信服务器验证
            if (!this.checkSignature(timestamp, nonce, signature)) {
                // 消息签名不正确，说明不是公众平台发过来的消息
                RemexConstants.logger.info("微信->非法请求！");
                return null;
            }
            // 说明是一个仅仅用来验证的请求，返回echostr
            RemexConstants.logger.info("微信->验证请求！");
            return echostr;
        }
        if (!Judgment.nullOrBlank(xml)) { // 微信
            RemexConstants.logger.info("微信->回调信息！ 为：" + xml);
            String resXml = this.XmlOperation(xml);
            RemexConstants.logger.info("微信->回调信息！ 为：" + resXml);
            return resXml;
        }
        //微信错误请求！
        RemexConstants.logger.info("微信->错误请求！");
        return null;
    }
    //微信事件触发
    private String XmlOperation(String xml) {
        String resXml="";
        try {
            Document document = DocumentHelper.parseText(xml);
            String msgType = document.getRootElement().elementText("MsgType");
            String fromUserName = document.getRootElement().elementText("FromUserName");
            String toUserName = document.getRootElement().elementText("ToUserName");
            long createTime = Long.parseLong(document.getRootElement().elementText("CreateTime"));
            String event = document.getRootElement().elementText("Event");
            String eventKey = document.getRootElement().elementText("EventKey");
            String ticket = document.getRootElement().elementText("Ticket");
            if (Judgment.nullOrBlank(msgType)) {
                RemexConstants.logger.info("invalid wechat xml");
                return "";
            }
            this.saveXMLFile(xml, msgType, null, fromUserName);

            //定义返回的xml的document，设置ToUserName和FromUserName
            Element re = DocumentHelper.createElement("xml");
            Document doc = DocumentHelper.createDocument(re);
            re.addElement("FromUserName").addText(document.getRootElement().elementText("ToUserName"));
            re.addElement("ToUserName").addText(fromUserName);
            re.addElement("CreateTime").addText(new Date().getTime()/1000+"");
            RemexApplication.publishEvent(new WeChatEvent("wechat", msgType, fromUserName,toUserName,createTime, event, eventKey, ticket, document));
            //text  image  voice video shortvideo location link event
            switch (msgType) {
//                case "text": //文本，用户发的信息
//                    break;
                case "event":  //用户的事件
                    String openId = fromUserName;

                    switch (event) {
                        case "subscribe": //关注事件
                            String[] eventKeyParts = null;
                            RemexApplication.publishEvent(new WeChatSubscribeEvent("subscribe", openId, !Judgment.nullOrBlank(eventKey) && (eventKeyParts = eventKey.split("_")).length > 1 ? eventKeyParts[1] : null));
                            re.addElement("MsgType").addText("text");
                            re.addElement("Content").addText(RemexApplication.getMapValue(WeChatConfig.WeiXin,WeChatConfig.WeiXin_subscribeMsg));
                            resXml = doc.asXML();
                            break;
                        case "SCAN": //扫码事件，已经关注后，再次扫码事件
                            RemexApplication.publishEvent(new WeChatScanEvent("SCAN", openId, eventKey, ticket));
                            break;
                        case "unsubscribe": //取消关注事件
                            RemexApplication.publishEvent(new WeChatUnsubscribeEvent("unsubscribe", openId, eventKey));
                            break;
                        case "TEMPLATESENDJOBFINISH": //模板发送成功
                            break;
                    }
                    this.saveXMLFile(xml, msgType, event, fromUserName);
                    break;
                case "text": //文本，用户发的信息
                    String content = document.getRootElement().elementText("Content");
                    String msgId = document.getRootElement().elementText("MsgId");
                    RemexApplication.publishEvent(new WeChatTextEvent("wechatTextEvent",fromUserName,content,Long.valueOf(createTime),msgId));
                default:
                    re.addElement("MsgType").addText("transfer_customer_service");
                    resXml = doc.asXML();
                    break;
            }

        } catch (DocumentException e) {
            RemexConstants.logger.error("微信->XML 解析错误！！", e);
        }
        RemexConstants.logger.info(resXml);
        return resXml;
    }

    private boolean saveXMLFile(String xml, String type, String evenType, String fileName) {
        final String curSystem = System.getProperty("os.name");
        String baseXmlFilePath = null;
        if (curSystem.toUpperCase().startsWith("WIN")) {
//			findXMLFilePathSQL += "'ErrorXmlFileSavePath'";
            baseXmlFilePath = WeChatConfig.WIN_PATH;
        } else if (curSystem.toUpperCase().startsWith("LINUX")) {
//			findXMLFilePathSQL += "'LinuxErrorXmlFileSavePath'";
            baseXmlFilePath = WeChatConfig.LINUX_PATH;
        }
        Assert.notNull(baseXmlFilePath,  ServiceCode.FAIL,"文件路径不能为空！");
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HHmmss");
        StringBuilder xmlFilePath = new StringBuilder(baseXmlFilePath + File.separator + type + File.separator);
        if (!Judgment.nullOrBlank(evenType)) {
            xmlFilePath.append(evenType).append(File.separator);
        }
        xmlFilePath.append(localDateTime.toLocalDate().toString()).append(File.separator).append(localDateTime.toLocalTime().format(f)).append("-").append(fileName).append(".xml");
        FileHelper.saveFileContent(xmlFilePath.toString(), xml);
        return true;
    }


    private boolean checkSignature(String timestamp, String nonce, String signature) {
        try {
            return SHA1.gen(WeChatConfig.token, timestamp, nonce).equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
