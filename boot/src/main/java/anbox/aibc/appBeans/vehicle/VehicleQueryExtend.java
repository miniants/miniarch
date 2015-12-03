package anbox.aibc.appBeans.vehicle;

import anbox.aibc.appBeans.AncarExtend;

public class VehicleQueryExtend extends AncarExtend{
	private static final long serialVersionUID = -6405573346255023539L;
	public VehicleQueryExtend(boolean status, String msg) {
		super(status, msg);
	}
	public VehicleQueryExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
}
