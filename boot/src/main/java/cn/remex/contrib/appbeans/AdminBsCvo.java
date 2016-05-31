package cn.remex.contrib.appbeans;


import cn.remex.db.model.cert.AuthUser;

/**
 *
 */
public class AdminBsCvo extends DataCvo {
	private AuthUser user;

	public AuthUser getUser() {
		return user;
	}

	public void setUser(AuthUser user) {
		this.user = user;
	}
}
