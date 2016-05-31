package cn.remex.wechat.api.pay;

import cn.remex.RemexConstants;
import cn.remex.core.util.Judgment;
import cn.remex.wechat.beans.paymch.WeChatUnifiedorder;
import cn.remex.wechat.beans.paymch.WeChatUnifiedorderResult;
import cn.remex.wechat.config.WeChatConfig;
import cn.remex.wechat.beans.paymch.WeChatPayResult;

import java.util.UUID;

/**
 * Created by guoqi on 2016/3/23.
 */
public class WeChatPay {
    /**
     *  微信支付
     * @param desc
     * @param orderId
     * @param money
     * @param payType  可以为空， 默认 JSAPI 支付    NATIVE： 扫码
     * @return
     */
    public static WeChatPayResult pay(String desc, String orderId, String money, String openId, String payType){
        RemexConstants.logger.info("微信付款："+desc+"金额："+money);
        WeChatUnifiedorder weChatUnifiedorder = new WeChatUnifiedorder();
        weChatUnifiedorder.setAppid(WeChatConfig.appid);
        weChatUnifiedorder.setMch_id(WeChatConfig.MCHID);
        weChatUnifiedorder.setNonce_str(UUID.randomUUID().toString().replace("-", ""));
        weChatUnifiedorder.setBody(desc);
        weChatUnifiedorder.setOut_trade_no(orderId);
        weChatUnifiedorder.setTotal_fee(money);
        weChatUnifiedorder.setSpbill_create_ip("192.168.1.1");
        weChatUnifiedorder.setNotify_url(WeChatConfig.getBackurl());
        weChatUnifiedorder.setTrade_type(Judgment.nullOrBlank(payType)?"JSAPI":payType);
        if(Judgment.nullOrBlank(payType)){
            weChatUnifiedorder.setOpenid(openId);
        }
        WeChatUnifiedorderResult weChatUnifiedorderResult = WeChatPayMchAPI.payUnifiedorder(weChatUnifiedorder);
        if("SUCCESS".equals(weChatUnifiedorderResult.getReturn_code())){
            WeChatPayResult weChatPay = new WeChatPayResult();
            if("SUCCESS".equals(weChatUnifiedorderResult.getResult_code())){
                RemexConstants.logger.info("微信付款预下单成功！");
                weChatPay.setCode(weChatUnifiedorderResult.getResult_code());
                weChatPay.setPrepay_id(weChatUnifiedorderResult.getPrepay_id());
                weChatPay.setUrl(weChatUnifiedorderResult.getCode_url());
                return weChatPay;
            }else{
                RemexConstants.logger.info("微信付款发生错误！"+"错误代码："+weChatUnifiedorderResult.getErr_code()+"错误原因："+weChatUnifiedorderResult.getErr_code_des());
                weChatPay.setCode(weChatUnifiedorderResult.getErr_code());
                weChatPay.setMsg(weChatUnifiedorderResult.getErr_code_des());
                return weChatPay;
            }
        }
        RemexConstants.logger.info("微信付款预网络错误！");
        return null;
    }
}
