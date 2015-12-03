package anbox.aibc.appBeans.mmbCenter;


public class MmbCenterRvo {
	private String nickname;
	private String username;
	private String level;
	private int customerCount;
	private int beanCount;
	private int coinCount;
	private int subLineCount;
	private String mobile;
	private String memberType;
//	private List<ToPayOrder> toPayOrders;
	
	public String getNickname() {
		return nickname;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
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
	public int getBeanCount() {
		return beanCount;
	}
	public void setBeanCount(int beanCount) {
		this.beanCount = beanCount;
	}
	public int getCoinCount() {
		return coinCount;
	}
	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}
	public int getSubLineCount() {
		return subLineCount;
	}
	public void setSubLineCount(int subLineCount) {
		this.subLineCount = subLineCount;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
