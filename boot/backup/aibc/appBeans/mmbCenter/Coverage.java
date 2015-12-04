package zbh.aibc.appBeans.mmbCenter;

public class Coverage {
	private String prdtKindCode;//在险种编码
	private String coverageCode;           //条款编码
	private String coverageName;           //条款名称
//	private String deductibleFlag;         //投保不计免赔标志 0：不投保，1：投保
	private String premium;                //折后保费
	private String amount;                 //保险金额/赔偿限额
	private String unitAmount;             //单位保额
	private String quantity;               //数量
	private String modeCode;               //玻璃险种类
	private String modeName;               //玻璃险种类名称
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
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	
}
