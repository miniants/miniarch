package cn.com.qqbx.wx.appbean;

import java.io.Serializable;
import java.util.List;


public class WxVehicleList implements Serializable{
	private List<WxVehicle> wxVehicles;

	public List<WxVehicle> getWxVehicles() {
		return wxVehicles;
	}

	public void setWxVehicles(List<WxVehicle> wxVehicles) {
		this.wxVehicles = wxVehicles;
	}
	
}
