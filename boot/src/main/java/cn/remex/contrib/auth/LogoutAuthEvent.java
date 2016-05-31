package cn.remex.contrib.auth;

import cn.remex.core.RemexEvent;
import cn.remex.db.model.cert.AuthUser;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/10 0010.
 */
public class LogoutAuthEvent extends RemexEvent {

    private static final long serialVersionUID = 8729476063946441385L;
    private AuthUser authUser;

    public AuthUser getAuthUser() {
        return authUser;
    }

    public LogoutAuthEvent(String source, AuthUser authUser) {
        super(source);
        this.authUser = authUser;
    }



}
