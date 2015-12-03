package cn.com.qqbx.wx.appbeans.autoInsu;

public class AutoInsuRvo {
	private String code;   //合作人编码
	private String nameCn;   //合作人中文名称
	private String nameEn;   //合作人英文名称
	private String abbreviation;   //合作人简称
	private boolean defaultVote;//是否默认勾选
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNameCn() {
		return nameCn;
	}
	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public boolean isDefaultVote() {
		return defaultVote;
	}
	public void setDefaultVote(boolean defaultVote) {
		this.defaultVote = defaultVote;
	}
	
}
