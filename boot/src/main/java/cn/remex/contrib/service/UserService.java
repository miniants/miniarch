package cn.remex.contrib.service;

import cn.remex.RemexConstants;
import cn.remex.contrib.appbeans.DataRvo;
import cn.remex.contrib.auth.AuthenticateBtx;
import cn.remex.core.CoreSvo;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import cn.remex.core.util.DateHelper;
import cn.remex.core.util.Judgment;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.model.SysMenu;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Sort;
import cn.remex.db.sql.SqlColumn;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;
import cn.remex.wechat.api.ticket.WeChatTicket;
import cn.remex.wechat.beans.ticket.QrcodeUrl;
import cn.remex.wechat.models.WeChatNotify;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.remex.db.sql.WhereGroupOp.OR;
import static cn.remex.db.sql.WhereRuleOper.eq;
import static cn.remex.db.sql.WhereRuleOper.gt;
import static cn.remex.db.sql.WhereRuleOper.lt;

/** 
 * @author liuhengyang 
 * @date 2016-4-21 下午6:20:59
 * @version 版本号码
 * @TODO 描述
 */
@BusinessService
public class UserService {
    @BusinessService
    public BsRvo userProfile() {
        String UID = CoreSvo.valCookieValue("UID");
        Assert.notNullAndEmpty(UID, ServiceCode.ACCOUNT_NOT_AUTH, "请先登录!");

        AuthUser curUser = ContainerFactory.createDbCvo(AuthUser.class)
                .filterBy(AuthUser::getUsername, eq, UID)
                .withList(AuthUser::getRoles, l -> l.withList(AuthRole::getMenus)).rowCount(1000)
                .ready().queryBean();

        Assert.notNullAndEmpty(curUser, ServiceCode.ACCOUNT_ERROR, "用户信息错误!");

        Map<String, SysMenu> menus = new HashMap<>();
        if(!Judgment.nullOrBlank(curUser.getRoles()))
            curUser.getRoles().forEach(
                    r-> {
                        if(r.getMenus()!=null)
                            r.getMenus().forEach(
                                    m -> menus.put(m.getId(), m));
                    });

        Map profile = new HashMap<>();
        profile.put("menus", menus);
        profile.put("curUser", curUser);


        return new BsRvo(ServiceCode.SUCCESS, "Query UserProfile Finished.", profile);
    }
    @BusinessService
    public BsRvo homeMenus(String rootMenu) {
        //获取三层菜单返回给前端
        DbCvo<SysMenu> dbCvo = ContainerFactory.getSession().createDbCvo(SysMenu.class)
                .orderBy(SysMenu::getNodeOrder, Sort.ASC)
                .withModel(SysMenu::getSupMenu, c -> c.withColumns(ModelableImpl::getId)
                        .filterBy(SysMenu::getNodeName, eq, Judgment.nullOrBlank(rootMenu) ? "ROOTMENU" : rootMenu))
                .withColumns().withList(SysMenu::getSubMenus, sm->sm.withColumns().withList(SysMenu::getSubMenus, SqlColumn::withColumns))
                .rowCount(1000);
        DbRvo dbRvo = dbCvo.ready().query();
        return new DataRvo(dbRvo);
    }
    @BusinessService(needAuth = false)
    public BsRvo logout(String username, String password, String redirect) {
        AuthenticateBtx.clearToken();
        return new BsRvo(ServiceCode.SUCCESS, "退出登录", null, "./login.html", "redirect");
    }
    @BusinessService
    public BsRvo resetPassword(String oldPassword,String newPassword){
        if(Judgment.nullOrBlank(oldPassword) || Judgment.nullOrBlank(newPassword))
            return new BsRvo(ServiceCode.ACCOUNT_PASSWORD_INVALID, "原密码/新密码不能为空!"); //,"10000"
        AuthUser authUser = AuthenticateBtx.obtainCurUser();
       /* if("123".equals(newPassword)){//测试
            return new BsRvo(true, "成功", "0000","1");
        }*/
        if(authUser.getPassword().equals(oldPassword)){
            authUser.setPassword(newPassword);
            ContainerFactory.getSession().store(authUser);
            AuthenticateBtx.clearToken();
            return new BsRvo(ServiceCode.SUCCESS, "成功");
        }else{
            return new BsRvo(ServiceCode.ACCOUNT_PASSWORD_NOTMATCH, "原密码不能正确！");
        }
    }
    @BusinessService
    public BsRvo modifyMobile(String oldPassword, String mobile) {
        Assert.notNullAndEmpty(oldPassword, ServiceCode.ACCOUNT_PASSWORD_INVALID, "密码不能为空!");
        Assert.notNullAndEmpty(mobile, ServiceCode.FAIL, "手机不能为空!");

        AuthUser authUser = AuthenticateBtx.obtainCurUser();
       /* if("123".equals(newPassword)){//测试
            return new BsRvo(true, "成功", "0000","1");
        }*/
        if (authUser.getPassword().equals(oldPassword)) {
            authUser.setMobile(mobile);
            ContainerFactory.getSession().store(authUser);
            return new BsRvo(ServiceCode.SUCCESS, "成功");
        } else {
            return new BsRvo(ServiceCode.ACCOUNT_PASSWORD_NOTMATCH, "原密码不能正确！");
        }
    }
    @BusinessService(needAuth = false)
    public BsRvo login(String username, String password,String redirect) {
        Assert.notNullAndEmpty(username, ServiceCode.ACCOUNT_USERNAME_INVALID,"用户名不能为空！");
        Assert.notNullAndEmpty(password, ServiceCode.ACCOUNT_PASSWORD_INVALID,"登录密码不能为空！");

	    AuthenticateBtx.checkToken();

        List<AuthUser> authUsers = ContainerFactory.createDbCvo(AuthUser.class)
                .withList(AuthUser::getRoles)
                .filterBy(AuthUser::getPassword, eq, password)
                .filterByGroup(g -> g
                        .filterOper(OR)
                        .filterBy(AuthUser::getUsername, eq, username)
                        .filterBy(AuthUser::getMobile, eq, username))
                .ready().query().obtainObjects(AuthUser.class);


        if(authUsers.size()==1) {
            Map<String, String> tokeMap = AuthenticateBtx.placeToken(authUsers.get(0));
            return new BsRvo(ServiceCode.SUCCESS,"登录成功",tokeMap);
        }else if (authUsers.size() > 1) {
            AuthenticateBtx.clearToken();
            return new BsRvo(ServiceCode.ACCOUNT_ERROR,"账号异常");
        }else {
            AuthenticateBtx.clearToken();
            return new BsRvo(ServiceCode.FAIL,"用户名/密码错误");
        }
    }

    @BusinessService(needAuth = false)
    public BsRvo obtainWeixinLoginUrl(){
        QrcodeUrl qrcodeUrl = WeChatTicket.qrcodeSceneUrl(WeiXinService.weixinLoginQrcodeUrl_SceneId, "120");
        Assert.notNullAndEmpty(qrcodeUrl, ServiceCode.FAIL,"获取微信二维码路径错误！");
        return new BsRvo(ServiceCode.SUCCESS, "以获取到绑定微信的二维码", qrcodeUrl);
    }
    @BusinessService(needAuth = false)
    public BsRvo loginByWeixin(String ticket) {
        Assert.notNullAndEmpty(ticket, ServiceCode.FAIL, "Ticket不能为空！");
	    WeChatNotify weChatNotify = ContainerFactory.createDbCvo(WeChatNotify.class)
			    .filterBy(WeChatNotify::getTicket, eq, ticket)
			    .filterBy(WeChatNotify::getMsgTime, gt, new Date().getTime()/1000-3*60)//有效期，ticket
	            .ready().queryBean();

        if(Judgment.nullOrBlank(weChatNotify)){
	        return new BsRvo(ServiceCode.FAIL,"未扫描二维码");
        }else if(new Date().getTime()/1000 - weChatNotify.getMsgTime()> 5*60){//五分钟内有效
            return new BsRvo(ServiceCode.FAIL,"扫描超时");
        }

	    AuthUser curAuthUser = ContainerFactory.createDbCvo(AuthUser.class)
			    .filterBy(AuthUser::getOpenid, eq, weChatNotify.getFromUserName())
			    .ready().queryBean();

		if(Judgment.nullOrBlank(curAuthUser)){
			return new BsRvo(ServiceCode.FAIL, "请使用绑定的微信号扫描二维码");
		}else{
			AuthenticateBtx.placeToken(curAuthUser);
			return new BsRvo(ServiceCode.SUCCESS, "登录成功", "/");
		}

    }
    @BusinessService
    public BsRvo bindWeixin(){
        Assert.nullOrEmpty(AuthenticateBtx.obtainCurUser().getOpenid(),ServiceCode.WEIXIN_HAS_BIND,"已经绑定");
        QrcodeUrl aa = WeChatTicket.qrcodeLimitStrUrl(AuthenticateBtx.obtainCurUser().getId());
        Assert.notNullAndEmpty(aa, ServiceCode.FAIL,"获取微信二维码路径错误！");
        return new BsRvo(ServiceCode.SUCCESS, "以获取到绑定微信的二维码",aa);
    }
    @BusinessService
    public BsRvo checkBindWeixin(String ticket,String createTime){

        Assert.notNullAndEmpty(ticket, ServiceCode.FAIL, "Ticket不能为空！");
        WeChatNotify weChatNotify = ContainerFactory.createDbCvo(WeChatNotify.class)
                .filterBy(WeChatNotify::getTicket, eq, ticket)
                .filterBy(WeChatNotify::getMsgTime, gt, Long.parseLong(createTime)/1000)
                .ready().queryBean();//查找5分钟内是否扫码

        if(Judgment.nullOrBlank(weChatNotify)){
            return new BsRvo(ServiceCode.WEIXIN_NOT_BIND,"未扫描二维码");
        }/*else if(new Date().getTime()/1000 - weChatNotify.getMsgTime()> 5*60){//五分钟内有效
            return new BsRvo(ServiceCode.FAIL,"扫描超时");
        }*/

	    //走到这里说明，已经扫描
	    RemexConstants.logger.info("检查用户状态, openId:" + weChatNotify.getFromUserName());
	    AuthUser curAuthUser = ContainerFactory.createDbCvo(AuthUser.class)
			    .filterBy(AuthUser::getOpenid, eq, weChatNotify.getFromUserName())
			    .ready().queryBean();
	    if (null == curAuthUser || !curAuthUser.getId().equals(AuthenticateBtx.obtainCurUser().getId())) {
		    return new BsRvo(ServiceCode.OPENID_INVALID, "扫描的微信号无效，可能已经绑定其他账号");
	    }else if(!Judgment.nullOrBlank(curAuthUser.getOpenid())){
		    return new BsRvo(ServiceCode.WEIXIN_HAS_BIND, "微信已绑定");
	    }else{
            return new BsRvo(ServiceCode.WEIXIN_NOT_BIND, "微信未绑定");
        }

//        AuthUser authUser1 = ContainerFactory.getSession().createDbCvo(AuthUser.class).filterById( AuthenticateBtx.obtainCurUser().getId()).ready().queryBean();
//        RemexConstants.logger.info("CHECK USER BIND WEIXIN Status, id:" + AuthenticateBtx.obtainCurUser().getId());
//        if(authUser1!=null && !Judgment.nullOrBlank(authUser1.getOpenid())){
//            return new BsRvo(ServiceCode.WEIXIN_HAS_BIND,"微信已绑定");
//        }else{
//            return new BsRvo(ServiceCode.WEIXIN_NOT_BIND,"微信未绑定");
//        }
    }
    @BusinessService
    public BsRvo unbindWeixin(String password){
        Assert.notNullAndEmpty(password, ServiceCode.FAIL,"密码不能为空");

        AuthenticateBtx.refreshCurUser();//更新缓存
        AuthUser loginUser = AuthenticateBtx.obtainCurUser();
        if(password.equals(loginUser.getPassword())){
            WeiXinService.dismissedBindUser(loginUser.getOpenid());
            AuthenticateBtx.refreshCurUser();//更新缓存
            return new BsRvo(ServiceCode.SUCCESS, "解除绑定成功","");
        }else{
            return new BsRvo(ServiceCode.ACCOUNT_PASSWORD_NOTMATCH, "原密码不能正确！");
        }
    }
}
