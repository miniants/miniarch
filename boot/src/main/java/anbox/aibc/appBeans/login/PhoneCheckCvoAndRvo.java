package anbox.aibc.appBeans.login;

public class PhoneCheckCvoAndRvo {
	private String phoneNo;
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	private String phoneCheckCode;
	private String phoneCheckToken;
	public String getPhoneCheckToken() {
		return phoneCheckToken;
	}
	public void setPhoneCheckToken(String phoneCheckToken) {
		this.phoneCheckToken = phoneCheckToken;
	}
	public String getPhoneCheckCode() {
		return phoneCheckCode;
	}
	public void setPhoneCheckCode(String invitationCode) {
		this.phoneCheckCode = invitationCode;
	}
	
}
