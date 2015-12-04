package zbh.wx.appbeans.mmbCenter;

import java.util.List;

public class OrderInfo {
	private String orderNo;//订单号
	private String orderType;//订单类型，分车险保单、车主人身意外险
	private String insuCom;
	private String cityCode;
	private String licenseNo;
	private String insuredName;//被保人
	private String insuredCertNo;//被保人证件类型
	private String tciStartDate;
	private String vciStartDate;
	private double premium;
	private double tciPremium;
	private double vciPremium;
	private double sumTravelTax;
	private String quotePriceDate;//询价日期
	private String orderStatus;
	private String orderDate;
	private String brandName;
	private String frameNo;
	private String engineNo;
	private String enrollDate;
	private String tciEndDate;
	private String vciEndDate;
	private List<Coverage> atinCoverages;
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
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getInsuredCertNo() {
		return insuredCertNo;
	}
	public void setInsuredCertNo(String insuredCertNo) {
		this.insuredCertNo = insuredCertNo;
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
	public double getPremium() {
		return premium;
	}
	public void setPremium(double premium) {
		this.premium = premium;
	}
	public double getTciPremium() {
		return tciPremium;
	}
	public void setTciPremium(double tciPremium) {
		this.tciPremium = tciPremium;
	}
	public double getVciPremium() {
		return vciPremium;
	}
	public void setVciPremium(double vciPremium) {
		this.vciPremium = vciPremium;
	}
	public double getSumTravelTax() {
		return sumTravelTax;
	}
	public void setSumTravelTax(double sumTravelTax) {
		this.sumTravelTax = sumTravelTax;
	}
	public String getQuotePriceDate() {
		return quotePriceDate;
	}
	public void setQuotePriceDate(String quotePriceDate) {
		this.quotePriceDate = quotePriceDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	public String getTciEndDate() {
		return tciEndDate;
	}
	public void setTciEndDate(String tciEndDate) {
		this.tciEndDate = tciEndDate;
	}
	public String getVciEndDate() {
		return vciEndDate;
	}
	public void setVciEndDate(String vciEndDate) {
		this.vciEndDate = vciEndDate;
	}
	public List<Coverage> getAtinCoverages() {
		return atinCoverages;
	}
	public void setAtinCoverages(List<Coverage> atinCoverages) {
		this.atinCoverages = atinCoverages;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
}
