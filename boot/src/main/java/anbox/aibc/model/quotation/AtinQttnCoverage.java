package anbox.aibc.model.quotation;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 询价单条款信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinQttnCoverage extends ModelableImpl{
	private static final long serialVersionUID = 7974883797826933253L;
	private AtinQuotation atinQuotation;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String prdtKindCode;            //险种编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String coverageCode;            //条款编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String coverageName;            //条款名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String modeCode;                //投保方式代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String modeName;                //投保方式名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String deductibleFlag;          //是否投保不计免赔标志
	private double optionalValue;           //可选免赔金额
	private double deductibleRate;          //免赔率
	private double deductibleAmount;        //免赔额
	private double unitAmount;              //单位保额
	private int quantity;                   //数量
	private double amount;                  //保险金额/赔偿限额
	private double benchmarkPremium;        //折前保费
	private double premium;                 //折后保费
	private double discount;                //折扣
	private double baseRate;                //基础费率
	private double shortCoefficient;        //短期系数
	private double adjustCoefficient;       //调整系数
	private double floatCoefficient;        //浮动系数
	
	public AtinQuotation getAtinQuotation() {
		return atinQuotation;
	}
	public void setAtinQuotation(AtinQuotation atinQuotation) {
		this.atinQuotation = atinQuotation;
	}
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
	public String getDeductibleFlag() {
		return deductibleFlag;
	}
	public void setDeductibleFlag(String deductibleFlag) {
		this.deductibleFlag = deductibleFlag;
	}
	public double getOptionalValue() {
		return optionalValue;
	}
	public void setOptionalValue(double optionalValue) {
		this.optionalValue = optionalValue;
	}
	public double getDeductibleRate() {
		return deductibleRate;
	}
	public void setDeductibleRate(double deductibleRate) {
		this.deductibleRate = deductibleRate;
	}
	public double getDeductibleAmount() {
		return deductibleAmount;
	}
	public void setDeductibleAmount(double deductibleAmount) {
		this.deductibleAmount = deductibleAmount;
	}
	public double getUnitAmount() {
		return unitAmount;
	}
	public void setUnitAmount(double unitAmount) {
		this.unitAmount = unitAmount;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getBenchmarkPremium() {
		return benchmarkPremium;
	}
	public void setBenchmarkPremium(double benchmarkPremium) {
		this.benchmarkPremium = benchmarkPremium;
	}
	public double getPremium() {
		return premium;
	}
	public void setPremium(double premium) {
		this.premium = premium;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getBaseRate() {
		return baseRate;
	}
	public void setBaseRate(double baseRate) {
		this.baseRate = baseRate;
	}
	public double getShortCoefficient() {
		return shortCoefficient;
	}
	public void setShortCoefficient(double shortCoefficient) {
		this.shortCoefficient = shortCoefficient;
	}
	public double getAdjustCoefficient() {
		return adjustCoefficient;
	}
	public void setAdjustCoefficient(double adjustCoefficient) {
		this.adjustCoefficient = adjustCoefficient;
	}
	public double getFloatCoefficient() {
		return floatCoefficient;
	}
	public void setFloatCoefficient(double floatCoefficient) {
		this.floatCoefficient = floatCoefficient;
	}	
}
