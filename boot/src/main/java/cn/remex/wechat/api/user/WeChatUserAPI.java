package cn.remex.wechat.api.user;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.util.Assert;
import cn.remex.core.util.JsonHelper;
import cn.remex.core.util.Judgment;
import cn.remex.web.service.BsException;
import cn.remex.wechat.beans.WeChatUserInfo;
import cn.remex.wechat.beans.WeChatUserOpenIdList;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;

import java.util.function.Consumer;

/**
 * Created by guoqi on 2016/2/29.
 */
public class WeChatUserAPI {
    /**
     * 在关注者与公众号产生消息交互后，
     * 公众号可获得关注者的OpenID
     * （加密后的微信号，每个用户对每个公众号的OpenID是唯一的。对于不同公众号，同一用户的openid不同）。
     * 公众号可通过本接口来根据OpenID获取用户基本信息，包括昵称、头像、性别、所在城市、语言和关注时间。
     * @param openId
     * @return
     */
    public static WeChatUserInfo userInfo(String openId){
        String access_token = WeChatConfig.getAccess_token();
        Assert.notNullAndEmpty(openId, ServiceCode.OPENID_INVALID, "用户openId 不能为空~！");
        Assert.notNullAndEmpty(access_token, ServiceCode.WEIXIN_ARGS_INVALID, "access_token 不能为空~！");
        RemexConstants.logger.info("obtain WeChat UserInfo by openId:"+openId);
        String url = WeChatAPIConfig.userInfo + "access_token=" + access_token +
                "&openid=" + openId +
                "&lang=zh_CN";
        String result = HttpHelper.sendXml(url,"");
        RemexConstants.logger.info("微信->获取用户基本信息为:"+result);
        WeChatUserInfo weChatUserInfo = JsonHelper.toJavaObject(result, WeChatUserInfo.class);
        if(Judgment.nullOrBlank(weChatUserInfo.getErrcode())){
            String nickname = weChatUserInfo.getNickname();
            if(!Judgment.nullOrBlank(nickname)){ // 只 支持 unic表情，字符 删除其他
                weChatUserInfo.setNickname(nickname.replaceAll("[^\\w\u4e00-\u9fa5]",""));
            }
            return "0".equals(weChatUserInfo.getSubscribe())?null: weChatUserInfo;
        }else{
            throw new BsException("微信--获取用户信息失败！");
        }

    }


    public static void everyWeChatUserOpenId(Consumer<String> openIdConsumer) {
        WeChatUserOpenIdList weChatUserOpenIdList = obtainOpenIdList("", openIdConsumer);
        do{
            if (Judgment.nullOrBlank(weChatUserOpenIdList.getErrcode()) && weChatUserOpenIdList.getData() != null && weChatUserOpenIdList.getData().getOpenid() != null) {
                weChatUserOpenIdList.getData().getOpenid().forEach(openIdConsumer);
            } else {
                throw new BsException("微信--获取用户信息失败！");
            }
            weChatUserOpenIdList = obtainOpenIdList(weChatUserOpenIdList.getNext_openid(), openIdConsumer);
        }while(!Judgment.nullOrBlank(weChatUserOpenIdList.getNext_openid()));
    }


    private static WeChatUserOpenIdList obtainOpenIdList(String openId,Consumer<String> openIdConsumer) {
        StringBuilder url = new StringBuilder(WeChatAPIConfig.pulluserInfo);
        String access_token = WeChatConfig.getAccess_token();
        Assert.notNullAndEmpty(access_token, ServiceCode.WEIXIN_ARGS_INVALID, "access_token can not be null！");

        url.append("access_token=").append(access_token);
        url.append("&next_openid=") .append(openId);
        url.append("&lang=zh_CN");


        String result = HttpHelper.sendXml(url.toString(),"");
        RemexConstants.logger.info("Poll WeChat User OpenId List...");
        WeChatUserOpenIdList weChatUserOpenIdList = JsonHelper.toJavaObject(result, WeChatUserOpenIdList.class);

        if(Judgment.nullOrBlank(weChatUserOpenIdList.getErrcode())){
            return weChatUserOpenIdList;
        }else{
            throw new BsException("Failed for Poll WeChat User OpenId List");
        }
    }
}
