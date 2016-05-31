package cn.remex.wechat.wechatservice;

import cn.remex.core.RemexApplication;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.*;
import cn.remex.web.service.BusinessService;
import cn.remex.wechat.beans.paymch.PayNotify;
import cn.remex.wechat.wechatservice.bean.WeChatPayNotifyOuter;
import cn.remex.wechat.config.WeChatConfig;
import cn.remex.wechat.event.WeChatPayNotifyEvent;
import cn.remex.wechat.utils.Signature;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by guoqi on 2016/3/21.
 */
@BusinessService
public class WeChatPayNotify {
    @BusinessService(bodyParamName = "xml", needAuth = false)
    public String payNotify(String xml) {
        WeChatPayNotifyOuter weChatPayNotifyOuter = new WeChatPayNotifyOuter();
        if (Judgment.nullOrBlank(xml)) {
            weChatPayNotifyOuter.setReturn_code("FAIL ");
            weChatPayNotifyOuter.setReturn_msg("接收信息为空");
            return XmlHelper.marshall(weChatPayNotifyOuter);
        }
        PayNotify payNotify = XmlHelper.unmarshall(PayNotify.class, xml);
        if (!Judgment.nullOrBlank(payNotify)) {
            Map<String, String> map = MapHelper.objectToFlat(payNotify);
            if (Signature.validateSign(map, WeChatConfig.KEY)) {
                this.saveXMLFile(xml, "PAY", payNotify.getOut_trade_no());
                try {
                    RemexApplication.publishEvent(new WeChatPayNotifyEvent("WeChatPayNotifyEvent", payNotify));
//                    BsRvo bsRvo = PaySuccessService.paySucess(payNotify);
                    weChatPayNotifyOuter.setReturn_code("SUCCESS");

                } catch (Throwable throwable) {
                        weChatPayNotifyOuter.setReturn_code("FAIL");
                        weChatPayNotifyOuter.setReturn_msg(throwable.toString());
                }

                return  XmlHelper.marshall(weChatPayNotifyOuter);
            }
        }
        weChatPayNotifyOuter.setReturn_code("FAIL");
        return XmlHelper.marshall(weChatPayNotifyOuter);
    }


    private boolean saveXMLFile(String xml, String type, String fileName) {
        final String curSystem = System.getProperty("os.name");
        String baseXmlFilePath = null;
        if (curSystem.toUpperCase().startsWith("WIN")) {
//			findXMLFilePathSQL += "'ErrorXmlFileSavePath'";
            baseXmlFilePath = WeChatConfig.WIN_PATH;
        } else if (curSystem.toUpperCase().startsWith("LINUX")) {
//			findXMLFilePathSQL += "'LinuxErrorXmlFileSavePath'";
            baseXmlFilePath = WeChatConfig.LINUX_PATH;
        }
        Assert.notNull(baseXmlFilePath, ServiceCode.FAIL, "文件路径不能为空！");
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HHmmss");
        FileHelper.saveFileContent(baseXmlFilePath + File.separator + type + File.separator + localDateTime.toLocalDate().toString() + File.separator + fileName + ".xml", xml);
        return true;
    }
}
