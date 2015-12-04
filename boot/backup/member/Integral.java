package zbh.aias.model.member;

import cn.remex.db.rsql.model.ModelableImpl;
import javax.persistence.Column;

import java.sql.Types;

public class Integral  extends ModelableImpl{
	private static final long serialVersionUID = -4072236051416019030L;
	private Member member;//          所属会员
	private int totalCarCoins;//   卡币累计总数
	private int totalCarBeans;//   卡豆累计总数
	private int carCoins;//        当前卡币总数
	private int carBeans;//        当前卡豆总数
	@Column(length = 20)
	private String carBeanStartTime;//卡豆积累起始时间
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public int getTotalCarCoins() {
		return totalCarCoins;
	}
	public void setTotalCarCoins(int totalCarCoins) {
		this.totalCarCoins = totalCarCoins;
	}
	public int getTotalCarBeans() {
		return totalCarBeans;
	}
	public void setTotalCarBeans(int totalCarBeans) {
		this.totalCarBeans = totalCarBeans;
	}
	public int getCarCoins() {
		return carCoins;
	}
	public void setCarCoins(int carCoins) {
		this.carCoins = carCoins;
	}
	public int getCarBeans() {
		return carBeans;
	}
	public void setCarBeans(int carBeans) {
		this.carBeans = carBeans;
	}
	public String getCarBeanStartTime() {
		return carBeanStartTime;
	}
	public void setCarBeanStartTime(String carBeanStartTime) {
		this.carBeanStartTime = carBeanStartTime;
	}
	
}
