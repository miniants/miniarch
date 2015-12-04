package zbh.wx.appbeans.mmbCenter;

import java.util.List;

public class MmbCenterRvo {
	private String nickname;
	private String username;
	private String level;
	private int customerCount;
	private int coinCount;
	private int beanCount;
	private int subLineCount;
	private List<ToPayOrder> toPayOrders;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}
	public int getCoinCount() {
		return coinCount;
	}
	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}
	public int getBeanCount() {
		return beanCount;
	}
	public void setBeanCount(int beanCount) {
		this.beanCount = beanCount;
	}
	public List<ToPayOrder> getToPayOrders() {
		return toPayOrders;
	}
	public void setToPayOrders(List<ToPayOrder> toPayOrders) {
		this.toPayOrders = toPayOrders;
	}
	public int getSubLineCount() {
		return subLineCount;
	}
	public void setSubLineCount(int subLineCount) {
		this.subLineCount = subLineCount;
	}
	
}
