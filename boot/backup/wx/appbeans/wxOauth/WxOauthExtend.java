package zbh.wx.appbeans.wxOauth;

import zbh.wx.appbeans.AncarExtend;

public class WxOauthExtend extends AncarExtend {
	private static final long serialVersionUID = -6405573346255023539L;
	public WxOauthExtend(boolean status, String msg) {
		super(status, msg);
	}
	public WxOauthExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
}
