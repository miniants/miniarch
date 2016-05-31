package cn.remex.contrib.auth;

import cn.remex.core.CoreSvo;
import cn.remex.core.RemexApplication;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.Param;
import cn.remex.core.util.RequestHelper;
import cn.remex.db.ContainerFactory;
import cn.remex.db.model.SysUri;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlColumn;
import cn.remex.db.sql.WhereRuleOper;
import cn.remex.web.service.BusinessService;
import cn.remex.web.service.ServiceFactory;
import cn.remex.contrib.service.DataService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by LIU on 15/11/26.
 */
public class AuthenticateBtx {
    private static final String KEY_UID = "UID";
    private static final String KEY_TKN = "TKN";
    private static final String KEY_TS = "TS";

    public static boolean authenticate() {
        return true;
    }

    /*public static boolean checkOpenid(String uri, String uriParams) {
        if(Judgment.nullOrBlank(uriParams)){
           return false;
        }
        if(!Judgment.nullOrBlank(uri) && uri.contains("/smvc/AdminService/wxmenu.jsp") ){
            String code = uriParams.substring(uriParams.indexOf("code=")+7,uriParams.length());
            int i= uriParams.indexOf("&");
            code = i!=-1?code.substring(0,i):code;
            String openid = "";
            if(Judgment.nullOrBlank(openid) || openid.length()==0){
                return false;
            }else{
                AuthUser authUser = ContainerFactory.getSession().createDbCvo(AuthUser.class)
                        .filterBy(AuthUser::getOpenid,WhereRuleOper.eq,openid)
                        .ready().query().obtainBean();
                if(!Judgment.nullOrBlank(authUser)){
                    placeToken(authUser.getUsername());
                    return true;
                }
            }
        }
        return false;
    }*/

    public static boolean checkToken() {
        String UID = CoreSvo.valCookieValue(KEY_UID); // 用户
        String TS = CoreSvo.valCookieValue(KEY_TS); // 时间戳
        String TKN = CoreSvo.valCookieValue(KEY_TKN); // 凭证

        return !(Judgment.nullOrBlank(UID) || Judgment.nullOrBlank(TS) || Judgment.nullOrBlank(TKN) || !TKN.equals(generateToken(UID, TS)));
    }

    public static Map<String,String> placeToken(AuthUser authUser) {
        String username = authUser.getUsername();
        String TS = String.valueOf(LocalTime.now().hashCode());
        String UID = username; // 用户
        String TKN = generateToken(UID, TS); // 凭证
        CoreSvo.putCookie(KEY_UID, UID); // 用户
        CoreSvo.putCookie(KEY_TS, TS); // 时间戳
        CoreSvo.putCookie(KEY_TKN, TKN); // 凭证

        Map<String, String> tokeMap = new HashMap<>();
        tokeMap.put(KEY_UID, UID);
        tokeMap.put(KEY_TS, TS);
        tokeMap.put(KEY_TKN, TKN);

        AuthenticateBtx.refreshCurUser(username);//登录时清理用户缓存

        RemexApplication.publishEvent(new LoginAuthEvent("LoginAuthEvent", authUser));
        return tokeMap;
    }

    public static void clearToken() {
        AuthUser authUser = AuthenticateBtx.obtainCurUser();
        CoreSvo.putCookie(KEY_UID, null); // 用户
        CoreSvo.putCookie(KEY_TS, null); // 时间戳
        CoreSvo.putCookie(KEY_TKN, null); // 凭证

        RemexApplication.publishEvent(new LogoutAuthEvent("LogoutAuthEvent", authUser));
    }

    public static String generateToken(String username, String TS) {
        String IP = RequestHelper.getClientIP((HttpServletRequest) CoreSvo.valLocal(CoreSvo.HTTP_REQUEST_KEY));
        return String.valueOf(KEY_UID.hashCode()) + username.hashCode() + "-" + KEY_TS.hashCode() + TS.hashCode() + "-" + IP.hashCode();
    }

    public static Map<String, AuthUser> users = new HashMap<>();
    public static Map<String, AuthUser> wxusers = new HashMap<>();

    public  static void refreshCurUser(){
        String username = CoreSvo.valCookieValue(KEY_UID);
        if(username != null){
            refreshCurUser(username);
        }
    }
    public  static void refreshCurUser(String username){
        if(username != null){
            AuthUser curUser = ContainerFactory.getSession().createDbCvo(AuthUser.class)
                    .filterBy(AuthUser::getUsername, WhereRuleOper.eq, username)
                    .withList(AuthUser::getRoles)
                    .rowCount(10000)
                    .ready().query()
                    .obtainBean();
            users.remove(username);
            users.put(username, curUser);
        }
    }

    public static AuthUser obtainCurUser() {
        String username = CoreSvo.valCookieValue(KEY_UID);
        AuthUser curUser;

        if (null == (curUser = users.get(username))) {
            List<AuthUser> ret = ContainerFactory.createDbCvo(AuthUser.class)
                    .filterBy(AuthUser::getUsername, WhereRuleOper.eq, username)
                    .withList(AuthUser::getRoles, SqlColumn::withColumns)
                    .rowCount(10000)
                    .ready().query()
                    .obtainBeans();
            Assert.isTrue(ret.size() <= 1, ServiceCode.FAIL, "用户数据出现重复异常,请联系管理员!");
            if(ret.size() == 1){
                curUser = ret.get(0);
                users.put(username, curUser);
            }
        }
        return curUser;

    }

    /**
     * 根据serviceFactory的bs / RsqlCore中的orm and web.xml的配置生成系统服务接口的SysUris列表.
     *
     * @return
     */
    public static List<SysUri> obtainDefaultUris() {
        //String mvcRoot = "smvc/";
        //String DataService = "DataService";
        SysUriMapToRole = null;
        List<SysUri> sysUris = new ArrayList<>();

        //Bs部分
        Param<BusinessService> bsan = new Param<>(null);
        ServiceFactory.getBsMap().forEach((bsName, v) -> ReflectUtil.getAllMethods(v).forEach((methodName, sameNameMethods) ->
                        sameNameMethods.stream().filter(m -> (bsan.param = ReflectUtil.getAnnotation(m, BusinessService.class)) != null)
                                .forEach(m -> {
                                    SysUri sysUri = new SysUri();
                                    sysUri.setUri("/" + bsName + "/" + methodName);
                                    sysUri.setUriName(bsan.param.name());
                                    sysUri.setUriDesc(bsan.param.desc());
                                    sysUris.add(sysUri);
                                })
        ));

        //orm部分的DataTableService
        RDBManager.getLocalSpaceConfig().getOrmBeans().forEach((beanName, beanClass) -> ReflectUtil.getAllMethods(DataService.class).forEach((methodName, sameNameMethods) -> {
            sameNameMethods.stream().filter(m -> (bsan.param = ReflectUtil.getAnnotation(m, BusinessService.class)) != null)
                    .forEach(m -> {
                        SysUri sysUri = new SysUri();
                        sysUri.setUri("/" + DataService.class.getSimpleName() + "/" + beanName + "/" + methodName);
                        sysUris.add(sysUri);
                    });
        }));


        return sysUris;
    }

    public static Map<String, SysUri> obtainSysUri() {
        return null; //TODO
    }

    //uri -> (role.id,?)
    public static Map<String, Map<String,?>> SysUriMapToRole = null;

    public static Map<String, Map<String, ?>> obtainSysUriMapToRole() {
        if(null!=SysUriMapToRole)
            return SysUriMapToRole;
        AuthRole root = ContainerFactory.createDbCvo(AuthRole.class).filterBy(AuthRole::getName, WhereRuleOper.eq, "root").ready().query().obtainBean();
        SysUriMapToRole = new HashMap<>();
        ContainerFactory.getSession().createDbCvo(SysUri.class).withList(SysUri::getRoles).rowCount(10000)
                .ready().query().obtainObjects(SysUri.class)
                .forEach(sysUri -> {
                    Map<String, ?> curRoles = SysUriMapToRole.get(sysUri.getUri());
                    if (null == curRoles) {
                        curRoles = new HashMap<>();
                        SysUriMapToRole.put(sysUri.getUri(), curRoles);
                        curRoles.put(root.getId(), null);
                    }
                    final Map<String, ?> finalCurRoles = curRoles;
                    if (null != sysUri.getRoles())
                        sysUri.getRoles().forEach(role -> finalCurRoles.put(role.getId(), null));

                });

        return SysUriMapToRole;
    }

    public static boolean isRootUser(){
        AuthUser authUser = AuthenticateBtx.obtainCurUser();
        if (authUser != null)
            for(int i=0; i<authUser.getRoles().size(); i++){
                if("root".equals(authUser.getRoles().get(i).getName())){
                    return true;
                }
            }
        return  false;
    }

}
