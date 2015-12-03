package cn.com.qqbx.wx.appbean;

import java.io.Serializable;
/**
 * 车辆信息
 * @author guoshaopeng
 * @since  2013-10-14
 */
public class WxOrder implements Serializable {

	/**
	 * 
	 */
	private String id;
	private String bizOrderId;
	private String prdtAmount;
	private String prdtType;
	private String prdtName;
	private String contactName;             
	private String email;              //邮箱
	private String postAddr;           //寄送地址
	private String postCode;           //寄送邮编
	private String mobile;        //指定手机号
	private String tel;           //电话
	private String openId;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getBizOrderId() {
		return bizOrderId;
	}
	public void setBizOrderId(String bizOrderId) {
		this.bizOrderId = bizOrderId;
	}
	public String getPrdtAmount() {
		return prdtAmount;
	}
	public void setPrdtAmount(String prdtAmount) {
		this.prdtAmount = prdtAmount;
	}
	public String getPrdtType() {
		return prdtType;
	}
	public void setPrdtType(String prdtType) {
		this.prdtType = prdtType;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
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
	public String getPrdtName() {
		return prdtName;
	}
	public void setPrdtName(String prdtName) {
		this.prdtName = prdtName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}

