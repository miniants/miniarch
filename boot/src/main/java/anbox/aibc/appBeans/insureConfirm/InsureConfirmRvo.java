package anbox.aibc.appBeans.insureConfirm;

import java.util.List;

public class InsureConfirmRvo {
	private String insuCom;         //保险公司
	private String cityCode;         //城市
	private String innerPlcyNo;         //内部保单号
	private String insureSuccessFlag; //投保确认成功标识
	private String insureFailReason;  //投保确认失败信息
	private String vciPolicyNo;   //商业险投保单号
	private String tciPolicyNo;   //交强险投保单号
	private String vciProposalNo;   //商业险投保单号
	private String tciProposalNo;   //交强险投保单号
	private String tciUdwrtFlag;    //交强险核保标志
	private String tciUdwrtMessage; //交强险核保信息
	private String vciUdwrtFlag;    //商业险核保标志
	private String vciUdwrtMessage; //商业险核保信息
	private String orderNo;
	private List<Object> deliveryInfos;
	private double orderPremium;
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public double getOrderPremium() {
		return orderPremium;
	}
	public void setOrderPremium(double orderPremium) {
		this.orderPremium = orderPremium;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getInnerPlcyNo() {
		return innerPlcyNo;
	}
	public void setInnerPlcyNo(String innerPlcyNo) {
		this.innerPlcyNo = innerPlcyNo;
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
	public String getVciPolicyNo() {
		return vciPolicyNo;
	}
	public void setVciPolicyNo(String vciPolicyNo) {
		this.vciPolicyNo = vciPolicyNo;
	}
	public String getTciPolicyNo() {
		return tciPolicyNo;
	}
	public void setTciPolicyNo(String tciPolicyNo) {
		this.tciPolicyNo = tciPolicyNo;
	}
	public String getVciProposalNo() {
		return vciProposalNo;
	}
	public void setVciProposalNo(String vciProposalNo) {
		this.vciProposalNo = vciProposalNo;
	}
	public String getTciProposalNo() {
		return tciProposalNo;
	}
	public void setTciProposalNo(String tciProposalNo) {
		this.tciProposalNo = tciProposalNo;
	}
	public String getTciUdwrtFlag() {
		return tciUdwrtFlag;
	}
	public void setTciUdwrtFlag(String tciUdwrtFlag) {
		this.tciUdwrtFlag = tciUdwrtFlag;
	}
	public String getTciUdwrtMessage() {
		return tciUdwrtMessage;
	}
	public void setTciUdwrtMessage(String tciUdwrtMessage) {
		this.tciUdwrtMessage = tciUdwrtMessage;
	}
	public String getVciUdwrtFlag() {
		return vciUdwrtFlag;
	}
	public void setVciUdwrtFlag(String vciUdwrtFlag) {
		this.vciUdwrtFlag = vciUdwrtFlag;
	}
	public String getVciUdwrtMessage() {
		return vciUdwrtMessage;
	}
	public void setVciUdwrtMessage(String vciUdwrtMessage) {
		this.vciUdwrtMessage = vciUdwrtMessage;
	}
	public List<Object> getDeliveryInfos() {
		return deliveryInfos;
	}
	public void setDeliveryInfos(List<Object> deliveryInfos) {
		this.deliveryInfos = deliveryInfos;
	}
	
}
