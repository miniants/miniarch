package zbh.aibc.appBeans;

import zbh.remex.bs.Extend;

public class AncarExtend extends Extend{
	private static final long serialVersionUID = -4295732801637122412L;
	public AncarExtend() {
		super(false, "");
	}
	public AncarExtend(boolean status, String msg) {
		super(status, msg);
	}
	public AncarExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
	private String userId;
	private String username;
	private String nickname;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
