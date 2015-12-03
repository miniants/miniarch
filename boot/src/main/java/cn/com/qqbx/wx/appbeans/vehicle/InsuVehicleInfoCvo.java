package cn.com.qqbx.wx.appbeans.vehicle;

public class InsuVehicleInfoCvo {
	private String insuCom;
	
	private String cityCode;//Y
	private String licenseNo;//Y
	private String frameNo;//Y
	private String engineNo;//Y
	private String enrollDate;//初登日期(除了北京必传)
	private String owner;
	private String ownerCertNo;
	private String brandName;//厂牌型号
	private String vehicleBrand;//车辆品牌
	private String useType;//使用性质，默认8A
	private String bodyColor;                //车身颜色
	private String licenseColor;             //号牌底色
	private String licenseType;              //号牌类型
	private String ecdemicVehicleFlag;       //外地车标志//Y
	private String vehicleType;       //车辆类型
	private String vehicleKind;       //车辆种类
	private String seatCount;

	//新车信息
	private String newVehicleFlag;//新车标志Y
	private String invoiceDate;              //购车发票日
	private String certDate;              //发证日期
	
	private String vehiOriginCertType;       //车辆来历凭证种类
	private String vehiOriginCertNo;         //车辆来历凭证编号
	private String vehiOriginCertDate;       //车辆来历凭证所载日期
	private String vehicleTypeDesc;       //车辆类型描述
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
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getBodyColor() {
		return bodyColor;
	}
	public void setBodyColor(String bodyColor) {
		this.bodyColor = bodyColor;
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
	public String getVehicleKind() {
		return vehicleKind;
	}
	public void setVehicleKind(String vehicleKind) {
		this.vehicleKind = vehicleKind;
	}
	public String getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(String seatCount) {
		this.seatCount = seatCount;
	}
	public String getNewVehicleFlag() {
		return newVehicleFlag;
	}
	public void setNewVehicleFlag(String newVehicleFlag) {
		this.newVehicleFlag = newVehicleFlag;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
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
	public String getCarCertificateDate() {
		return certDate;
	}
	public void setCarCertificateDate(String certDate) {
		this.certDate = certDate;
	}
	public String getVehicleTypeDesc() {
		return vehicleTypeDesc;
	}
	public void setVehicleTypeDesc(String vehicleTypeDesc) {
		this.vehicleTypeDesc = vehicleTypeDesc;
	}
	public String getOwnerCertNo() {
		return ownerCertNo;
	}
	public void setOwnerCertNo(String ownerCertNo) {
		this.ownerCertNo = ownerCertNo;
	}
	
}
