package zbh.aias.model.policy;

import cn.remex.db.rsql.model.ModelableImpl;
import javax.persistence.Column;

import java.sql.Types;
/**
 * 历史理赔信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinPlcyClaim extends ModelableImpl{
	private static final long serialVersionUID = 976700000244932631L;
	@Column(length = 30)
	private String atinPlcyNo;            //内部保单号
	@Column(length = 30)
	private String caseNo;                //立案号
	@Column(length = 30)
	private String claimNo;               //理赔编码
	@Column(length = 10)
	private String claimType;             //理赔类型
	@Column(length = 30)
	private String vehicleId;             //车辆编码
	@Column(length = 10)
	private String licenseNo;             //车牌号
	@Column(length = 10)
	private String insuCom;               //保险公司代码
	@Column(length = 10)
	private String riskFlag;              //险种区分标志
	@Column(length = 20)
	private String startDate;             //起保日期
	@Column(length = 20)
	private String endDate;               //终保日期
	@Column(length = 40)
	private String insureSearchNo;        //投保查询码
	@Column(length = 600)
	private String accidentDesc;          //事故类型描述
	@Column(length = 30)
	private String incidentStatus;        //事故者现状
	@Column(length = 30)
	private String accidentDate;          //出险时间
	@Column(length = 200)
	private String accidentSite;          //出险地点
	@Column(length = 200)
	private String accidentReason;        //出险原因
	@Column(length = 30)
	private String reportNo;              //报案号
	@Column(length = 30)
	private String declineNo;             //拒赔号
	@Column(length = 30)
	private String claimCloseId;          //结案号
	@Column(length = 30)
	private String claimCloseDate;        //结案时间
	private double claimAmount;           //赔付金额
	private double declineAmount;         //拒绝金额
	private double totalAmount;           //赔付总金额
	@Column(length = 200)
	private String claimConclusion;       //赔付结论
	@Column(length = 10)
	private String reportType;            //报案方式
	@Column(length = 10)
	private String relationToInsured;     //报案人与被保人关系
	@Column(length = 30)
	private String reportorName;          //报案人姓名
	@Column(length = 200)
	private String reportorAddr;          //报案人通讯地址
	@Column(length = 20)
	private String reportorPhone;         //报案人电话
	@Column(length = 20)
	private String reportorMobile;        //报案人手机
	@Column(length = 50)
	private String reportorEmail;         //报案人邮箱
	@Column(length = 30)
	private String reportDate;            //报案日期
	public String getAtinPlcyNo() {
		return atinPlcyNo;
	}
	public void setAtinPlcyNo(String atinPlcyNo) {
		this.atinPlcyNo = atinPlcyNo;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public String getRiskFlag() {
		return riskFlag;
	}
	public void setRiskFlag(String riskFlag) {
		this.riskFlag = riskFlag;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getInsureSearchNo() {
		return insureSearchNo;
	}
	public void setInsureSearchNo(String insureSearchNo) {
		this.insureSearchNo = insureSearchNo;
	}
	public String getAccidentDesc() {
		return accidentDesc;
	}
	public void setAccidentDesc(String accidentDesc) {
		this.accidentDesc = accidentDesc;
	}
	public String getIncidentStatus() {
		return incidentStatus;
	}
	public void setIncidentStatus(String incidentStatus) {
		this.incidentStatus = incidentStatus;
	}
	public String getAccidentDate() {
		return accidentDate;
	}
	public void setAccidentDate(String accidentDate) {
		this.accidentDate = accidentDate;
	}
	public String getAccidentSite() {
		return accidentSite;
	}
	public void setAccidentSite(String accidentSite) {
		this.accidentSite = accidentSite;
	}
	public String getAccidentReason() {
		return accidentReason;
	}
	public void setAccidentReason(String accidentReason) {
		this.accidentReason = accidentReason;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getDeclineNo() {
		return declineNo;
	}
	public void setDeclineNo(String declineNo) {
		this.declineNo = declineNo;
	}
	public String getClaimCloseId() {
		return claimCloseId;
	}
	public void setClaimCloseId(String claimCloseId) {
		this.claimCloseId = claimCloseId;
	}
	public String getClaimCloseDate() {
		return claimCloseDate;
	}
	public void setClaimCloseDate(String claimCloseDate) {
		this.claimCloseDate = claimCloseDate;
	}
	public double getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(double claimAmount) {
		this.claimAmount = claimAmount;
	}
	public double getDeclineAmount() {
		return declineAmount;
	}
	public void setDeclineAmount(double declineAmount) {
		this.declineAmount = declineAmount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getClaimConclusion() {
		return claimConclusion;
	}
	public void setClaimConclusion(String claimConclusion) {
		this.claimConclusion = claimConclusion;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getRelationToInsured() {
		return relationToInsured;
	}
	public void setRelationToInsured(String relationToInsured) {
		this.relationToInsured = relationToInsured;
	}
	public String getReportorName() {
		return reportorName;
	}
	public void setReportorName(String reportorName) {
		this.reportorName = reportorName;
	}
	public String getReportorAddr() {
		return reportorAddr;
	}
	public void setReportorAddr(String reportorAddr) {
		this.reportorAddr = reportorAddr;
	}
	public String getReportorPhone() {
		return reportorPhone;
	}
	public void setReportorPhone(String reportorPhone) {
		this.reportorPhone = reportorPhone;
	}
	public String getReportorMobile() {
		return reportorMobile;
	}
	public void setReportorMobile(String reportorMobile) {
		this.reportorMobile = reportorMobile;
	}
	public String getReportorEmail() {
		return reportorEmail;
	}
	public void setReportorEmail(String reportorEmail) {
		this.reportorEmail = reportorEmail;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
}
