package anbox.aibc.appBeans.login;

public class PwdResetCvoAndRvo {
	private String username;
	private String checkMobile;
	private String phoneCheckToken;//短信校验码
	private String checkCode;//短信校验码
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCheckMobile() {
		return checkMobile;
	}
	public void setCheckMobile(String checkMobile) {
		this.checkMobile = checkMobile;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
