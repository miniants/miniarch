package anbox.aibc.appBeans.ActualValue;

import anbox.aibc.appBeans.AncarExtend;

public class ActualValueExtend extends AncarExtend{
	private static final long serialVersionUID = -5817527668990714966L;
	public ActualValueExtend(boolean status, String msg) {
		super(status, msg);
	}
	public ActualValueExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
	
}