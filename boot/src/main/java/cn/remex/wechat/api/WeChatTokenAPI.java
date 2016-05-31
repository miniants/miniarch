package cn.remex.wechat.api;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.util.Assert;
import cn.remex.core.util.JsonHelper;
import cn.remex.wechat.beans.WeChatAccessToken;
import cn.remex.wechat.beans.accesstoken.AccessTokenResult;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;

/**
 * Created by guoqi on 2016/2/26.
 */
public class WeChatTokenAPI {


    /**
     * 获取access token
     *
     * access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。
     * 开发者需要进行妥善保存。access_token的存储至少要保留512个字符空间。
     * access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
     * @return
     */
    public static WeChatAccessToken accessToken(String appid, String secret){
        Assert.notNull(appid, ServiceCode.FAIL, "appid 不能为空~！");
        Assert.notNull(secret, ServiceCode.FAIL, "secret 不能为空~！");
        RemexConstants.logger.info("微信->获取 access_token");
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");
        url.append("&appid=").append(appid);
        url.append("&secret=").append(secret);
        String result = HttpHelper.sendXml(url.toString(),"");
        RemexConstants.logger.info("微信->获取 access_token  结果："+result);
        return JsonHelper.toJavaObject(result, WeChatAccessToken.class);
    }

    public static AccessTokenResult obtainOpenidByCode(String code){
        //appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = WeChatAPIConfig.access_token + "appid="+ WeChatConfig.appid+"&secret=" + WeChatConfig.secret + "&code=" + code + "&grant_type=authorization_code";
        RemexConstants.logger.info("获取access_token" + url);
        String result = HttpHelper.sendXml(url,"");
        return  JsonHelper.toJavaObject(result,AccessTokenResult.class);
    }

}
