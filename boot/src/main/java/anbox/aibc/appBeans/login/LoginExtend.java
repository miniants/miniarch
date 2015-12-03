package anbox.aibc.appBeans.login;

import anbox.aibc.appBeans.AncarExtend;

public class LoginExtend extends AncarExtend{
	private static final long serialVersionUID = 8241256987406479582L;
	public LoginExtend(boolean status, String msg) {
		super(status, msg);
	}
	public LoginExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
}
