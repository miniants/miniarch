package zbh.aias.model.system;

import cn.remex.db.rsql.model.ModelableImpl;

public class CityCode extends ModelableImpl {
	
	/**
	 * 省份城市表
	 * @authorzhangzhixin
	 * @since  2014-11-23
	 */
	private static final long serialVersionUID = -3613708537755035188L;
	private String regionCode;
	private String regionName;
	private	String cityLevel;  //城市级别
	private  String code;			//城市编码
	private  String  supCityCode;	//上级城市编码
	private	String  name;    //名称	
  	private	String shortName;		//简称
  	private	String shortNameSpell;		//简称
	private	String provinceShortName;  // 所属省份简称
	public String getCityLevel() {
		return cityLevel;
	}
	public void setCityLevel(String cityLevel) {
		this.cityLevel = cityLevel;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSupCityCode() {
		return supCityCode;
	}
	public void setSupCityCode(String supCityCode) {
		this.supCityCode = supCityCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getProvinceShortName() {
		return provinceShortName;
	}
	public void setProvinceShortName(String provinceShortName) {
		this.provinceShortName = provinceShortName;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getShortNameSpell() {
		return shortNameSpell;
	}
	public void setShortNameSpell(String shortNameSpell) {
		this.shortNameSpell = shortNameSpell;
	}
	
}
