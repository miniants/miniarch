package zbh.aias.model.policy;

import cn.remex.db.rsql.model.ModelableImpl;
/**
 * 行销机构对账明细
 * @author guoshaopeng
 * @since  2013-12-27
 */
public class AgntComRcclDetail extends ModelableImpl{
	private static final long serialVersionUID = 1424031138526163699L;
	private String atinPlcyId;             //内部保单号
	private String agntComTransNo;         //行销机构交易号
	private String execCom;                //执行机构
	private String mangCom;                //管理机构
	private String agntCom;                //行销机构
	private String insuCom;                //厂商机构
	private double mangPremium;            //管理机构金额
	private double insuPremium;            //行销机构金额
	private String policyStatus;           //保单状态
	private String rcclStatus;             //行销机构对账状态
	private String rcclDate;               //对账日期
	private String rcclMsg;                //对账信息
	private String rcclReqFile;            //对账请求报文路径
	private String rcclResFile;            //对账响应报文路径
	public String getAtinPlcyId() {
		return atinPlcyId;
	}
	public void setAtinPlcyId(String atinPlcyId) {
		this.atinPlcyId = atinPlcyId;
	}
	public String getAgntComTransNo() {
		return agntComTransNo;
	}
	public void setAgntComTransNo(String agntComTransNo) {
		this.agntComTransNo = agntComTransNo;
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
	public double getMangPremium() {
		return mangPremium;
	}
	public void setMangPremium(double mangPremium) {
		this.mangPremium = mangPremium;
	}
	public double getInsuPremium() {
		return insuPremium;
	}
	public void setInsuPremium(double insuPremium) {
		this.insuPremium = insuPremium;
	}
	public String getPolicyStatus() {
		return policyStatus;
	}
	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}
	public String getRcclStatus() {
		return rcclStatus;
	}
	public void setRcclStatus(String rcclStatus) {
		this.rcclStatus = rcclStatus;
	}
	public String getRcclDate() {
		return rcclDate;
	}
	public void setRcclDate(String rcclDate) {
		this.rcclDate = rcclDate;
	}
	public String getRcclMsg() {
		return rcclMsg;
	}
	public void setRcclMsg(String rcclMsg) {
		this.rcclMsg = rcclMsg;
	}
	public String getRcclReqFile() {
		return rcclReqFile;
	}
	public void setRcclReqFile(String rcclReqFile) {
		this.rcclReqFile = rcclReqFile;
	}
	public String getRcclResFile() {
		return rcclResFile;
	}
	public void setRcclResFile(String rcclResFile) {
		this.rcclResFile = rcclResFile;
	}

	
}
