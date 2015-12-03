package cn.com.qqbx.wx.appbeans.city;
import java.io.Serializable;
import java.util.List;

public class WxCitySelect implements Serializable{
	private List<WxCityCode> wxCityCodes;

	public List<WxCityCode> getWxCityCodes() {
		return wxCityCodes;
	}

	public void setWxCityCodes(List<WxCityCode> wxCityCodes) {
		this.wxCityCodes = wxCityCodes;
	}
	
}                                                                              
