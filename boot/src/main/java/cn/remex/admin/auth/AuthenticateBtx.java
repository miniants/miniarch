package cn.remex.admin.auth;

import cn.remex.core.CoreSvo;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.RequestHelper;
import cn.remex.db.ContainerFactory;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.sql.WhereRuleOper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

/**
 * Created by LIU on 15/11/26.
 */
public class AuthenticateBtx {
    private static final String KEY_UID = "UID";
    private static final String KEY_TKN = "TKN";
    private static final String KEY_TS =  "TS";

    public static boolean authenticate() {
        return true;
    }

    public static boolean checkToken(){
        String UID = CoreSvo.valCookieValue(KEY_UID); // 用户
        String TS = CoreSvo.valCookieValue(KEY_TS); // 时间戳
        String TKN = CoreSvo.valCookieValue(KEY_TKN); // 凭证

        return !(Judgment.nullOrBlank(UID) || Judgment.nullOrBlank(TS) || Judgment.nullOrBlank(TKN) || !TKN.equals(generateToken(UID,TS)));
    }

    public static void placeToken(String username){
        String TS = String.valueOf(LocalTime.now().hashCode());
        String UID = username; // 用户
        String TKN = generateToken(UID, TS); // 凭证
        CoreSvo.putCookie(KEY_UID, UID); // 用户
        CoreSvo.putCookie(KEY_TS, TS); // 时间戳
        CoreSvo.putCookie(KEY_TKN, TKN); // 凭证
    }

    public static void clearToken(){
        CoreSvo.putCookie(KEY_UID, null); // 用户
        CoreSvo.putCookie(KEY_TS, null); // 时间戳
        CoreSvo.putCookie(KEY_TKN, null); // 凭证
    }

    public static String generateToken(String username,String TS){
        String IP = RequestHelper.getClientIP((HttpServletRequest) CoreSvo.valLocal(CoreSvo.HTTP_REQUEST_KEY));
        return new StringBuilder().append(KEY_UID.hashCode()).append(username.hashCode()).append("-").append(KEY_TS.hashCode()).append(TS.hashCode()).append("-").append(IP.hashCode()).toString();
    }

    public static AuthUser obtainCurUser(){
        String username = CoreSvo.valCookieValue(KEY_UID);
        return ContainerFactory.createDbCvo(AuthUser.class).filterBy(AuthUser::getUsername, WhereRuleOper.eq,username).ready().queryBean();
    }

}
