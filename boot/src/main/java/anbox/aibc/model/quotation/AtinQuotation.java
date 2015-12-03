package anbox.aibc.model.quotation;

import java.sql.Types;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import anbox.aibc.model.member.Member;
import cn.remex.db.sql.SqlTypeAnnotation;

/**
 * 车险询价单信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinQuotation extends Quotation {
	private static final long serialVersionUID = -6241200401702090812L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String aitpQuotationNo;//车险平台内部询价单号
	private Member member;//所属会员  ???//网页版特有字段 
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String quotationStatus;

	
	@OneToMany(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private List<AtinQttnCustomer> atinCustomers; //询价客户信息
	@OneToMany(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private List<AtinQttnCoverage> atinCoverages; //询价条款信息
	@OneToMany(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private List<AtinQttnEngaged> atinEngageds;   //询价特别约定信息
	@OneToOne(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private AtinQttnTravelTax atinTravelTax;      //询价车船税信息
	@OneToOne(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private AtinQttnDriver atinDriver;            //询价驾驶人信息
	@OneToMany(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private List<AtinQttnEquipment> atinEquipments; //询价新增设备信息
	@OneToMany(mappedBy="atinQuotation", cascade={CascadeType.PERSIST})
	private List<AtinQttnFeeRate> atinFeeRates;          //询价费率信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String relationFlag;                      //关联单标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String tciSearchNo;                       //交强险平台查询码
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String tciPremSearchNo;                   //交强险保费计算查询码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciPolicyKind;                     //交强险保单种类
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String tciRnwlPolicyNo;                   //交强被续保单号
	private int tciNoDamageYears;                     //交强跨省未出险年数
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciFlag;                           //是否承保交强险
	@SqlTypeAnnotation(type=Types.CHAR, length = 200, sqlType = " ")
	private String tciUnInsureReason;                 //不投保交强险原因
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciLastFlag;                       //上年是否在本公司投保交强险
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String tciLastCompany;                    //上年交强险承保公司
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String tciLastPolicyNo;                   //上年交强险保单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String tciLastStartDate;                  //交强险上年保险起期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String tciLastEndDate;                    //交强险上年保险止期
	private double tciSumAmount;                      //交强险总保额
	private double tciBenchmarkPremium;               //交强险折前保费
	private double tciPremium;                        //交强险折后保费
	private double tciDiscountAmount;                 //交强险总折扣金额
	private double tciDiscount;                       //交强险折扣
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String tciStartDate;                      //交强险起保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciStartHour;                      //交强险起保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String tciEndDate;                        //交强险终保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciEndHour;                        //交强险终保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciSuccessFlag;                    //交强险报价成功标识
	@SqlTypeAnnotation(type=Types.CHAR, length = 1000, sqlType = " ")
	private String tciFailReason;                     //交强险报价失败原因
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String tciUdwrtFlag;                      //交强险核保标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 500, sqlType = " ")
	private String tciUdwrtMessage;                   //交强险核保信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String vciSearchNo;                       //商业险平台查询码
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String vciPremSearchNo;                   //商业险保费计算查询码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vciPolicyKind;                     //商业险保单种类
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String vciRnwlPolicyNo;                   //商业被续保单号
	private int vciNoDamageYears;                     //商业跨省未出险年数
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vciLastFlag;                       //上年是否在本公司投保商业险
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String vciLastStartDate;                  //商业险上年保险起期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String vciLastEndDate;                    //商业险上年保险止期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String vciStartDate;                      //商业险起保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vciStartHour;                      //商业险起保小时
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String vciEndDate;                        //商业险终保日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vciEndHour;                        //商业险终保小时
	private double vciBenchmarkPremium;               //商业险折前保费
	private double vciPremium;                        //商业险折后保费
	private double vciDiscount;                       //商业险折扣
	private double vciDiscountAmount;                 //商业险总折扣金额
	private double vciAdtnlFee;                       //商业险总附加费
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vciSuccessFlag;                    //商业险报价成功标识
	@SqlTypeAnnotation(type=Types.CHAR, length = 1000, sqlType = " ")
	private String vciFailReason;                     //商业险报价失败原因
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vciUdwrtFlag;                      //商业险核保标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 500, sqlType = " ")
	private String vciUdwrtMessage;                   //商业险核保信息
	private double lateFee;                           //滞纳金 
	private double actualTravelTax;                   //当年应缴车船税
	private double previousPay;                       //往年补交
	private double sumTravelTax;                      //总车船税
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vehicleRelation;                   //车辆所有关系
	private int vehicleAge;                           //车龄
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String deviceFlag;                        //新增设备标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String driverFlag;                        //是否指定驾驶员标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String proxyDriver;                       //指定驾驶员
	private int noDamageYears;                        //跨省首年投保未出险年数
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String inDoorFlag;                        //是否上门投保业务
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vehicleCheckFlag;                  //验车情况
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String vehicleCheckDate;                  //验车日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String vehicleId;                         //车辆编码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String licenseNo;                         //车牌号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String vehicleNo;				          //车辆编码，太保新车备案时使用
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String licenseColor;                      //号牌底色
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String licenseType;                       //号牌类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String frameNo;                           //车架号
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String engineNo;                          //发动机号
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String enrollDate;                        //初次登记日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String searchSequenceNo;                  //车型查询码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String cityCode;                          //所在城市
	private double actualValue;                       //车辆实际价值
	private double mdfyPurchasePrice;                 //修改后新车购置价
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String certDate;                          //行驶证发证日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String transferFlag;                      //过户车标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String transferDate;                      //转移登记日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String hkFlag;                            //港粤两地车牌标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String hkLicenseNo;                       //港粤车牌号
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String runMile;                           //年平均行驶里程
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String totalRunMile;                      //总行驶里程
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String runArea;                           //行驶区域
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String specialModelFlag;                  //是否古老特异车型
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String owner;                             //车主
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String ownerCertType;                     //证件类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String ownerCertNo;                       //证件号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String ownerType;                         //车主类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String drivLicenseType;                   //驾驶证类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String rBCode;                            //车型代码                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String brandName;                         //厂牌型号                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vehicleKind;                       //车辆种类                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String newVehicleFlag;                    //新车标志
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String ecdemicVehicleFlag;                //外地车标志     
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String vehicleType;                       //车辆类型代码                
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String vehicleTypeDesc;                   //车辆类型描述                
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String bodyColor;                        //车身颜色                  
	private double purchasePrice;                     //新车购置价                 
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String fuelType;                          //燃料种类                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String vehicleBrand;                      //车辆品牌                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String importFlag;                        //产地种类                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String makeDate;                          //出厂日期                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String manufacturer;                      //厂商名称                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String vehicleSeries;                     //车系名称                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String useType;                           //使用性质                  
	private int seatCount;                            //核定载客                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String marketDate;                        //年款(上市年份)              
	private double vehicleTonnage;                    //核定载质量                 
	private double exhaustCapacity;                   //排量                    
	private double vehicleWeight;                     //整备质量                  
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String alarmFlag;                         //是否有防盗装备               
	private int airBagNum;                            //安全气囊数                
	@SqlTypeAnnotation(type=Types.CHAR, length = 15, sqlType = " ")
	private String transmissionType;                  //变速器形式                 
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String absFlag;                           //是否有ABS    
	
	//2014-07-09 针对转保车问题
	private String vciQuestion;				//商业险验证码问题
	private String vciAnswer;					//商业险验证码问题答案
	private String tciQuestion;				//交强险验证码问题
	private String tciAnswer;					//交强险验证码问题答案
	private String insuComQuoteNo;	//保险公司询价单号
	
	//2014-04-14@针对 江苏 测试环境
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String ineffectualDate; //校验有效日期止
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String rejectDate;     //强制有效期止
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String lastCheckDate;  //最近定检日期
	//2014-04-21 富德报价必传
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String platformRbCode; //平台车型代码
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getAitpQuotationNo() {
		return aitpQuotationNo;
	}
	public void setAitpQuotationNo(String aitpQuotationNo) {
		this.aitpQuotationNo = aitpQuotationNo;
	}
	public List<AtinQttnCustomer> getAtinCustomers() {
		return atinCustomers;
	}
	public void setAtinCustomers(List<AtinQttnCustomer> atinCustomers) {
		this.atinCustomers = atinCustomers;
	}
	public List<AtinQttnCoverage> getAtinCoverages() {
		return atinCoverages;
	}
	public void setAtinCoverages(List<AtinQttnCoverage> atinCoverages) {
		this.atinCoverages = atinCoverages;
	}
	public List<AtinQttnEngaged> getAtinEngageds() {
		return atinEngageds;
	}
	public void setAtinEngageds(List<AtinQttnEngaged> atinEngageds) {
		this.atinEngageds = atinEngageds;
	}
	public List<AtinQttnEquipment> getAtinEquipments() {
		return atinEquipments;
	}
	public void setAtinEquipments(List<AtinQttnEquipment> atinEquipments) {
		this.atinEquipments = atinEquipments;
	}
	public AtinQttnTravelTax getAtinTravelTax() {
		return atinTravelTax;
	}
	public void setAtinTravelTax(AtinQttnTravelTax atinTravelTax) {
		this.atinTravelTax = atinTravelTax;
	}
	public AtinQttnDriver getAtinDriver() {
		return atinDriver;
	}
	public void setAtinDriver(AtinQttnDriver atinDriver) {
		this.atinDriver = atinDriver;
	}
	public List<AtinQttnFeeRate> getAtinFeeRates() {
		return atinFeeRates;
	}
	public void setAtinFeeRates(List<AtinQttnFeeRate> atinFeeRates) {
		this.atinFeeRates = atinFeeRates;
	}
	public String getRelationFlag() {
		return relationFlag;
	}
	public void setRelationFlag(String relationFlag) {
		this.relationFlag = relationFlag;
	}
	public String getTciSearchNo() {
		return tciSearchNo;
	}
	public void setTciSearchNo(String tciSearchNo) {
		this.tciSearchNo = tciSearchNo;
	}
	public String getTciPolicyKind() {
		return tciPolicyKind;
	}
	public void setTciPolicyKind(String tciPolicyKind) {
		this.tciPolicyKind = tciPolicyKind;
	}
	public String getTciRnwlPolicyNo() {
		return tciRnwlPolicyNo;
	}
	public void setTciRnwlPolicyNo(String tciRnwlPolicyNo) {
		this.tciRnwlPolicyNo = tciRnwlPolicyNo;
	}
	public String getTciFlag() {
		return tciFlag;
	}
	public void setTciFlag(String tciFlag) {
		this.tciFlag = tciFlag;
	}
	public String getTciUnInsureReason() {
		return tciUnInsureReason;
	}
	public void setTciUnInsureReason(String tciUnInsureReason) {
		this.tciUnInsureReason = tciUnInsureReason;
	}
	public String getTciLastFlag() {
		return tciLastFlag;
	}
	public void setTciLastFlag(String tciLastFlag) {
		this.tciLastFlag = tciLastFlag;
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
	public String getTciLastStartDate() {
		return tciLastStartDate;
	}
	public void setTciLastStartDate(String tciLastStartDate) {
		this.tciLastStartDate = tciLastStartDate;
	}
	public String getTciLastEndDate() {
		return tciLastEndDate;
	}
	public void setTciLastEndDate(String tciLastEndDate) {
		this.tciLastEndDate = tciLastEndDate;
	}
	public String getTciStartDate() {
		return tciStartDate;
	}
	public void setTciStartDate(String tciStartDate) {
		this.tciStartDate = tciStartDate;
	}
	public String getTciStartHour() {
		return tciStartHour;
	}
	public void setTciStartHour(String tciStartHour) {
		this.tciStartHour = tciStartHour;
	}
	public String getTciEndDate() {
		return tciEndDate;
	}
	public void setTciEndDate(String tciEndDate) {
		this.tciEndDate = tciEndDate;
	}
	public String getTciEndHour() {
		return tciEndHour;
	}
	public void setTciEndHour(String tciEndHour) {
		this.tciEndHour = tciEndHour;
	}
	public String getTciSuccessFlag() {
		return tciSuccessFlag;
	}
	public void setTciSuccessFlag(String tciSuccessFlag) {
		this.tciSuccessFlag = tciSuccessFlag;
	}
	public String getTciFailReason() {
		return tciFailReason;
	}
	public void setTciFailReason(String tciFailReason) {
		this.tciFailReason = tciFailReason;
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
	public String getVciSearchNo() {
		return vciSearchNo;
	}
	public void setVciSearchNo(String vciSearchNo) {
		this.vciSearchNo = vciSearchNo;
	}
	public String getVciPolicyKind() {
		return vciPolicyKind;
	}
	public void setVciPolicyKind(String vciPolicyKind) {
		this.vciPolicyKind = vciPolicyKind;
	}
	public String getVciRnwlPolicyNo() {
		return vciRnwlPolicyNo;
	}
	public void setVciRnwlPolicyNo(String vciRnwlPolicyNo) {
		this.vciRnwlPolicyNo = vciRnwlPolicyNo;
	}
	public String getVciLastFlag() {
		return vciLastFlag;
	}
	public void setVciLastFlag(String vciLastFlag) {
		this.vciLastFlag = vciLastFlag;
	}
	public String getVciLastStartDate() {
		return vciLastStartDate;
	}
	public void setVciLastStartDate(String vciLastStartDate) {
		this.vciLastStartDate = vciLastStartDate;
	}
	public String getVciLastEndDate() {
		return vciLastEndDate;
	}
	public void setVciLastEndDate(String vciLastEndDate) {
		this.vciLastEndDate = vciLastEndDate;
	}
	public String getVciStartDate() {
		return vciStartDate;
	}
	public void setVciStartDate(String vciStartDate) {
		this.vciStartDate = vciStartDate;
	}
	public String getVciStartHour() {
		return vciStartHour;
	}
	public void setVciStartHour(String vciStartHour) {
		this.vciStartHour = vciStartHour;
	}
	public String getVciEndDate() {
		return vciEndDate;
	}
	public void setVciEndDate(String vciEndDate) {
		this.vciEndDate = vciEndDate;
	}
	public String getVciEndHour() {
		return vciEndHour;
	}
	public void setVciEndHour(String vciEndHour) {
		this.vciEndHour = vciEndHour;
	}
	public String getVehicleRelation() {
		return vehicleRelation;
	}
	public void setVehicleRelation(String vehicleRelation) {
		this.vehicleRelation = vehicleRelation;
	}
	public String getDeviceFlag() {
		return deviceFlag;
	}
	public void setDeviceFlag(String deviceFlag) {
		this.deviceFlag = deviceFlag;
	}
	public String getDriverFlag() {
		return driverFlag;
	}
	public void setDriverFlag(String driverFlag) {
		this.driverFlag = driverFlag;
	}
	public String getProxyDriver() {
		return proxyDriver;
	}
	public void setProxyDriver(String proxyDriver) {
		this.proxyDriver = proxyDriver;
	}
	public String getInDoorFlag() {
		return inDoorFlag;
	}
	public void setInDoorFlag(String inDoorFlag) {
		this.inDoorFlag = inDoorFlag;
	}
	public String getVehicleCheckFlag() {
		return vehicleCheckFlag;
	}
	public void setVehicleCheckFlag(String vehicleCheckFlag) {
		this.vehicleCheckFlag = vehicleCheckFlag;
	}
	public String getVehicleCheckDate() {
		return vehicleCheckDate;
	}
	public void setVehicleCheckDate(String vehicleCheckDate) {
		this.vehicleCheckDate = vehicleCheckDate;
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
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getLicenseColor() {
		return licenseColor;
	}
	public void setLicenseColor(String licenseColor) {
		this.licenseColor = licenseColor;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	public String getSearchSequenceNo() {
		return searchSequenceNo;
	}
	public void setSearchSequenceNo(String searchSequenceNo) {
		this.searchSequenceNo = searchSequenceNo;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCertDate() {
		return certDate;
	}
	public void setCertDate(String certDate) {
		this.certDate = certDate;
	}
	public String getTransferFlag() {
		return transferFlag;
	}
	public void setTransferFlag(String transferFlag) {
		this.transferFlag = transferFlag;
	}
	public String getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
	public String getHkFlag() {
		return hkFlag;
	}
	public void setHkFlag(String hkFlag) {
		this.hkFlag = hkFlag;
	}
	public String getHkLicenseNo() {
		return hkLicenseNo;
	}
	public void setHkLicenseNo(String hkLicenseNo) {
		this.hkLicenseNo = hkLicenseNo;
	}
	public String getRunMile() {
		return runMile;
	}
	public void setRunMile(String runMile) {
		this.runMile = runMile;
	}
	public String getTotalRunMile() {
		return totalRunMile;
	}
	public void setTotalRunMile(String totalRunMile) {
		this.totalRunMile = totalRunMile;
	}
	public String getRunArea() {
		return runArea;
	}
	public void setRunArea(String runArea) {
		this.runArea = runArea;
	}
	public String getSpecialModelFlag() {
		return specialModelFlag;
	}
	public void setSpecialModelFlag(String specialModelFlag) {
		this.specialModelFlag = specialModelFlag;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerCertType() {
		return ownerCertType;
	}
	public void setOwnerCertType(String ownerCertType) {
		this.ownerCertType = ownerCertType;
	}
	public String getOwnerCertNo() {
		return ownerCertNo;
	}
	public void setOwnerCertNo(String ownerCertNo) {
		this.ownerCertNo = ownerCertNo;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getDrivLicenseType() {
		return drivLicenseType;
	}
	public void setDrivLicenseType(String drivLicenseType) {
		this.drivLicenseType = drivLicenseType;
	}
	public String getRBCode() {
		return rBCode;
	}
	public void setRBCode(String rBCode) {
		this.rBCode = rBCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getVehicleKind() {
		return vehicleKind;
	}
	public void setVehicleKind(String vehicleKind) {
		this.vehicleKind = vehicleKind;
	}
	public String getNewVehicleFlag() {
		return newVehicleFlag;
	}
	public void setNewVehicleFlag(String newVehicleFlag) {
		this.newVehicleFlag = newVehicleFlag;
	}
	public String getEcdemicVehicleFlag() {
		return ecdemicVehicleFlag;
	}
	public void setEcdemicVehicleFlag(String ecdemicVehicleFlag) {
		this.ecdemicVehicleFlag = ecdemicVehicleFlag;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVehicleTypeDesc() {
		return vehicleTypeDesc;
	}
	public void setVehicleTypeDesc(String vehicleTypeDesc) {
		this.vehicleTypeDesc = vehicleTypeDesc;
	}
	public String getBodyColor() {
		return bodyColor;
	}
	public void setBodyColor(String bodyColor) {
		this.bodyColor = bodyColor;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getImportFlag() {
		return importFlag;
	}
	public void setImportFlag(String importFlag) {
		this.importFlag = importFlag;
	}
	public String getMakeDate() {
		return makeDate;
	}
	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getVehicleSeries() {
		return vehicleSeries;
	}
	public void setVehicleSeries(String vehicleSeries) {
		this.vehicleSeries = vehicleSeries;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
	}
	public String getAlarmFlag() {
		return alarmFlag;
	}
	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}
	public String getTransmissionType() {
		return transmissionType;
	}
	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}
	public String getAbsFlag() {
		return absFlag;
	}
	public void setAbsFlag(String absFlag) {
		this.absFlag = absFlag;
	}
	public int getTciNoDamageYears() {
		return tciNoDamageYears;
	}
	public void setTciNoDamageYears(int tciNoDamageYears) {
		this.tciNoDamageYears = tciNoDamageYears;
	}
	public double getTciSumAmount() {
		return tciSumAmount;
	}
	public void setTciSumAmount(double tciSumAmount) {
		this.tciSumAmount = tciSumAmount;
	}
	public double getTciBenchmarkPremium() {
		return tciBenchmarkPremium;
	}
	public void setTciBenchmarkPremium(double tciBenchmarkPremium) {
		this.tciBenchmarkPremium = tciBenchmarkPremium;
	}
	public double getTciPremium() {
		return tciPremium;
	}
	public void setTciPremium(double tciPremium) {
		this.tciPremium = tciPremium;
	}
	public double getTciDiscountAmount() {
		return tciDiscountAmount;
	}
	public void setTciDiscountAmount(double tciDiscountAmount) {
		this.tciDiscountAmount = tciDiscountAmount;
	}
	public double getTciDiscount() {
		return tciDiscount;
	}
	public void setTciDiscount(double tciDiscount) {
		this.tciDiscount = tciDiscount;
	}
	public int getVciNoDamageYears() {
		return vciNoDamageYears;
	}
	public void setVciNoDamageYears(int vciNoDamageYears) {
		this.vciNoDamageYears = vciNoDamageYears;
	}
	public double getVciBenchmarkPremium() {
		return vciBenchmarkPremium;
	}
	public void setVciBenchmarkPremium(double vciBenchmarkPremium) {
		this.vciBenchmarkPremium = vciBenchmarkPremium;
	}
	public double getVciPremium() {
		return vciPremium;
	}
	public void setVciPremium(double vciPremium) {
		this.vciPremium = vciPremium;
	}
	public double getVciDiscount() {
		return vciDiscount;
	}
	public void setVciDiscount(double vciDiscount) {
		this.vciDiscount = vciDiscount;
	}
	public double getVciDiscountAmount() {
		return vciDiscountAmount;
	}
	public void setVciDiscountAmount(double vciDiscountAmount) {
		this.vciDiscountAmount = vciDiscountAmount;
	}
	public double getVciAdtnlFee() {
		return vciAdtnlFee;
	}
	public void setVciAdtnlFee(double vciAdtnlFee) {
		this.vciAdtnlFee = vciAdtnlFee;
	}
	public String getVciSuccessFlag() {
		return vciSuccessFlag;
	}
	public void setVciSuccessFlag(String vciSuccessFlag) {
		this.vciSuccessFlag = vciSuccessFlag;
	}
	public String getVciFailReason() {
		return vciFailReason;
	}
	public void setVciFailReason(String vciFailReason) {
		this.vciFailReason = vciFailReason;
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
	public int getVehicleAge() {
		return vehicleAge;
	}
	public void setVehicleAge(int vehicleAge) {
		this.vehicleAge = vehicleAge;
	}
	public int getNoDamageYears() {
		return noDamageYears;
	}
	public void setNoDamageYears(int noDamageYears) {
		this.noDamageYears = noDamageYears;
	}
	public double getActualValue() {
		return actualValue;
	}
	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	public double getMdfyPurchasePrice() {
		return mdfyPurchasePrice;
	}
	public void setMdfyPurchasePrice(double mdfyPurchasePrice) {
		this.mdfyPurchasePrice = mdfyPurchasePrice;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public double getVehicleTonnage() {
		return vehicleTonnage;
	}
	public void setVehicleTonnage(double vehicleTonnage) {
		this.vehicleTonnage = vehicleTonnage;
	}
	public double getExhaustCapacity() {
		return exhaustCapacity;
	}
	public void setExhaustCapacity(double exhaustCapacity) {
		this.exhaustCapacity = exhaustCapacity;
	}
	public double getVehicleWeight() {
		return vehicleWeight;
	}
	public void setVehicleWeight(double vehicleWeight) {
		this.vehicleWeight = vehicleWeight;
	}
	public int getAirBagNum() {
		return airBagNum;
	}
	public void setAirBagNum(int airBagNum) {
		this.airBagNum = airBagNum;
	}
	public String getTciPremSearchNo() {
		return tciPremSearchNo;
	}
	public void setTciPremSearchNo(String tciPremSearchNo) {
		this.tciPremSearchNo = tciPremSearchNo;
	}
	public String getVciPremSearchNo() {
		return vciPremSearchNo;
	}
	public void setVciPremSearchNo(String vciPremSearchNo) {
		this.vciPremSearchNo = vciPremSearchNo;
	}
	public double getLateFee() {
		return lateFee;
	}
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
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
	public String getIneffectualDate() {
		return ineffectualDate;
	}
	public void setIneffectualDate(String ineffectualDate) {
		this.ineffectualDate = ineffectualDate;
	}
	public String getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(String rejectDate) {
		this.rejectDate = rejectDate;
	}
	public String getLastCheckDate() {
		return lastCheckDate;
	}
	public void setLastCheckDate(String lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}
	public String getPlatformRbCode() {
		return platformRbCode;
	}
	public void setPlatformRbCode(String platformRbCode) {
		this.platformRbCode = platformRbCode;
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
	public String getInsuComQuoteNo() {
		return insuComQuoteNo;
	}
	public void setInsuComQuoteNo(String insuComQuoteNo) {
		this.insuComQuoteNo = insuComQuoteNo;
	}
	public String getQuotationStatus() {
		return quotationStatus;
	}
	public void setQuotationStatus(String quotationStatus) {
		this.quotationStatus = quotationStatus;
	}

}                                                                              
