package zbh.aibc.appBeans.mmbCenter;

public class PwdCvo {
	private String originPassword;
	private String newPassword;
	private String mobile;
	private String phoneCheckToken;//短信校验码
	private String checkCode;//短信校验码
	public String getOriginPassword() {
		return originPassword;
	}
	public void setOriginPassword(String originPassword) {
		this.originPassword = originPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhoneCheckToken() {
		return phoneCheckToken;
	}
	public void setPhoneCheckToken(String phoneCheckToken) {
		this.phoneCheckToken = phoneCheckToken;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
}
