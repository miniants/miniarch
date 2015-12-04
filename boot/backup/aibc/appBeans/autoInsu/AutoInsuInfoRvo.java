package zbh.aibc.appBeans.autoInsu;

import java.util.Map;

public class AutoInsuInfoRvo {
	//响应
	private String cityCode;
	private Map<String,AutoInsuRvo> insuComMap;
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public Map<String, AutoInsuRvo> getInsuComMap() {
		return insuComMap;
	}
	public void setInsuComMap(Map<String, AutoInsuRvo> insuComMap) {
		this.insuComMap = insuComMap;
	}
}
