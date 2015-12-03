package anbox.aibc.appBeans.quotation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleInfo {
	private String cityCode;						  //城市
	private String licenseNo;                //车牌号码
	private String vehicleNo;				 //车辆编码，太保新车备案时使用
	private String frameNo;                  //车架号
	private String engineNo;                 //发动机号
	private String enrollDate;               //初次登记日期
	private double actualValue;              //车辆实际价值
	private double mdfyPurchasePrice;        //修改后新车购置价
	private String runMile;                  //年平均行驶里程
	private String totalRunMile;             //总行驶里程
	private String owner;                    //车主
	private String ownerCertType;            //证件类型
	private String ownerCertNo;              //证件号码
	private String ownerType;                //车主类型
	@JsonProperty(value="rBCode")
	private String rBCode;                   //车型代码       
	private String brandName;                //厂牌型号 或车型名称    
	private String vehicleType;              //车辆类型代码     
	private String vehicleTypeDesc;         //车辆类型描述     
	private double purchasePrice;            //新车购置价      
	private String fuelType;                 //燃料种类       
	private String vehicleBrand;             //车辆品牌       
	private String useType;                  //使用性质       
	private int seatCount;                   //核定载客       
	private double exhaustCapacity;          //排量         
	private String importFlag;          
	private String vehicleKind;          //车辆种类    
	
	//新车信息
	private String newVehicleFlag;           //新车标志       
	private String certDate;                 //行驶证发证日期
	private String invoiceDate;                  //购车发票日
	private String vehiOriginCertType;           //车辆来历凭证种类
	private String vehiOriginCertNo;             //车辆来历凭证编号
	private String vehiOriginCertDate;           //车辆来历凭证所载日期
	private String vehicleWeight;
	
	private String searchSequenceNo;//车辆查询码 Y必传
	
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
	public String getrBCode() {
		return rBCode;
	}
	public void setrBCode(String rBCode) {
		this.rBCode = rBCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
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
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public double getExhaustCapacity() {
		return exhaustCapacity;
	}
	public void setExhaustCapacity(double exhaustCapacity) {
		this.exhaustCapacity = exhaustCapacity;
	}
	public String getNewVehicleFlag() {
		return newVehicleFlag;
	}
	public void setNewVehicleFlag(String newVehicleFlag) {
		this.newVehicleFlag = newVehicleFlag;
	}
	public String getCertDate() {
		return certDate;
	}
	public void setCertDate(String certDate) {
		this.certDate = certDate;
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
	public String getImportFlag() {
		return importFlag;
	}
	public void setImportFlag(String importFlag) {
		this.importFlag = importFlag;
	}
	public String getVehicleKind() {
		return vehicleKind;
	}
	public void setVehicleKind(String vehicleKind) {
		this.vehicleKind = vehicleKind;
	}
	public String getVehicleWeight() {
		return vehicleWeight;
	}
	public void setVehicleWeight(String vehicleWeight) {
		this.vehicleWeight = vehicleWeight;
	}
}
