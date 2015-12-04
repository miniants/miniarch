package zbh.aibc.appBeans.vehicle;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsuVehicle {
	private String licenseNo;                //车牌号码
	private String vehicleNo;				 //车辆编码，太保新车备案时使用
	private String licenseColor;             //号牌底色
	private String licenseType;              //号牌类型
	private String frameNo;                  //车架号
	private String engineNo;                 //发动机号
	private String enrollDate;               //初次登记日期
	private String searchSequenceNo;         //车型查询码
	private String cityCode;                 //所在城市
	private double actualValue;              //车辆实际价值
	private double mdfyPurchasePrice;        //修改后新车购置价
	private String certDate;                 //行驶证发证日期
	private String transferFlag;             //过户车标志
	private String transferDate;             //转移登记日期
	private String hkFlag;                   //港粤两地车牌标志
	private String hkLicenseNo;              //港粤车牌号
	private String runMile;                  //年平均行驶里程
	private String totalRunMile;             //总行驶里程
	private String runArea;                  //行驶区域
	private String specialModelFlag;         //是否古老特异车型
	private String owner;                    //车主
	private String ownerCertType;            //证件类型
	private String ownerCertNo;              //证件号码
	private String ownerType;                //车主类型
	private String drivLicenseType;          //驾驶证类型
	private String invoiceDate;              //购车发票日
	private String vehiOriginCertType;       //车辆来历凭证种类
	private String vehiOriginCertNo;         //车辆来历凭证编号
	private String vehiOriginCertDate;       //车辆来历凭证所载日期
	@JsonProperty(value="rBCode")
	private String rBCode;                   //车型代码       
	private String brandName;                //厂牌型号 或车型名称    
	private String vehicleKind;              //车辆种类       
	private String newVehicleFlag;           //新车标志       
	private String ecdemicVehicleFlag;       //外地车标志      
	private String vehicleType;              //车辆类型代码     
	private String vehicleTypeDesc;         //车辆类型描述     
	private String bodyColor;                //车身颜色       
	private double purchasePrice;            //新车购置价      
	private String fuelType;                 //燃料种类       
	private String vehicleBrand;             //车辆品牌       
	private String importFlag;               //产地种类       
	private String makeDate;                 //出厂日期       
	private String manufacturer;             //厂商名称       
	private String vehicleSeries;            //车系名称       
	private String useType;                  //使用性质       
	private int seatCount;                   //核定载客       
	private String marketDate;               //年款(上市年份)   
	private double vehicleTonnage;           //核定载质量      
	private double exhaustCapacity;          //排量         
	private double vehicleWeight;            //整备质量       
	private String alarmFlag;                //是否有防盗装备    
	private int airBagNum;                   //安全气囊数      
	private String transmissionType;         //变速器形式      
	private String absFlag;                  //是否有ABS   
	private int vehicleAge;          //车龄
	private String tciLastEndDate;   //交强险上年保险止期
	private String vciLastEndDate;   //商业险上年保险止期
	private String sourceFlag;       //车辆来源标志
	private String splmtFlag;        //补充标志
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
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
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
	public String getAlarmFlag() {
		return alarmFlag;
	}
	public void setAlarmFlag(String alarmFlag) {
		this.alarmFlag = alarmFlag;
	}
	public int getAirBagNum() {
		return airBagNum;
	}
	public void setAirBagNum(int airBagNum) {
		this.airBagNum = airBagNum;
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
	public int getVehicleAge() {
		return vehicleAge;
	}
	public void setVehicleAge(int vehicleAge) {
		this.vehicleAge = vehicleAge;
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
	public String getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public String getSplmtFlag() {
		return splmtFlag;
	}
	public void setSplmtFlag(String splmtFlag) {
		this.splmtFlag = splmtFlag;
	}
	
}
