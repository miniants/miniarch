package cn.com.qqbx.wx.appbeans.delivery;

import cn.com.qqbx.wx.appbeans.AncarExtend;

public class DeliveryExtend extends AncarExtend{
	private static final long serialVersionUID = -8721926547308908995L;
	public DeliveryExtend(boolean status, String msg) {
		super(status, msg);
	}
	public DeliveryExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
}
