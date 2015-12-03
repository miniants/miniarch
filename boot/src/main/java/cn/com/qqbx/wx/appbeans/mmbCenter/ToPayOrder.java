package cn.com.qqbx.wx.appbeans.mmbCenter;

public class ToPayOrder {
	private String orderNo;
	private String insuCom;
	private String packageType;
	private String applicantName;
	private String premium;
	private String tciProposalNo;
	private String vciProposalNo;
	private String tciStartDate;
	private String vciStartDate;
	private String insureDate;//投保日期
	private String payDate;//支付日期
	private String orderStatus;//订单状态
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getTciProposalNo() {
		return tciProposalNo;
	}
	public void setTciProposalNo(String tciProposalNo) {
		this.tciProposalNo = tciProposalNo;
	}
	public String getVciProposalNo() {
		return vciProposalNo;
	}
	public void setVciProposalNo(String vciProposalNo) {
		this.vciProposalNo = vciProposalNo;
	}
	public String getTciStartDate() {
		return tciStartDate;
	}
	public void setTciStartDate(String tciStartDate) {
		this.tciStartDate = tciStartDate;
	}
	public String getVciStartDate() {
		return vciStartDate;
	}
	public void setVciStartDate(String vciStartDate) {
		this.vciStartDate = vciStartDate;
	}
	public String getInsureDate() {
		return insureDate;
	}
	public void setInsureDate(String insureDate) {
		this.insureDate = insureDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
