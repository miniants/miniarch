package anbox.aibc.model.member;

import java.sql.Types;

import anbox.aibc.AiwbConsts.IntegralType;
import anbox.aibc.AiwbConsts.IntegralWay;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public abstract class IntegralDetail  extends ModelableImpl{
	private static final long serialVersionUID = -5547067122192531820L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String memberId;
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String memberUsername;
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String amount;//收入支出的明细数，+为入-为出
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String beforeBalance;//结算前余额
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String afterBalance;//结算余额
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private IntegralType type;
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String settleTime;//结算时间
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private IntegralWay way;//积分获取途径
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String wayDesc;//积分获取途径
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String status;//积分状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
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
