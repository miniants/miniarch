package zbh.aibc.appBeans.mmbCenter;

public class VehicleInfo {
	private String vehicleId;//用于前端详情展示
	private String licenseNo;
	private String frameNo;
	private String engineNo;
	private String brandName;
	private int seatCount;
	private String exhaustCapacity;//排量
	private String enrollDate;//初登日期
	private String newVehicleFlag;//新车标志
	//详情信息
	private String licenseType;
	private String cityCode;
	private String owner;
	private String ownerMobile;
	private String certDate;//行驶证发证日期
	private String useType;//使用性质
	private String ownerCertNo;
	private double purchasePrice;
	private String tciStartDate;   
	private String vciStartDate;   
	private String tciEndDate;   
	private String vciEndDate;   
	
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
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(int seatCount) {
		this.seatCount = seatCount;
	}
	public String getExhaustCapacity() {
		return exhaustCapacity;
	}
	public void setExhaustCapacity(String exhaustCapacity) {
		this.exhaustCapacity = exhaustCapacity;
	}
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCertDate() {
		return certDate;
	}
	public void setCertDate(String certDate) {
		this.certDate = certDate;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getOwnerCertNo() {
		return ownerCertNo;
	}
	public void setOwnerCertNo(String ownerCertNo) {
		this.ownerCertNo = ownerCertNo;
	}
	public String getNewVehicleFlag() {
		return newVehicleFlag;
	}
	public void setNewVehicleFlag(String newVehicleFlag) {
		this.newVehicleFlag = newVehicleFlag;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getOwnerMobile() {
		return ownerMobile;
	}
	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
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
	public String getTciEndDate() {
		return tciEndDate;
	}
	public void setTciEndDate(String tciEndDate) {
		this.tciEndDate = tciEndDate;
	}
	public String getVciEndDate() {
		return vciEndDate;
	}
	public void setVciEndDate(String vciEndDate) {
		this.vciEndDate = vciEndDate;
	}
	
}
