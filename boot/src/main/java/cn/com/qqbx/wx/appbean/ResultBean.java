package cn.com.qqbx.wx.appbean;

import cn.remex.bs.Extend;

public class ResultBean extends Extend {

	public ResultBean(boolean status, String msg) {
		super(status, msg);
	}

	public ResultBean(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
}
