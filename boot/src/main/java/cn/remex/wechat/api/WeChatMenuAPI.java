package cn.remex.wechat.api;

import cn.remex.RemexConstants;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.util.JsonHelper;
import cn.remex.wechat.beans.WeChatResult;
import cn.remex.wechat.beans.menu.MenuButtons;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;

/**
 *
 * Created by guoqi on 2016/3/4.
 */
public class WeChatMenuAPI {

    /**
     * 创建菜单
     * @param menuButtons
     * @return
     */
    public static WeChatResult menuCreate(MenuButtons menuButtons){
        String json = JsonHelper.toJsonString(menuButtons);
        RemexConstants.logger.info("微信->创建菜单  发送参数："+json);
        String result = HttpHelper.sendXml(WeChatAPIConfig.menuCreate + "access_token=" + WeChatConfig.getAccess_token(),json);
        RemexConstants.logger.info("微信->创建菜单   接收参数："+result);
        return JsonHelper.toJavaObject(result,WeChatResult.class);
    }

}
