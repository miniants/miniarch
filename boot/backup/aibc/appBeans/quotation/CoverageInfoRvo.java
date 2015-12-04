package zbh.aibc.appBeans.quotation;

public class CoverageInfoRvo {
	private String prdtKindCode;        //险种编码
	private String coverageCode;        //条款编码
	private String coverageName;        //条款名称
	private String deductibleFlag;      //投保不计免赔标志 0：不投保，1：投保
	private String modeCode;            //玻璃种类
	private String modeName;            //玻璃种类名称
	private String optionalValue;       //可选免赔金额
	private String unitAmount;          //单位保额
	private String quantity;            //数量
	private String amount;              //保险金额/赔偿限额
	
	private String benchmarkPremium;       //折前保费
	private String premium;                //折后保费
	private String discount;               //折扣
	public String getPrdtKindCode() {
		return prdtKindCode;
	}
	public void setPrdtKindCode(String prdtKindCode) {
		this.prdtKindCode = prdtKindCode;
	}
	public String getCoverageCode() {
		return coverageCode;
	}
	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}
	public String getCoverageName() {
		return coverageName;
	}
	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}
	public String getDeductibleFlag() {
		return deductibleFlag;
	}
	public void setDeductibleFlag(String deductibleFlag) {
		this.deductibleFlag = deductibleFlag;
	}
	public String getModeCode() {
		return modeCode;
	}
	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}
	public String getModeName() {
		return modeName;
	}
	public void setModeName(String modeName) {
		this.modeName = modeName;
	}
	public String getOptionalValue() {
		return optionalValue;
	}
	public void setOptionalValue(String optionalValue) {
		this.optionalValue = optionalValue;
	}
	public String getUnitAmount() {
		return unitAmount;
	}
	public void setUnitAmount(String unitAmount) {
		this.unitAmount = unitAmount;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBenchmarkPremium() {
		return benchmarkPremium;
	}
	public void setBenchmarkPremium(String benchmarkPremium) {
		this.benchmarkPremium = benchmarkPremium;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
}
