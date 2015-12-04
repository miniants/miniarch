package zbh.aias.model.policy;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;
/**
 * 保单客户关系信息
 * @author guoshaopeng
 * @since 20130712
 */
public class PlcyCustomer extends ModelableImpl{
	private static final long serialVersionUID = 6251154233432385623L;	
	@Column(length = 30)
	private String customerId;         //客户编码
	@Column(length = 10)
	private String personType;         //关系人类型
	@Column(length = 10)
	private String personClass;        //关系人类别
	@Column(length = 10)
	private String customerType;       //客户类型
	@Column(length = 10)
	private String vehicleRelation;   //车辆所有关系
	@Column(length = 100)
	private String name;               //姓名
	@Column(length = 5)
	private String sex;                //性别
	@Column(length = 30)
	private String birthday;           //生日
	@Column(length = 10)
	private String certType;           //证件类型
	@Column(length = 20)
	private String certNo;             //证件号码
	@Column(length = 20)
	private String mobile;             //手机号
	@Column(length = 20)
	private String tel;                //电话
	@Column(length = 50)
	private String email;              //邮箱
	@Column(length = 200)
	private String postAddr;           //寄送地址
	@Column(length = 10)
	private String postCode;           //寄送邮编
	@Column(length = 20)
	private String proxyMobile;        //指定手机号
	@Column(length = 20)
	private String proxyTel;           //电话
	@Column(length = 50)
	private String proxyEmail;         //指定邮箱
	@Column(length = 200)
	private String proxyPostAddr;      //指定寄送地址
	@Column(length = 10)
	private String proxyPostCode;      //指定寄送邮编
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		this.personType = personType;
	}
	public String getPersonClass() {
		return personClass;
	}
	public void setPersonClass(String personClass) {
		this.personClass = personClass;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getVehicleRelation() {
		return vehicleRelation;
	}
	public void setVehicleRelation(String vehicleRelation) {
		this.vehicleRelation = vehicleRelation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostAddr() {
		return postAddr;
	}
	public void setPostAddr(String postAddr) {
		this.postAddr = postAddr;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getProxyMobile() {
		return proxyMobile;
	}
	public void setProxyMobile(String proxyMobile) {
		this.proxyMobile = proxyMobile;
	}
	public String getProxyTel() {
		return proxyTel;
	}
	public void setProxyTel(String proxyTel) {
		this.proxyTel = proxyTel;
	}
	public String getProxyEmail() {
		return proxyEmail;
	}
	public void setProxyEmail(String proxyEmail) {
		this.proxyEmail = proxyEmail;
	}
	public String getProxyPostAddr() {
		return proxyPostAddr;
	}
	public void setProxyPostAddr(String proxyPostAddr) {
		this.proxyPostAddr = proxyPostAddr;
	}
	public String getProxyPostCode() {
		return proxyPostCode;
	}
	public void setProxyPostCode(String proxyPostCode) {
		this.proxyPostCode = proxyPostCode;
	}

}
