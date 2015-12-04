package zbh.aias.model.policy;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import java.sql.Types;
/**
 * 保单车船税信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinPlcyTravelTax extends ModelableImpl{
	private static final long serialVersionUID = -1072389948605414283L;
	private AtinPolicy atinPolicy;
	@Column(length = 10)
	private String taxStatus;                  //完税状态
	@Column(length = 100)
	private String taxPayerName;               //纳税人名称
	@Column(length = 10)
	private String taxPayerCertType;           //纳税人证件类型
	@Column(length = 20)
	private String taxPayerCertNo;             //纳税人证件号
	@Column(length = 30)
	private String invoiceDate;                //购车发票日
	@Column(length = 30)
	private String payTaxCertNo;               //完税凭证号
	@Column(length = 30)
	private String taxReliefCertNo;            //减免税证明号
	@Column(length = 30)
	private String issuTaxAuthority;           //开具税务机关代码
	@Column(length = 50)
	private String issuTaxAuthorityName;        //开具税务机关名称
	@Column(length = 30)
	private String taxAuthority;               //所属税务机构代码
	@Column(length = 50)
	private String taxAuthorityName;           //所属税务机构名称
	@Column(length = 30)
	private String taxStartDate;               //税款所属起期
	@Column(length = 30)
	private String taxEndDate;                 //税款所属止期
	@Column(length = 10)
	private String taxReliefPlan;              //减免税方案
	@Column(length = 30)
	private String taxReliefReason;            //减免税原因
	private double taxReliefAmount;            //减免税额
	private double taxReliefRate;              //减免比例
	@Column(length = 10)
	private String hisFeeFlag;                 //是否计算往年补交
	@Column(length = 10)
	private String clctTravalTaxFlag;          //是否代收车船税
	@Column(length = 10)
	private String lateFeeFlag;                //是否滞纳金
	private double lateFee;                    //滞纳金
	private double actualTravelTax;            //当年应缴车船税
	private double previousPay;                //往年补缴
	private double sumTravelTax;               //总车船税
	private double taxReliefBalance;           //补缴税改差额
	@Column(length = 10)
	private String lastPayYear;                //前次缴费年度
	@Column(length = 30)
	private String lastPolicyEndDate;          //前次保单止期
	@Column(length = 30)
	private String lateFeeStartDate;           //滞纳金计算起始日期
	@Column(length = 30)
	private String lateFeeEndDate;             //滞纳金计算终止日期
	@Column(length = 10)
	private String fuelType;                   //机动车燃料种类
	@Column(length = 30)
	private String vehiOriginCertType;         //车辆来历凭证种类
	@Column(length = 30)
	private String vehiOriginCertNo;           //车辆来历凭证编号
	@Column(length = 30)
	private String vehiOriginCertDate;         //车辆来历凭证所载日期
	@Column(length = 30)
	private String transactionNo;//交易流水号

   
	public AtinPolicy getAtinPolicy() {
		return atinPolicy;
	}
	public void setAtinPolicy(AtinPolicy atinPolicy) {
		this.atinPolicy = atinPolicy;
	}
	public String getTaxStatus() {
		return taxStatus;
	}
	public void setTaxStatus(String taxStatus) {
		this.taxStatus = taxStatus;
	}
	public String getTaxPayerName() {
		return taxPayerName;
	}
	public void setTaxPayerName(String taxPayerName) {
		this.taxPayerName = taxPayerName;
	}
	public String getTaxPayerCertType() {
		return taxPayerCertType;
	}
	public void setTaxPayerCertType(String taxPayerCertType) {
		this.taxPayerCertType = taxPayerCertType;
	}
	public String getTaxPayerCertNo() {
		return taxPayerCertNo;
	}
	public void setTaxPayerCertNo(String taxPayerCertNo) {
		this.taxPayerCertNo = taxPayerCertNo;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getPayTaxCertNo() {
		return payTaxCertNo;
	}
	public void setPayTaxCertNo(String payTaxCertNo) {
		this.payTaxCertNo = payTaxCertNo;
	}
	public String getTaxReliefCertNo() {
		return taxReliefCertNo;
	}
	public void setTaxReliefCertNo(String taxReliefCertNo) {
		this.taxReliefCertNo = taxReliefCertNo;
	}
	public String getIssuTaxAuthority() {
		return issuTaxAuthority;
	}
	public void setIssuTaxAuthority(String issuTaxAuthority) {
		this.issuTaxAuthority = issuTaxAuthority;
	}
	public String getIssuTaxAuthorityName() {
		return issuTaxAuthorityName;
	}
	public void setIssuTaxAuthorityName(String issuTaxAuthorityName) {
		this.issuTaxAuthorityName = issuTaxAuthorityName;
	}
	public String getTaxAuthority() {
		return taxAuthority;
	}
	public void setTaxAuthority(String taxAuthority) {
		this.taxAuthority = taxAuthority;
	}
	public String getTaxAuthorityName() {
		return taxAuthorityName;
	}
	public void setTaxAuthorityName(String taxAuthorityName) {
		this.taxAuthorityName = taxAuthorityName;
	}
	public String getTaxStartDate() {
		return taxStartDate;
	}
	public void setTaxStartDate(String taxStartDate) {
		this.taxStartDate = taxStartDate;
	}
	public String getTaxEndDate() {
		return taxEndDate;
	}
	public void setTaxEndDate(String taxEndDate) {
		this.taxEndDate = taxEndDate;
	}
	public String getTaxReliefPlan() {
		return taxReliefPlan;
	}
	public void setTaxReliefPlan(String taxReliefPlan) {
		this.taxReliefPlan = taxReliefPlan;
	}
	public String getTaxReliefReason() {
		return taxReliefReason;
	}
	public void setTaxReliefReason(String taxReliefReason) {
		this.taxReliefReason = taxReliefReason;
	}
	public double getTaxReliefAmount() {
		return taxReliefAmount;
	}
	public void setTaxReliefAmount(double taxReliefAmount) {
		this.taxReliefAmount = taxReliefAmount;
	}
	public double getTaxReliefRate() {
		return taxReliefRate;
	}
	public void setTaxReliefRate(double taxReliefRate) {
		this.taxReliefRate = taxReliefRate;
	}
	public String getHisFeeFlag() {
		return hisFeeFlag;
	}
	public void setHisFeeFlag(String hisFeeFlag) {
		this.hisFeeFlag = hisFeeFlag;
	}
	public String getLateFeeFlag() {
		return lateFeeFlag;
	}
	public void setLateFeeFlag(String lateFeeFlag) {
		this.lateFeeFlag = lateFeeFlag;
	}
	public double getTaxReliefBalance() {
		return taxReliefBalance;
	}
	public void setTaxReliefBalance(double taxReliefBalance) {
		this.taxReliefBalance = taxReliefBalance;
	}
	public String getLastPayYear() {
		return lastPayYear;
	}
	public void setLastPayYear(String lastPayYear) {
		this.lastPayYear = lastPayYear;
	}
	public String getLastPolicyEndDate() {
		return lastPolicyEndDate;
	}
	public void setLastPolicyEndDate(String lastPolicyEndDate) {
		this.lastPolicyEndDate = lastPolicyEndDate;
	}
	public String getLateFeeStartDate() {
		return lateFeeStartDate;
	}
	public void setLateFeeStartDate(String lateFeeStartDate) {
		this.lateFeeStartDate = lateFeeStartDate;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getVehiOriginCertType() {
		return vehiOriginCertType;
	}
	public void setVehiOriginCertType(String vehiOriginCertType) {
		this.vehiOriginCertType = vehiOriginCertType;
	}
	public String getVehiOriginCertNo() {
		return vehiOriginCertNo;
	}
	public void setVehiOriginCertNo(String vehiOriginCertNo) {
		this.vehiOriginCertNo = vehiOriginCertNo;
	}
	public String getVehiOriginCertDate() {
		return vehiOriginCertDate;
	}
	public void setVehiOriginCertDate(String vehiOriginCertDate) {
		this.vehiOriginCertDate = vehiOriginCertDate;
	}
	public double getLateFee() {
		return lateFee;
	}
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}
	public String getLateFeeEndDate() {
		return lateFeeEndDate;
	}
	public void setLateFeeEndDate(String lateFeeEndDate) {
		this.lateFeeEndDate = lateFeeEndDate;
	}
	public String getClctTravalTaxFlag() {
		return clctTravalTaxFlag;
	}
	public void setClctTravalTaxFlag(String clctTravalTaxFlag) {
		this.clctTravalTaxFlag = clctTravalTaxFlag;
	}
	public double getActualTravelTax() {
		return actualTravelTax;
	}
	public void setActualTravelTax(double actualTravelTax) {
		this.actualTravelTax = actualTravelTax;
	}
	public double getPreviousPay() {
		return previousPay;
	}
	public void setPreviousPay(double previousPay) {
		this.previousPay = previousPay;
	}
	public double getSumTravelTax() {
		return sumTravelTax;
	}
	public void setSumTravelTax(double sumTravelTax) {
		this.sumTravelTax = sumTravelTax;
	}
	public String getTransactionNo() {
		return transactionNo;
	}
	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}
}
