package cn.com.qqbx.wx.appbeans.order;

import cn.com.qqbx.wx.appbeans.AncarExtend;

public class OrderSubmitExtend extends AncarExtend{
	private static final long serialVersionUID = -7534982115197600635L;
	public OrderSubmitExtend(boolean status, String msg) {
		super(status, msg);
	}
	public OrderSubmitExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}

}