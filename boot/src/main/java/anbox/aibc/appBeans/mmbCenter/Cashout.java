package anbox.aibc.appBeans.mmbCenter;

import anbox.aibc.AiwbConsts.ApplyCashoutStatus;


public class Cashout {
	private String applyMember;
	private ApplyCashoutStatus applyStatus;
	private String applyTime;
	private String bankCardNumber;
	private String bankOutlets;
	private String realname;
	public String getApplyMember() {
		return applyMember;
	}
	public void setApplyMember(String applyMember) {
		this.applyMember = applyMember;
	}
	public ApplyCashoutStatus getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(ApplyCashoutStatus applyStatus) {
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

	
	
}
