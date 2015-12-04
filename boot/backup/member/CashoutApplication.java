/**
 * 
 */
package zbh.aias.model.member;

import anbox.aibc.AiwbConsts.ApplyCashoutStatus;
import cn.remex.db.rsql.model.ModelableImpl;

/** 
 * @author liuhengyang 
 * @date 2015-2-12 上午12:39:12
 * @version 版本号码
 * @TODO 描述
 */
public class CashoutApplication extends ModelableImpl {

	private static final long serialVersionUID = -4405713510272838198L;
	private String applyMember;
	private ApplyCashoutStatus applyStatus;
	private String applyTime;
	private String bankCardNumber;
	private String bankOutlets;
	private String realname;
	private String transCashAmount;
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
	public String getTransCashAmount() {
		return transCashAmount;
	}
	public void setTransCashAmount(String transCashAmount) {
		this.transCashAmount = transCashAmount;
	}
	public String getApplyMember() {
		return applyMember;
	}
	public ApplyCashoutStatus getApplyStatus() {
		return applyStatus;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public String getBankOutlets() {
		return bankOutlets;
	}
	public String getRealname() {
		return realname;
	}
	public void setApplyMember(String applyMember) {
		this.applyMember = applyMember;
	}
	public void setApplyStatus(ApplyCashoutStatus applyStatus) {
		this.applyStatus = applyStatus;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public void setBankOutlets(String bankOutlets) {
		this.bankOutlets = bankOutlets;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
}
