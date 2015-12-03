package anbox.aibc.appBeans.insureConfirm;

import anbox.aibc.appBeans.AncarExtend;

public class InsureConfirmExtend extends AncarExtend{
	private static final long serialVersionUID = -7529097006144099401L;
	private String loginFlag;
	
	public String getLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	public InsureConfirmExtend(boolean status, String msg) {
		super(status, msg);
	}
	public InsureConfirmExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
	
}
