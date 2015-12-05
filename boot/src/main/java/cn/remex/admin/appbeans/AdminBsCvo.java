package cn.remex.admin.appbeans;


import cn.remex.db.model.cert.AuthUser;
import cn.remex.web.service.BsCvo;

/**
 *
 */
public class AdminBsCvo extends BsCvo {
	private AuthUser user;

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
}
