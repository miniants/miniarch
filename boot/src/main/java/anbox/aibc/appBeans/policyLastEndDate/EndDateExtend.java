package anbox.aibc.appBeans.policyLastEndDate;

import anbox.aibc.appBeans.AncarExtend;

public class EndDateExtend extends AncarExtend{
	private static final long serialVersionUID = 7095504978004166937L;
	public EndDateExtend(boolean status, String msg) {
		super(status, msg);
	}
	public EndDateExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
	
}
