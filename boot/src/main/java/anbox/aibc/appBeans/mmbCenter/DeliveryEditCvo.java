package anbox.aibc.appBeans.mmbCenter;

public class DeliveryEditCvo {
	private String id;
	private String postProvince;
	private String postCity;
	private String postAddress;
	private String postMobile;
	private String postContact;
	private String postTel;
	private String postEmail;
	private String postCode;
	private String defaultFlag;//默认地址标志，默认为Y,否则为N
	public String getPostProvince() {
		return postProvince;
	}
	public void setPostProvince(String postProvince) {
		this.postProvince = postProvince;
	}
	public String getPostCity() {
		return postCity;
	}
	public void setPostCity(String postCity) {
		this.postCity = postCity;
	}
	public String getPostAddress() {
		return postAddress;
	}
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}
	public String getPostMobile() {
		return postMobile;
	}
	public void setPostMobile(String postMobile) {
		this.postMobile = postMobile;
	}
	public String getPostContact() {
		return postContact;
	}
	public void setPostContact(String postContact) {
		this.postContact = postContact;
	}
	public String getPostEmail() {
		return postEmail;
	}
	public void setPostEmail(String postEmail) {
		this.postEmail = postEmail;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPostTel() {
		return postTel;
	}
	public void setPostTel(String postTel) {
		this.postTel = postTel;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
}