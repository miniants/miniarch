package zbh.aibc.appBeans.mmbCenter;

import java.util.List;

public class Quotation {
	private String cityCode;
	private String quoteNo;
	private String insuCom;
	private String licenseNo;
	private double premium;
	private String insuredName;//被保人姓名
	private String insuredCertNo;
	private String quotePriceDate;//询价日期
	private String tciStartDate;
	private String vciStartDate;
	private String tciEndDate;
	private String vciEndDate;
	private double sumTravelTax;
	private double tciPremium;
	private double vciPremium;
	private String owner;
	private String quotationStatus;
	private String frameNo;
	private String brandName;
	private String enrollDate;
	private List<Coverage> atinCoverages;
	public String getQuoteNo() {
		return quoteNo;
	}
	public void setQuoteNo(String quoteNo) {
		this.quoteNo = quoteNo;
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
	public double getPremium() {
		return premium;
	}
	public void setPremium(double premium) {
		this.premium = premium;
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
	public String getQuotePriceDate() {
		return quotePriceDate;
	}
	public void setQuotePriceDate(String quotePriceDate) {
		this.quotePriceDate = quotePriceDate;
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
	public double getSumTravelTax() {
		return sumTravelTax;
	}
	public void setSumTravelTax(double sumTravelTax) {
		this.sumTravelTax = sumTravelTax;
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
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	public List<Coverage> getAtinCoverages() {
		return atinCoverages;
	}
	public void setAtinCoverages(List<Coverage> atinCoverages) {
		this.atinCoverages = atinCoverages;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getQuotationStatus() {
		return quotationStatus;
	}
	public void setQuotationStatus(String quotationStatus) {
		this.quotationStatus = quotationStatus;
	}
	
}
