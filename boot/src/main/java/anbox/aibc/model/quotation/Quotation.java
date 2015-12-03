package anbox.aibc.model.quotation;

import java.sql.Types;

import anbox.aibc.model.CommModel;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 报价信息
 * @author guoshaopeng
 * @since  2013-8-28
 */
public abstract class Quotation extends CommModel{
	private static final long serialVersionUID = 5642250393484568398L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String quoteNo;
	
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String insuComPriceNo;      //保险公司报价单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntComTransNo;      //行销机构报价单号
	private QttnCustomer applicant;     //投保人
	private QttnCustomer insured;       //被保人
	
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String execProtocolCode;	//执行协议号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String agntProtocolCode;	//行销协议号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntOper;            //行销机构人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntOperName;        //行销机构人员名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String execOper;            //执行机构人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String execOperName;        //执行机构人员名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String mangOper;            //管理机构人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String mangOperName;        //管理机构人员名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String agntCom;             //行销机构
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String execCom;             //执行机构
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String mangCom;             //管理机构
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String insuCom;             //保险公司
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String InsureCityCode;      //实际投保城市
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String policyKind;          //保单种类
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String policyClass;         //保单类别
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String rnwlPolicyNo;        //被续保单号
	private double totalAmount;         //投保总保额
	private double benchmarkPremium;    //折前保费
	private double premium;             //折后保费
	private double sumDiscount;         //总折扣金额
	private double discount;            //折扣
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String lastStartDate;       //上年保险起期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String lastEndDate;         //上年保险止期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String startDate;           //起保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String startHour;           //起保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String endDate;             //终保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String endHour;             //终保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String successFlag;         //报价成功标识
	@SqlTypeAnnotation(type=Types.CHAR, length = 1000, sqlType = " ")
	private String failReason;          //报价失败原因
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String currency;            //投保币别
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String effectFlag;          //即时生效标识
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String engagedFlag;         //特别约定标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String argueSolution;       //争议解决方式
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String arbitration;         //仲裁机构代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String jurisdictArea;       //司法管辖区域代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String serviceArea;         //服务区域
	
	//微信中添加的字段
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String read;

	public String getRead() {
		return read;
	}
	public void setRead(String read) {
		this.read = read;
	}
	public String getInsuComPriceNo() {
		return insuComPriceNo;
	}
	public void setInsuComPriceNo(String insuComPriceNo) {
		this.insuComPriceNo = insuComPriceNo;
	}
	public String getAgntComTransNo() {
		return agntComTransNo;
	}
	public void setAgntComTransNo(String agntComTransNo) {
		this.agntComTransNo = agntComTransNo;
	}
	public QttnCustomer getApplicant() {
		return applicant;
	}
	public void setApplicant(QttnCustomer applicant) {
		this.applicant = applicant;
	}
	public QttnCustomer getInsured() {
		return insured;
	}
	public void setInsured(QttnCustomer insured) {
		this.insured = insured;
	}
	public String getExecProtocolCode() {
		return execProtocolCode;
	}
	public void setExecProtocolCode(String execProtocolCode) {
		this.execProtocolCode = execProtocolCode;
	}
	public String getAgntProtocolCode() {
		return agntProtocolCode;
	}
	public void setAgntProtocolCode(String agntProtocolCode) {
		this.agntProtocolCode = agntProtocolCode;
	}
	public String getAgntOper() {
		return agntOper;
	}
	public void setAgntOper(String agntOper) {
		this.agntOper = agntOper;
	}
	public String getInsureCityCode() {
		return InsureCityCode;
	}
	public void setInsureCityCode(String insureCityCode) {
		InsureCityCode = insureCityCode;
	}
	public String getAgntOperName() {
		return agntOperName;
	}
	public void setAgntOperName(String agntOperName) {
		this.agntOperName = agntOperName;
	}
	public String getExecOper() {
		return execOper;
	}
	public void setExecOper(String execOper) {
		this.execOper = execOper;
	}
	public String getExecOperName() {
		return execOperName;
	}
	public void setExecOperName(String execOperName) {
		this.execOperName = execOperName;
	}
	public String getMangOper() {
		return mangOper;
	}
	public void setMangOper(String mangOper) {
		this.mangOper = mangOper;
	}
	public String getMangOperName() {
		return mangOperName;
	}
	public void setMangOperName(String mangOperName) {
		this.mangOperName = mangOperName;
	}
	public String getAgntCom() {
		return agntCom;
	}
	public void setAgntCom(String agntCom) {
		this.agntCom = agntCom;
	}
	public String getExecCom() {
		return execCom;
	}
	public void setExecCom(String execCom) {
		this.execCom = execCom;
	}
	public String getMangCom() {
		return mangCom;
	}
	public void setMangCom(String mangCom) {
		this.mangCom = mangCom;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public String getPolicyKind() {
		return policyKind;
	}
	public void setPolicyKind(String policyKind) {
		this.policyKind = policyKind;
	}
	public String getPolicyClass() {
		return policyClass;
	}
	public void setPolicyClass(String policyClass) {
		this.policyClass = policyClass;
	}
	public String getRnwlPolicyNo() {
		return rnwlPolicyNo;
	}
	public void setRnwlPolicyNo(String rnwlPolicyNo) {
		this.rnwlPolicyNo = rnwlPolicyNo;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
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
	public double getSumDiscount() {
		return sumDiscount;
	}
	public void setSumDiscount(double sumDiscount) {
		this.sumDiscount = sumDiscount;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getLastStartDate() {
		return lastStartDate;
	}
	public void setLastStartDate(String lastStartDate) {
		this.lastStartDate = lastStartDate;
	}
	public String getLastEndDate() {
		return lastEndDate;
	}
	public void setLastEndDate(String lastEndDate) {
		this.lastEndDate = lastEndDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getEffectFlag() {
		return effectFlag;
	}
	public void setEffectFlag(String effectFlag) {
		this.effectFlag = effectFlag;
	}
	public String getEngagedFlag() {
		return engagedFlag;
	}
	public void setEngagedFlag(String engagedFlag) {
		this.engagedFlag = engagedFlag;
	}
	public String getArgueSolution() {
		return argueSolution;
	}
	public void setArgueSolution(String argueSolution) {
		this.argueSolution = argueSolution;
	}
	public String getArbitration() {
		return arbitration;
	}
	public void setArbitration(String arbitration) {
		this.arbitration = arbitration;
	}
	public String getJurisdictArea() {
		return jurisdictArea;
	}
	public void setJurisdictArea(String jurisdictArea) {
		this.jurisdictArea = jurisdictArea;
	}
	public String getServiceArea() {
		return serviceArea;
	}
	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}
	public String getQuoteNo() {
		return quoteNo;
	}
	public void setQuoteNo(String quoteNo) {
		this.quoteNo = quoteNo;
	}
	
}
