package anbox.aibc.appBeans.mmbCenter;

public class PersonalRvo {
	private String nickname;
	private String realname;
	private String idNo;
	private String mobile;
	private String agntCertificateNo;//代理人资格证号
	private String address;
	private String province;
	private String city;
	private String email;
	private String ownedInsuComName;//所属保险公司
	private String entirePeriod;//从业年限
	private String memberType;
	
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public String getEntirePeriod() {
		return entirePeriod;
	}
	public void setEntirePeriod(String entirePeriod) {
		this.entirePeriod = entirePeriod;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAgntCertificateNo() {
		return agntCertificateNo;
	}
	public void setAgntCertificateNo(String agntCertificateNo) {
		this.agntCertificateNo = agntCertificateNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOwnedInsuComName() {
		return ownedInsuComName;
	}
	public void setOwnedInsuComName(String ownedInsuComName) {
		this.ownedInsuComName = ownedInsuComName;
	}
	
}
