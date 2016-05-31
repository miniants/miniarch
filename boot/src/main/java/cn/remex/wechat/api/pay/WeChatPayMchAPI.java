package cn.remex.wechat.api.pay;

import cn.remex.RemexConstants;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.MapHelper;
import cn.remex.core.util.XmlHelper;
import cn.remex.wechat.beans.paymch.WeChatUnifiedorder;
import cn.remex.wechat.beans.paymch.WeChatUnifiedorderResult;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;
import cn.remex.wechat.utils.Signature;

import java.util.Map;

/**
 * 微信 支付
 * Created by guoqi on 2016/3/21.
 */
class WeChatPayMchAPI {
    /**
     *  微信 统一下单接口
     * @param weChatUnifiedorder
     * @return
     */
    static WeChatUnifiedorderResult payUnifiedorder(WeChatUnifiedorder weChatUnifiedorder){
        Map<String, String> map = MapHelper.objectToFlat(weChatUnifiedorder);
        if( !Judgment.nullOrBlank( WeChatConfig.KEY)){
			String sign = Signature.generateSign(map, WeChatConfig.KEY);
            weChatUnifiedorder.setSign(sign);
		}
        String weChatUnifiedorderXML = XmlHelper.marshall(weChatUnifiedorder);
        RemexConstants.logger.info("微信->预下单  发送数据："+weChatUnifiedorderXML);
        String result = HttpHelper.sendXml(WeChatAPIConfig.payUnifiedorder,weChatUnifiedorderXML);
        RemexConstants.logger.info("微信->预下单  接收数据："+result);
        return XmlHelper.unmarshall(WeChatUnifiedorderResult.class,result);
    }


}
