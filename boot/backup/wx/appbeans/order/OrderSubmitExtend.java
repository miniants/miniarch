package zbh.wx.appbeans.order;

import zbh.wx.appbeans.AncarExtend;

public class OrderSubmitExtend extends AncarExtend{
	private static final long serialVersionUID = -7534982115197600635L;
	public OrderSubmitExtend(boolean status, String msg) {
		super(status, msg);
	}
	public OrderSubmitExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}

}
