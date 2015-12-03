package anbox.aibc.model.policy;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
import cn.remex.db.view.Element;
/**
 * 保单信息
 * @author guoshaopeng
 * @since  2013-8-28
 */
public abstract class Policy extends ModelableImpl{

	private static final long serialVersionUID = 643419120579618872L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	@Element(label="内部保单号" , colModelIndex = 1, width = 220)
	private String id;                       //内部保单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	//此处保存应用端询价单号
	private String atinQttnId;               //询价单流水号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String insuComPriceNo;           //保险公司报价单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntComTransNo;           //行销机构报价单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agentComTransNo;          //银行交易流水号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String execComTransNo;           //执行机构流水号
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String execProtocolCode;	     //执行协议号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String agntProtocolCode;	     //行销协议号码
	private PlcyCustomer applicant;          //投保人
	private PlcyCustomer insured;            //被保人
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntOper;                 //行销人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntOperName;             //行销人员名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String execOper;                 //执行机构人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String execOperName;             //执行机构人员名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String mangOper;                 //管理机构人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String mangOperName;             //管理机构人员名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String execCom;                  //执行机构
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String mangCom;                  //管理机构
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String agntCom;                  //行销机构
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	@Element(label="保险公司")
	private String insuCom;                  //保险公司
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String insureCityCode;           //实际投保城市
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String policyKind;               //保单种类
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String policyClass;              //保单类别
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String rnwlPolicyNo;             //被续保单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String currency;                 //投保币别
	private double totalAmount;              //投保总保额
	private double benchmarkPremium;         //折前总保费
	@Element(label= "折后总保费" )
	private double premium;                  //折后总保费
	private double totalDiscount;            //总折扣金额
	private double discount;                 //折扣
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String insuComPayNo;             //保险公司支付号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String checkCode;                //校验码  pos机划卡时输入支付号后可能需要输入校验码
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String lastStartDate;            //上年保险起期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String lastEndDate;              //上年保险止期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String startDate;                //起保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String startHour;                //起保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String endDate;                  //终保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String endHour;                  //终保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String engagedFlag;              //特别约定标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String confirmNo;                //确认码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String jurisdictArea;            //司法管辖区域代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String argueSolution;            //争议解决方式
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String arbitration;              //仲裁机构代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String insureDate;               //投保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String signDate;                 //签单日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String issueDate;                //出单日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String policyPrintDate;          //保单打印日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String policyPostDate;           //保单递送日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String receiptDate;              //保单回执日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String insureSuccessFlag;        //投保确认成功标识
	@SqlTypeAnnotation(type=Types.CHAR, length = 3000, sqlType = " ")
	private String insureFailReason;         //投保确认失败信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	@Element(label="保单状态" ,colModelIndex = 8 ,width = 80)
	private String policyStatus;             //保单状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String policyStatusMsg;          //保单状态信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String policyStatusDate;         //保单状态修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String policyStatusOper;         //保单状态修改人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String payStatus;                //支付状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String payMsg;                   //支付状态信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String payDate;                  //支付状态修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String payOper;                  //支付人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	@Element(label="对账状态",colModelIndex = 5 ,width = 80)
	private String insuRcclStatus;           //(保险)对账状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	@Element(label="对账结果",colModelIndex = 6 ,width = 80)
	private String insuRcclMsg;              //(保险)对账信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String insuRcclDate;             //保险公司对账修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String insuRcclOper;             //保险公司对账人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String execRcclStatus;           //执行机构对账状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String execRcclMsg;              //执行机构对账信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String execRcclDate;             //执行机构对账修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String execRcclOper;             //执行机构对账人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String agntRcclStatus;           //行销机构对账状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String agntRcclMsg;              //行销机构对账信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String agntRcclDate;             //行销机构对账修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntRcclOper;             //行销机构对账人员
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String printSatus;               //打印状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String printMsg;                 //打印状态信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String printDate;                //打印状态修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String printOper;                //打印状态修改人
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String postStatus;               //递送状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String postMsg;                  //递送状态信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postDate;                 //递送状态修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String postOper;                 //递送状态修改人
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String settleStatus;             //结算状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String settleMsg;                //结算状态信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String settleDate;               //结算状态修改日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String settleOper;               //结算操作员
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String postNo;                   //运单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String postContact;              //递送联系人
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postTel;                  //递送电话
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postMobile;                //递送手机号
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String postEmail;                //递送邮箱
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String postAddr;                 //寄送地址
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String postCode;                 //寄送邮编
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String agntDeductFlag;           //银行扣费标识
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String agntDeductMsg;            //银行扣费提示信息
	private double agntDeductAmount;         //银行扣费总金额
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAtinQttnId() {
		return atinQttnId;
	}
	public void setAtinQttnId(String atinQttnId) {
		this.atinQttnId = atinQttnId;
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
	public String getAgentComTransNo() {
		return agentComTransNo;
	}
	public void setAgentComTransNo(String agentComTransNo) {
		this.agentComTransNo = agentComTransNo;
	}
	public String getExecComTransNo() {
		return execComTransNo;
	}
	public void setExecComTransNo(String execComTransNo) {
		this.execComTransNo = execComTransNo;
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
	public PlcyCustomer getApplicant() {
		return applicant;
	}
	public void setApplicant(PlcyCustomer applicant) {
		this.applicant = applicant;
	}
	public PlcyCustomer getInsured() {
		return insured;
	}
	public void setInsured(PlcyCustomer insured) {
		this.insured = insured;
	}
	public String getAgntOper() {
		return agntOper;
	}
	public void setAgntOper(String agntOper) {
		this.agntOper = agntOper;
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
	public String getAgntCom() {
		return agntCom;
	}
	public void setAgntCom(String agntCom) {
		this.agntCom = agntCom;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
    
	public String getInsureCityCode() {
		return insureCityCode;
	}
	public void setInsureCityCode(String insureCityCode) {
		this.insureCityCode = insureCityCode;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	public double getTotalDiscount() {
		return totalDiscount;
	}
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
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
	public String getEngagedFlag() {
		return engagedFlag;
	}
	public void setEngagedFlag(String engagedFlag) {
		this.engagedFlag = engagedFlag;
	}
	public String getConfirmNo() {
		return confirmNo;
	}
	public void setConfirmNo(String confirmNo) {
		this.confirmNo = confirmNo;
	}
	public String getJurisdictArea() {
		return jurisdictArea;
	}
	public void setJurisdictArea(String jurisdictArea) {
		this.jurisdictArea = jurisdictArea;
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
	public String getInsureDate() {
		return insureDate;
	}
	public void setInsureDate(String insureDate) {
		this.insureDate = insureDate;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getPolicyPrintDate() {
		return policyPrintDate;
	}
	public void setPolicyPrintDate(String policyPrintDate) {
		this.policyPrintDate = policyPrintDate;
	}
	public String getPolicyPostDate() {
		return policyPostDate;
	}
	public void setPolicyPostDate(String policyPostDate) {
		this.policyPostDate = policyPostDate;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getInsureSuccessFlag() {
		return insureSuccessFlag;
	}
	public void setInsureSuccessFlag(String insureSuccessFlag) {
		this.insureSuccessFlag = insureSuccessFlag;
	}
	public String getInsureFailReason() {
		return insureFailReason;
	}
	public void setInsureFailReason(String insureFailReason) {
		this.insureFailReason = insureFailReason;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	public String getPolicyStatusMsg() {
		return policyStatusMsg;
	}
	public void setPolicyStatusMsg(String policyStatusMsg) {
		this.policyStatusMsg = policyStatusMsg;
	}
	public String getPolicyStatusDate() {
		return policyStatusDate;
	}
	public void setPolicyStatusDate(String policyStatusDate) {
		this.policyStatusDate = policyStatusDate;
	}
	public String getPolicyStatusOper() {
		return policyStatusOper;
	}
	public void setPolicyStatusOper(String policyStatusOper) {
		this.policyStatusOper = policyStatusOper;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPayMsg() {
		return payMsg;
	}
	public void setPayMsg(String payMsg) {
		this.payMsg = payMsg;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayOper() {
		return payOper;
	}
	public void setPayOper(String payOper) {
		this.payOper = payOper;
	}
	public String getInsuRcclStatus() {
		return insuRcclStatus;
	}
	public void setInsuRcclStatus(String insuRcclStatus) {
		this.insuRcclStatus = insuRcclStatus;
	}
	public String getInsuRcclMsg() {
		return insuRcclMsg;
	}
	public void setInsuRcclMsg(String insuRcclMsg) {
		this.insuRcclMsg = insuRcclMsg;
	}
	public String getInsuRcclDate() {
		return insuRcclDate;
	}
	public void setInsuRcclDate(String insuRcclDate) {
		this.insuRcclDate = insuRcclDate;
	}
	public String getInsuRcclOper() {
		return insuRcclOper;
	}
	public void setInsuRcclOper(String insuRcclOper) {
		this.insuRcclOper = insuRcclOper;
	}
	public String getExecRcclStatus() {
		return execRcclStatus;
	}
	public void setExecRcclStatus(String execRcclStatus) {
		this.execRcclStatus = execRcclStatus;
	}
	public String getExecRcclMsg() {
		return execRcclMsg;
	}
	public void setExecRcclMsg(String execRcclMsg) {
		this.execRcclMsg = execRcclMsg;
	}
	public String getExecRcclDate() {
		return execRcclDate;
	}
	public void setExecRcclDate(String execRcclDate) {
		this.execRcclDate = execRcclDate;
	}
	public String getExecRcclOper() {
		return execRcclOper;
	}
	public void setExecRcclOper(String execRcclOper) {
		this.execRcclOper = execRcclOper;
	}
	public String getAgntRcclStatus() {
		return agntRcclStatus;
	}
	public void setAgntRcclStatus(String agntRcclStatus) {
		this.agntRcclStatus = agntRcclStatus;
	}
	public String getAgntRcclMsg() {
		return agntRcclMsg;
	}
	public void setAgntRcclMsg(String agntRcclMsg) {
		this.agntRcclMsg = agntRcclMsg;
	}
	public String getAgntRcclDate() {
		return agntRcclDate;
	}
	public void setAgntRcclDate(String agntRcclDate) {
		this.agntRcclDate = agntRcclDate;
	}
	public String getAgntRcclOper() {
		return agntRcclOper;
	}
	public void setAgntRcclOper(String agntRcclOper) {
		this.agntRcclOper = agntRcclOper;
	}
	public String getPrintSatus() {
		return printSatus;
	}
	public void setPrintSatus(String printSatus) {
		this.printSatus = printSatus;
	}
	public String getPrintMsg() {
		return printMsg;
	}
	public void setPrintMsg(String printMsg) {
		this.printMsg = printMsg;
	}
	public String getPrintDate() {
		return printDate;
	}
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	public String getPrintOper() {
		return printOper;
	}
	public void setPrintOper(String printOper) {
		this.printOper = printOper;
	}
	public String getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(String postStatus) {
		this.postStatus = postStatus;
	}
	public String getPostMsg() {
		return postMsg;
	}
	public void setPostMsg(String postMsg) {
		this.postMsg = postMsg;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getPostOper() {
		return postOper;
	}
	public void setPostOper(String postOper) {
		this.postOper = postOper;
	}
	public String getSettleStatus() {
		return settleStatus;
	}
	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}
	public String getSettleMsg() {
		return settleMsg;
	}
	public void setSettleMsg(String settleMsg) {
		this.settleMsg = settleMsg;
	}
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getSettleOper() {
		return settleOper;
	}
	public void setSettleOper(String settleOper) {
		this.settleOper = settleOper;
	}
	public String getPostNo() {
		return postNo;
	}
	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}
	public String getPostContact() {
		return postContact;
	}
	public void setPostContact(String postContact) {
		this.postContact = postContact;
	}
	public String getPostTel() {
		return postTel;
	}
	public void setPostTel(String postTel) {
		this.postTel = postTel;
	}
	public String getPostMobile() {
		return postMobile;
	}
	public void setPostMobile(String postMobile) {
		this.postMobile = postMobile;
	}
	public String getPostEmail() {
		return postEmail;
	}
	public void setPostEmail(String postEmail) {
		this.postEmail = postEmail;
	}
	public String getPostAddr() {
		return postAddr;
	}
	public void setPostAddr(String postAddr) {
		this.postAddr = postAddr;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getAgntDeductFlag() {
		return agntDeductFlag;
	}
	public void setAgntDeductFlag(String agntDeductFlag) {
		this.agntDeductFlag = agntDeductFlag;
	}
	public String getAgntDeductMsg() {
		return agntDeductMsg;
	}
	public void setAgntDeductMsg(String agntDeductMsg) {
		this.agntDeductMsg = agntDeductMsg;
	}
	public double getAgntDeductAmount() {
		return agntDeductAmount;
	}
	public void setAgntDeductAmount(double agntDeductAmount) {
		this.agntDeductAmount = agntDeductAmount;
	}
	public String getInsuComPayNo() {
		return insuComPayNo;
	}
	public void setInsuComPayNo(String insuComPayNo) {
		this.insuComPayNo = insuComPayNo;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
}
