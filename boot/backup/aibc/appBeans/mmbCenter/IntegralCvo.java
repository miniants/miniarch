package zbh.aibc.appBeans.mmbCenter;


public class IntegralCvo {
	private String bankCardNumber;//银行卡号
	private String bankCardHolder;//持卡人姓名
	private String bankOutlets;//银行网点
	private String mobile;//已注册手机
	private String checkCode;//短信校验码
	private String phoneCheckToken;//短信校验码
	private String amountOfMoney;
	private String transType;
	private String zfbAccount;//支付宝账号
	private String wxAccount;//微信账号
	
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getAmountOfMoney() {
		return amountOfMoney;
	}
	public void setAmountOfMoney(String amountOfMoney) {
		this.amountOfMoney = amountOfMoney;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getPhoneCheckToken() {
		return phoneCheckToken;
	}
	public void setPhoneCheckToken(String phoneCheckToken) {
		this.phoneCheckToken = phoneCheckToken;
	}
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
	public String getBankCardHolder() {
		return bankCardHolder;
	}
	public void setBankCardHolder(String bankCardHolder) {
		this.bankCardHolder = bankCardHolder;
	}
	
}
