package zbh.wx.appbeans.quotation;

import java.util.List;

public class QuotePriceCvo {
	private String insuCom;                      //保险公司
	private List<CustomerInfo> atinCustomers;   //人员信息
	private List<CoverageInfoCvo> atinCoverages;   //险种信息
	private VehicleInfo vehicleInfo;//车辆信息
	private String cityCode;						  //城市
	private String actualValue;						  //实际价值
	private String packageType;						  //套餐类型
	private String tciFlag;						  //是否投保交强险
	private String travelTaxFlag;                //车船税缴纳标志
	private String taxStatus;                    //完税状态
	private String tciStartDate;                 //交强险起保日期
	private String vciStartDate;                 //商业险起保日期
	
	private String tciLastCompany;               //上年交强险承保公司
	private String tciLastPolicyNo;              //上年交强险保单号
	private String tciLastEndDate;               //交强险上年保险止期
	private String vciLastEndDate;               //商业险上年保险止期
	//转保信息
	private String vciQuestion;				//商业险验证码问题
	private String vciAnswer;					//商业险验证码问题答案
	private String tciQuestion;				//交强险验证码问题
	private String tciAnswer;					//交强险验证码问题答案
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public List<CustomerInfo> getAtinCustomers() {
		return atinCustomers;
	}
	public void setAtinCustomers(List<CustomerInfo> atinCustomers) {
		this.atinCustomers = atinCustomers;
	}
	public List<CoverageInfoCvo> getAtinCoverages() {
		return atinCoverages;
	}
	public void setAtinCoverages(List<CoverageInfoCvo> atinCoverages) {
		this.atinCoverages = atinCoverages;
	}
	public VehicleInfo getVehicleInfo() {
		return vehicleInfo;
	}
	public void setVehicleInfo(VehicleInfo vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getTciFlag() {
		return tciFlag;
	}
	public void setTciFlag(String tciFlag) {
		this.tciFlag = tciFlag;
	}
	public String getTravelTaxFlag() {
		return travelTaxFlag;
	}
	public void setTravelTaxFlag(String travelTaxFlag) {
		this.travelTaxFlag = travelTaxFlag;
	}
	public String getTaxStatus() {
		return taxStatus;
	}
	public void setTaxStatus(String taxStatus) {
		this.taxStatus = taxStatus;
	}
	public String getTciStartDate() {
		return tciStartDate;
	}
	public void setTciStartDate(String tciStartDate) {
		this.tciStartDate = tciStartDate;
	}
	public String getVciStartDate() {
		return vciStartDate;
	}
	public void setVciStartDate(String vciStartDate) {
		this.vciStartDate = vciStartDate;
	}
	public String getTciLastCompany() {
		return tciLastCompany;
	}
	public void setTciLastCompany(String tciLastCompany) {
		this.tciLastCompany = tciLastCompany;
	}
	public String getTciLastPolicyNo() {
		return tciLastPolicyNo;
	}
	public void setTciLastPolicyNo(String tciLastPolicyNo) {
		this.tciLastPolicyNo = tciLastPolicyNo;
	}
	public String getTciLastEndDate() {
		return tciLastEndDate;
	}
	public void setTciLastEndDate(String tciLastEndDate) {
		this.tciLastEndDate = tciLastEndDate;
	}
	public String getVciLastEndDate() {
		return vciLastEndDate;
	}
	public void setVciLastEndDate(String vciLastEndDate) {
		this.vciLastEndDate = vciLastEndDate;
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
	public String getActualValue() {
		return actualValue;
	}
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	
}
