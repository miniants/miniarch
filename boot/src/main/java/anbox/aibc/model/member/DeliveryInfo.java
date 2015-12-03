package anbox.aibc.model.member;

import java.sql.Types;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import anbox.aibc.model.CommModel;
import cn.remex.db.sql.SqlTypeAnnotation;
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"member","postAddress","postMobile","postContact"}
				)
})
public class DeliveryInfo extends CommModel{
	private static final long serialVersionUID = -2148341319432494924L;
	private Member member;//所属会员
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String postAddress;//寄送地址
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postMobile;//寄送联系人电话
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String postContact;//联系人
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postProvince;//寄送省份
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postCity;//寄送城市
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String postEmail;              //邮箱
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String postCode;           //寄送邮编
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postTel;           //电话
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
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
