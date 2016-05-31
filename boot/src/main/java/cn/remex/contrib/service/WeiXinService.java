package cn.remex.contrib.service;

import cn.remex.RemexConstants;
import cn.remex.contrib.appbeans.DataCvo;
import cn.remex.contrib.appbeans.DataRvo;
import cn.remex.contrib.auth.AuthenticateBtx;
import cn.remex.core.CoreSvo;
import cn.remex.core.RemexApplication;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.net.HttpHelper;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.JsonHelper;
import cn.remex.core.util.Judgment;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbRvo;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.sql.WhereGroupOp;
import cn.remex.db.sql.WhereRuleOper;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import cn.remex.web.service.appbeans.AsyncCvo;
import cn.remex.web.service.appbeans.AsyncRvo;
import cn.remex.wechat.api.WeChatMenuAPI;
import cn.remex.wechat.api.WeChatMessageAPI;
import cn.remex.wechat.api.user.WeChatUserAPI;
import cn.remex.wechat.beans.WeChatUserInfo;
import cn.remex.wechat.beans.accesstoken.AccessTokenResult;
import cn.remex.wechat.beans.menu.Button;
import cn.remex.wechat.beans.menu.MenuButtons;
import cn.remex.wechat.beans.messages.TemplateMessage;
import cn.remex.wechat.beans.messages.TemplateMsg;
import cn.remex.wechat.config.WeChatAPIConfig;
import cn.remex.wechat.config.WeChatConfig;
import cn.remex.wechat.event.WeChatEvent;
import cn.remex.wechat.event.WeChatScanEvent;
import cn.remex.wechat.event.WeChatSubscribeEvent;
import cn.remex.wechat.event.WeChatTextEvent;
import cn.remex.wechat.event.WeChatUnsubscribeEvent;
import cn.remex.wechat.models.WeChatMsg;
import cn.remex.wechat.models.WeChatNotify;
import cn.remex.wechat.models.WeChatUser;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.remex.RemexConstants.UserType.ADMIN;
import static cn.remex.core.exception.ServiceCode.FAIL;
import static cn.remex.core.exception.ServiceCode.SUCCESS;
import static cn.remex.web.service.BusinessService.ServiceType.AsyncService;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by guoqi on 2016/3/1.
 */
@BusinessService
@Component
@EnableAsync
public class WeiXinService {

    public static AccessTokenResult obtainOpenidFromCode(String code){
        //appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = WeChatAPIConfig.access_token + "appid="+ WeChatConfig.appid+"&secret=" + WeChatConfig.secret + "&code=" + code + "&grant_type=authorization_code";
        RemexConstants.logger.info("获取access_token" + url);
        String result = HttpHelper.sendXml(url,"");
        return  JsonHelper.toJavaObject(result,AccessTokenResult.class);
    }

    @BusinessService(authLevel = ADMIN)
    public Object updateWxMenu(){
        MenuButtons menuButtons = new MenuButtons();
        List<Button> buttons = RemexApplication.getBean("weixin_menus");
        menuButtons.setButton(buttons);
        return WeChatMenuAPI.menuCreate(menuButtons);
    }

    @BusinessService(needAuth = false)
    public BsRvo redirectWxMenu(String code, String state){
        //code 是微信回调的授权code
        //state 是微信菜单回调时携带的参数

        RemexConstants.logger.info("===================微信菜单回调,code="+code + ";state="+state+",==================");
        if(!Judgment.nullOrBlank(code)){
            //默认通过openid登录
	        String openid = WeiXinService.obtainOpenidFromCode(code).getOpenid();//"o1dzwv1e8Nl1Eq_OA3jvBScf0tAg";
            AuthUser authUser = ContainerFactory.getSession().createDbCvo(AuthUser.class)
                    .filterBy(AuthUser::getOpenid, WhereRuleOper.eq,openid)
                    .ready().query().obtainBean();
			//如果失败则通过uionid登录
	        if(Judgment.nullOrBlank(authUser)){
		        String unionid = WeChatUserAPI.userInfo(openid).getUnionid();
		        authUser = ContainerFactory.getSession().createDbCvo(AuthUser.class)
				        .filterBy(AuthUser::getUnionid, WhereRuleOper.eq,unionid)
				        .ready().query().obtainBean();
	        }

            String orgin_redirect = WeChatConfig.obtainStateRedirect(state);
            if(Judgment.nullOrBlank(authUser)){
                String state_redirect = WeChatConfig.obtainStateRedirect("STU_LOGIN")+"?redirectUri="+orgin_redirect;
                CoreSvo.putCookie("openId",openid);//openId回填前端页面，用于后续的绑定
                RemexConstants.logger.info("===================微信菜单回调，需要登录,state_redirect="+state_redirect+",==================");
                return new BsRvo(SUCCESS, "微信菜单回调服务正常，该用户没有绑定，需要登录绑定", state_redirect, "wxmenu", "jsp");
            }else{
                AuthenticateBtx.placeToken(authUser);//登录成功
                RemexConstants.logger.info("===================微信菜单回调，正常跳转,state_redirect="+orgin_redirect+",==================");
                return new BsRvo(SUCCESS, "微信菜单回调服务正常，绑定用户登录成功", orgin_redirect, "wxmenu", "jsp");
            }
        }
        return new BsRvo(ServiceCode.ERROR, "微信菜单回调时code参数为空！", null, "wxmenu", "jsp");
    }

    @BusinessService(authLevel = ADMIN)
    public BsRvo weixinUsers(DataCvo dataCvo) {
        DbRvo userInfos = dataCvo.obtainDbCvo(WeChatUser.class).ready().query();
        return new DataRvo(userInfos);
    }
	@BusinessService(type = AsyncService)
	public BsRvo updateAllUserInfoWithWeChat(AsyncCvo asyncCvo){
		return new AsyncRvo().start(asyncRvo -> {
			WeChatUserAPI.everyWeChatUserOpenId(this::updateUserInfoWithWeChat);
			return asyncRvo;
		});
	}
	@BusinessService
	public BsRvo updateUserInfoWithWeChatByOpenId(String openId){
		updateUserInfoWithWeChat(openId);
		return new BsRvo(SUCCESS,"用户微信信息更新！","");
	}
	@BusinessService
	public BsRvo sendMsgByOpenId(String openid, String msgContent){
		AuthUser curUserInfo = ContainerFactory.createDbCvo(AuthUser.class).filterBy(AuthUser::getOpenid, WhereRuleOper.eq, openid).ready().queryBean();

		if(true || System.currentTimeMillis()-curUserInfo.getWeixinLastTime()<23*3600*1000) {
			return new BsRvo(WeChatMessageAPI.sendCustomWeChatMsg(openid, msgContent)? SUCCESS:FAIL,"消息发送");
		}else
			return new BsRvo(sendTemplateMsg(curUserInfo.getOpenid(), msgContent)? SUCCESS:FAIL,"消息发送");
	}



    @EventListener@Async // spring异步事件
    public void dealWeChatEvent(WeChatEvent event) {
        RemexConstants.logger.info("异步处理WeChatEvent，openId="+event.getFromUserName());

	     //保存所有的微信信息
        //event eventKey ticket 三个字段必须保存，其中ticket再微信二维码扫描登录中已被使用
	    WeChatNotify weChatNotify = new WeChatNotify(event.getMsgType(), event.getFromUserName(), event.getToUserName(), new Date().getTime()/1000,
			    event.getEvent(), event.getEventKey(),event.getTicket() );
	    ContainerFactory.getSession().store(weChatNotify);

        //更新微信访问的最后时间
        AuthUser authUser = ContainerFactory.createDbCvo(AuthUser.class).filterBy(AuthUser::getOpenid, WhereRuleOper.eq, event.getFromUserName()).ready().queryBean();
        if(!Judgment.nullOrBlank(authUser)) {
	        authUser.setWeixinLastTime(System.currentTimeMillis()/1000);
	        ContainerFactory.getSession().store(authUser);
        }

	    //更新用户信息，TODO 不用每次都更新
	    //updateUserInfoWithWeChat(event.getFromUserName());
    }
    /**
     * 微信微信与用户绑定
     *
     * //此方法不能跑出异常，否则微信端会出错。
     *
     * 关注事件(直接关注 eventKey为空，没有关注时通过群生成的二维码关注 eventKey为scene_id，没有关注时通过平台生成的二位码关注 比如登录的二位码，eventKey为qrscene_+平台指定的参数)
     * 关注事件中，如果是通过平台用用户id生成的二位码进入的，则可以绑定用户
     * @return
     */
    @EventListener@Async // spring异步事件
    public void dealSubscribeForBindUser(WeChatSubscribeEvent event) {
        RemexConstants.logger.info("异步处理WeChatSubscribeEvent，openId="+event.getOpenId()+";userId(sceneKey)="+event.getSceneKey());
        String openId = event.getOpenId();
        String userId = event.getSceneKey();

        if(!Judgment.nullOrBlank(event.getSceneKey()))
            bindUser(openId, userId);
    }

    public static final String weixinLoginQrcodeUrl_SceneId = "100"; // 微信登陆二维码场景
    @EventListener@Async
    public void dealScanEventForBindUserOrWeChatLogin(WeChatScanEvent event) {
        RemexConstants.logger.info("异步处理 WeChatScanEvent，openId="+event.getOpenId()+";eventKey="+event.getEventKey());
        //本系统定义了几个二位码的使用场景：
        //1. QR_SCENE为临时，weixinLoginQrcodeUrl_SceneId == eventKey -> 微信登录（目前Teep端在使用，CET没有使用）
        //2. 【此为默认选项】QR_LIMIT_STR_SCENE为永久的字符串参数值, 在AdminBs.bindWeixin 中通过userid生成二位码，实现用户绑定
        if (weixinLoginQrcodeUrl_SceneId.equals(event.getEventKey())) {
            //TODO 此分支的代码还需要在登录逻辑中分离 所有的时间均保存到WeChatNotify表中，后续可以考虑放到缓存中
//            //扫码登录
//            AuthUser curUser = ContainerFactory.createDbCvo(AuthUser.class)
//                    .filterBy(AuthUser::getOpenid, eq, event.getOpenId())
//                    .ready().queryBean();
//            if(!Judgment.nullOrBlank(curUser)){
//                String tickets = event.getTickets();
//                AuthenticateBtx.wxusers.put(tickets, curUser);//登陆成功,微信扫描成功
//            }
	        //TODO 通过WeChatEvent事件将扫描信息保存到WeChatNotify表中，再UserService.loginByWeiXin中读取该表的信息进行微信登录，避免了分布式缓存的问题。登录属于非平凡操作，直接读库问题不大
        }else if(!Judgment.nullOrBlank(event.getEventKey())){
            //绑定用户，默认eventKey不为空则尝试绑定
            RemexConstants.logger.info("准备绑定用户openId="+event.getOpenId()+";eventKey="+event.getEventKey());
            bindUser(event.getOpenId(), event.getEventKey());
        }else{
            //eventKey 为空的事件暂不处理
        }
    }
    @EventListener@Async
    public void dealUnsubscribeForDismissUser(WeChatUnsubscribeEvent event) {
        RemexConstants.logger.info("异步处理 WeChatUnsubscribeEvent，openId="+event.getOpenId()+";userId(sceneKey)="+event.getSceneKey());
        dismissedBindUser(event.getOpenId());
    }
    @EventListener@Async
    public void dealWeChatTextEvent(WeChatTextEvent event) {
        WeChatMsg msg = ContainerFactory.createDbCvo(WeChatMsg.class).filterBy(WeChatMsg::getMsgId, WhereRuleOper.eq, event.getMsgId()).ready().queryBean();
        if(null==msg){//避免重复保存
            msg = new WeChatMsg();
            msg.setMsgId(event.getMsgId());
            msg.setWxCreateTime(event.getCreateTime()*1000);
            msg.setFromUserName(event.getFromUserName());
            msg.setToUserName(WeChatConfig.appid);
            msg.setContent(event.getContent());
            ContainerFactory.getSession().store(msg);
        }

    }


    private static boolean bindUser(String openId,String userId) {
        RemexConstants.logger.info("WX Bind User：userId" + userId + "-------openId:" + openId);

        Assert.notNullAndEmpty(userId, ServiceCode.OPENID_INVALID,"userId不能为空");

        try {
            AuthUser user = ContainerFactory.getSession().createDbCvo(AuthUser.class).filterById(userId).ready().queryBean();
            if (null == user) {
                RemexConstants.logger.info("WX Bind User, this eventKey is not user id. eventKey-" + userId);
                return false;
            }
            boolean openIdUsed = ContainerFactory.getSession().createDbCvo(AuthUser.class).filterBy(AuthUser::getOpenid, WhereRuleOper.eq, openId).ready().query().getRecordCount() > 0;
            RemexConstants.logger.info("通过微信绑定用户，openId绑定状态 = " + openIdUsed+"; OpendId="+openId);
            if ((!Judgment.nullOrBlank(user) && !Judgment.nullOrBlank(user.getOpenid())) || openIdUsed) {  //用户已经绑定， 或者  openId 已经被使用
                return false;
            }

            user.setOpenid(openId);
            WeChatUserInfo weChatUserInfo = WeChatUserAPI.userInfo(openId);
            if (Judgment.nullOrBlank(weChatUserInfo)) {
                return false;
            }
            user.setNickname(weChatUserInfo.getNickname());
            user.setSex(weChatUserInfo.getSex());
            user.setCity(weChatUserInfo.getCity());
            user.setCountry(weChatUserInfo.getCountry());
            user.setProvince(weChatUserInfo.getProvince());
            user.setLanguage(weChatUserInfo.getLanguage());
            user.setHeadimgurl(weChatUserInfo.getHeadimgurl());
            user.setSubscribeTime(weChatUserInfo.getSubscribe_time());
            user.setUnionid(weChatUserInfo.getUnionid());
            user.setRemark(weChatUserInfo.getRemark());
            user.setGroupid(weChatUserInfo.getGroupid());
            ContainerFactory.getSession().store(user);
            return true;
        } catch (Throwable throwable) {
            RemexConstants.logger.error("绑定用于异常！", throwable);
            return false;
        }
    }

    public static boolean dismissedBindUser(String openId) {
        AuthUser user = ContainerFactory.getSession().createDbCvo(AuthUser.class).filterBy(AuthUser::getOpenid, WhereRuleOper.eq, openId).ready().queryBean();
        if (Judgment.nullOrBlank(user)) {
            return false;
        }
        user.setOpenid("");
        user.setNickname("");
        user.setSex("");
        user.setCity("");
        user.setCountry("");
        user.setProvince("");
        user.setLanguage("");
        user.setHeadimgurl("");
        user.setSubscribeTime("");
        user.setUnionid("");
        user.setRemark("");
        user.setGroupid("");
        ContainerFactory.getSession().store(user);
        return true;
    }
	private WeChatUser updateUserInfoWithWeChat(String openId) {
		WeChatUserInfo weChatUserInfo = WeChatUserAPI.userInfo(openId);

		if(null!=weChatUserInfo){
			//通过openid或unionid查看用户是否存在
			WeChatUser curUserInfo = ContainerFactory.createDbCvo(WeChatUser.class)
					.filterOper(WhereGroupOp.OR)
					.filterBy(WeChatUser::getUnionid,WhereRuleOper.eq,weChatUserInfo.getUnionid())
					.filterBy(WeChatUser::getOpenid, WhereRuleOper.eq, openId).ready().queryBean();
			if(Judgment.nullOrBlank(curUserInfo)) {
				curUserInfo = new WeChatUser();
			}
			ReflectUtil.copyProperties(curUserInfo,weChatUserInfo);//相同字段直接拷贝过去
			curUserInfo.setSubscribeTime(Long.parseLong(weChatUserInfo.getSubscribe_time()));//不同的字段
			ContainerFactory.getSession().store(curUserInfo).getStatus();
			return curUserInfo;
		}else
			return null;
	}

	public static boolean sendTemplateMsg(String openId, String remarkMsg){
		TemplateMessage templateMessage = new TemplateMessage();
		//模板Id,在模板信息中
		templateMessage.setTemplate_id(((Map<String,String>)RemexApplication.getBean("weixin")).get("msgTemplate"));
		templateMessage.setTouser(openId);
		TemplateMsg first = new TemplateMsg(remarkMsg,"#8cc474");
		TemplateMsg keyword1 = new TemplateMsg("服务消息","#173177");
		TemplateMsg keyword2 = new TemplateMsg(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),"#173177");
		TemplateMsg remark = new TemplateMsg("如有需要可微信留言，我公司客服将及时联系您。","#173177");
		LinkedHashMap<String, TemplateMsg> data = new LinkedHashMap<>();
		data.put("first",first);
		data.put("keyword1",keyword1);
		data.put("keyword2",keyword2);
		data.put("remark",remark);
		templateMessage.setData(data);
		return WeChatMessageAPI.messageTemplateSend(templateMessage);
	}
}
