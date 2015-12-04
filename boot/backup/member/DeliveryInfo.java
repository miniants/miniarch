package zbh.aias.model.member;

import javax.persistence.Column;
import zbh.aias.model.CommModel;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"member","postAddress","postMobile","postContact"}
				)
})
public class DeliveryInfo extends CommModel {
	private static final long serialVersionUID = -2148341319432494924L;
	private Member member;//所属会员
	@Column(length = 100)
	private String postAddress;//寄送地址
	@Column(length = 20)
	private String postMobile;//寄送联系人电话
	@Column(length = 50)
	private String postContact;//联系人
	@Column(length = 20)
	private String postProvince;//寄送省份
	@Column(length = 20)
	private String postCity;//寄送城市
	@Column(length = 50)
	private String postEmail;              //邮箱
	@Column(length = 10)
	private String postCode;           //寄送邮编
	@Column(length = 20)
	private String postTel;           //电话
	@Column(length = 10)
	private String defaultFlag;           //默认地址，默认为Y,否则为N
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
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
	public String getPostEmail() {
		return postEmail;
	}
	public void setPostEmail(String postEmail) {
		this.postEmail = postEmail;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPostTel() {
		return postTel;
	}
	public void setPostTel(String postTel) {
		this.postTel = postTel;
	}
	public String getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
}
