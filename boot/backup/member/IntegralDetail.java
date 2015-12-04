package zbh.aias.model.member;

import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;
import cn.remex.db.rsql.model.ModelableImpl;
import javax.persistence.Column;

import java.sql.Types;

public abstract class IntegralDetail  extends ModelableImpl{
	private static final long serialVersionUID = -5547067122192531820L;
	@Column(length = 50)
	private String memberId;
	@Column(length = 50)
	private String memberUsername;
	@Column(length = 20)
	private String amount;//收入支出的明细数，+为入-为出
	@Column(length = 20)
	private String beforeBalance;//结算前余额
	@Column(length = 20)
	private String afterBalance;//结算余额
	@Column(length = 20)
	private IntegralType type;
	@Column(length = 20)
	private String settleTime;//结算时间
	@Column(length = 20)
	private IntegralWay way;//积分获取途径
	@Column(length = 50)
	private String wayDesc;//积分获取途径
	@Column(length = 20)
	private String status;//积分状态
	@Column(length = 50)
	private String desc;
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSettleTime() {
		return settleTime;
	}
	public void setSettleTime(String settleTime) {
		this.settleTime = settleTime;
	}
	public IntegralWay getWay() {
		return way;
	}
	public void setWay(IntegralWay way) {
		this.way = way;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberUsername() {
		return memberUsername;
	}
	public void setMemberUsername(String memberRealname) {
		this.memberUsername = memberRealname;
	}
	public IntegralType getType() {
		return type;
	}
	public void setType(IntegralType type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBeforeBalance() {
		return beforeBalance;
	}
	public void setBeforeBalance(String beforeBalance) {
		this.beforeBalance = beforeBalance;
	}
	public String getAfterBalance() {
		return afterBalance;
	}
	public void setAfterBalance(String afterBalance) {
		this.afterBalance = afterBalance;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWayDesc() {
		return wayDesc;
	}
	public void setWayDesc(String wayDesc) {
		this.wayDesc = wayDesc;
	}
	
}
