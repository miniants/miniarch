package cn.remex.wechat.config;

/**
 * 微信url接口配置
 * Created by guoqi on 2016/3/1.
 */
public class WeChatAPIConfig {

    /**
     * 发送普通文本消息
     */
    public static final String customMsgSend = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    /**
     * 模板信息发送
     */
    public static final String templatMsgSend = "https://api.weixin.qq.com/cgi-bin/message/template/send?";
    /**
     * 批量获取用户
     */
    public static final String pulluserInfo ="https://api.weixin.qq.com/cgi-bin/user/get?";
    /**
     * 用户获取
     */
    public static final String userInfo = "https://api.weixin.qq.com/cgi-bin/user/info?";
    /**
     * 二维码生成url
     */
    public static final String qrcodeTicketCreate= "https://api.weixin.qq.com/cgi-bin/qrcode/create?";

    /**
     * 通过ticket换取二维码
     */
    public static final String showqrcode= "https://mp.weixin.qq.com/cgi-bin/showqrcode?";
    /**
     * 创建菜单
     */
    public static final String menuCreate = "https://api.weixin.qq.com/cgi-bin/menu/create?";

    /**
     * 通过code换取网页授权access_token
     */
    public static final  String access_token = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    /**
     * 微信预下单接口
     */
    public static final  String payUnifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
