package anbox.aibc.appBeans.insureConfirm;

import java.util.List;

public class InsureConfirmCvo {
	private String insuCom;  //投保公司 Y
	private String cityCode; //投保城市 Y
	private String quoteNo;  //询价单号 Y
	private String vciQuestion;//商业险验证码问题
	private String vciAnswer;///商业险验证码问题答案
	private String tciQuestion;///交强险验证码问题
	private String tciAnswer;///交强险验证码问题答案
	private String insuComApplicationNo;	//保险公司询价单号
	private String flowNo;
	private List<AddedService> addedServices;	//增值服务刷
	
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
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
	public String getQuoteNo() {
		return quoteNo;
	}
	public void setQuoteNo(String quoteNo) {
		this.quoteNo = quoteNo;
	}
	public String getVciQuestion() {
		return vciQuestion;
	}
	public void setVciQuestion(String vciQuestion) {
		this.vciQuestion = vciQuestion;
	}
	public String getVciAnswer() {
		return vciAnswer;
	}
	public void setVciAnswer(String vciAnswer) {
		this.vciAnswer = vciAnswer;
	}
	public String getTciQuestion() {
		return tciQuestion;
	}
	public void setTciQuestion(String tciQuestion) {
		this.tciQuestion = tciQuestion;
	}
	public String getTciAnswer() {
		return tciAnswer;
	}
	public void setTciAnswer(String tciAnswer) {
		this.tciAnswer = tciAnswer;
	}
	public String getInsuComApplicationNo() {
		return insuComApplicationNo;
	}
	public void setInsuComApplicationNo(String insuComApplicationNo) {
		this.insuComApplicationNo = insuComApplicationNo;
	}
	public List<AddedService> getAddedServices() {
		return addedServices;
	}
	public void setAddedServices(List<AddedService> addedServices) {
		this.addedServices = addedServices;
	}
	
}
