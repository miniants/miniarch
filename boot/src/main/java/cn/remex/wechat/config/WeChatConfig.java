package cn.remex.wechat.config;

import cn.remex.core.RemexApplication;
import cn.remex.core.util.Judgment;
import cn.remex.db.ContainerFactory;
import cn.remex.db.sql.WhereRuleOper;
import cn.remex.wechat.beans.WeChatAccessToken;
import cn.remex.wechat.api.WeChatTokenAPI;
import cn.remex.wechat.models.WeChatToken;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Created by guoqi on 2016/2/26.
 */
public class WeChatConfig {
    public final static String WeiXin = "weixin";
    public final static String WeiXin_subscribeMsg = "subscribeMsg";
    private final static Map<String, Object> wxConfig = RemexApplication.getBean(WeiXin);
    private final static Map<String, String> wxStateRedirects = (Map<String, String>) wxConfig.get("state_redirect");


    public final static String token  = (String)wxConfig.get("token");
    public final static String appid  = (String)wxConfig.get("appid"); //第三方用户唯一凭证
    public final static String secret = (String)wxConfig.get("secret");//第三方用户唯一凭证密钥，即appsecret

    /**
     * 微信支付配置
     */
    public static String KEY = "QQWWEqweasdcdffggrehu45123465123"; //签名算法需要用到的秘钥
    public static String MCHID = "1323681901"; //商户ID
    private static String backurl = null; //微信回调


    public static String LINUX_PATH = File.separator + "data" + File.separator + "default" + File.separator + "wxxml";
    public static String WIN_PATH = "D:" + File.separator + "data" + File.separator  + "default"+ File.separator + "wxxml";
//    private static String access_Token = "";


    public static String obtainStateRedirect(String state) {
        return wxStateRedirects.get(state);
    }

    //access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token
    public static String getAccess_token() {
        //数据库获取token
        WeChatToken weChatToken = ContainerFactory.getSession().createDbCvo(WeChatToken.class)
                .filterBy(WeChatToken::getValid, WhereRuleOper.eq, "true")
                .filterBy(WeChatToken::getAppid, WhereRuleOper.eq, appid).ready().queryBean();
        LocalDateTime localDateTimenow = LocalDateTime.now();
        if (!Judgment.nullOrBlank(weChatToken)) { // 如果不为空 则则说明数据库中有
            LocalDateTime localDateTime = LocalDateTime.parse(weChatToken.getAccessTime()); //  token存入的时间
            if (Math.abs(localDateTimenow.until(localDateTime, ChronoUnit.MILLIS)) < 5400000) { // 判断token是否失效
                return weChatToken.getAccessToken();  //返回token
            }
            weChatToken.setValid(false);
            ContainerFactory.getSession().store(weChatToken); //失效 做好标志位
        }
        //若数据库中没有token 或者刚刚删除， 则去获取新的token 存入
        WeChatAccessToken weChectAccessToken = WeChatTokenAPI.accessToken(WeChatConfig.appid, WeChatConfig.secret);
        WeChatToken weChatToken1 = new WeChatToken();
        weChatToken1.setAccessToken(weChectAccessToken.getAccess_token());
        weChatToken1.setAccessTime(localDateTimenow.toString());
        weChatToken1.setAppid(appid);
        weChatToken1.setValid(true);
        ContainerFactory.getSession().store(weChatToken1);
        return weChectAccessToken.getAccess_token();
    }


    public static String getBackurl() {
        if(Judgment.nullOrBlank(backurl)){
            backurl = (String)wxConfig.get("payBackUrl");
        }
        return backurl;
    }

    public static String certLocalPath; // HTTP证书在服务器中的路径，用来加载证书用
    public static String certPassword; // HTTP证书的密码，默认等于MCHID


}
