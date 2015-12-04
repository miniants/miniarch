package zbh.aibc.appBeans.mmbCenter;

public class SecurityCvo {
	private String oldMobile;
	private String newMobile;
	private String checkMobile;
	private String email;
	private String phoneCheckToken;//短信校验码
	private String checkCode;//短信校验码
	public String getOldMobile() {
		return oldMobile;
	}
	public void setOldMobile(String oldMobile) {
		this.oldMobile = oldMobile;
	}
	public String getNewMobile() {
		return newMobile;
	}
	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getCheckMobile() {
		return checkMobile;
	}
	public void setCheckMobile(String checkMobile) {
		this.checkMobile = checkMobile;
	}
	
}
