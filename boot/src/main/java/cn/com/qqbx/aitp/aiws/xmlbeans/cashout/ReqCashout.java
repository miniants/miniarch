package cn.com.qqbx.aitp.aiws.xmlbeans.cashout;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ReqCashout")
public class ReqCashout implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2909821607660254938L;
	private String applyMember;
	private String applyStatus;
	private String applyTime;
	private String bankCardNumber;
	private String bankOutlets;
	private String realname;
	private String transCashAmount;
	private String transType;
	private String mobile;
	private String cashoutId;
	private String zfbAccount;//支付宝账号
	private String wxAccount;//微信账号
	
	public String getZfbAccount() {
		return zfbAccount;
	}
	public void setZfbAccount(String zfbAccount) {
		this.zfbAccount = zfbAccount;
	}
	public String getWxAccount() {
		return wxAccount;
	}
	public void setWxAccount(String wxAccount) {
		this.wxAccount = wxAccount;
	}
	public String getCashoutId() {
		return cashoutId;
	}
	public void setCashoutId(String cashoutId) {
		this.cashoutId = cashoutId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getApplyMember() {
		return applyMember;
	}
	public void setApplyMember(String applyMember) {
		this.applyMember = applyMember;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getBankOutlets() {
		return bankOutlets;
	}
	public void setBankOutlets(String bankOutlets) {
		this.bankOutlets = bankOutlets;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getTransCashAmount() {
		return transCashAmount;
	}
	public void setTransCashAmount(String transCashAmount) {
		this.transCashAmount = transCashAmount;
	}
	
}
