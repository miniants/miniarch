package zbh.wx.appbeans.autoInsu;

import zbh.wx.appbeans.AncarExtend;

public class AutoInsuExtend  extends AncarExtend{
	private static final long serialVersionUID = -1787508286875348361L;
	public AutoInsuExtend(boolean status, String msg) {
		super(status, msg);
	}
	public AutoInsuExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
	
}
