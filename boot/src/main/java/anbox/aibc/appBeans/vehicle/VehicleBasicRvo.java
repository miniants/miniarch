package anbox.aibc.appBeans.vehicle;


public class VehicleBasicRvo {
	private String frameNo;//Y
	private String engineNo;//Y
	private String enrollDate;//Y
	
	//根据车牌改变查询返回信息部分
	private String owner;//Y
	private String ownerCertNo;//Y
	private String ownerMobile;//Y
	private String brandName;
	private String exhaustCapacity;
	private String purchasePrice;
	private String vehicleBrand;
	private String vehicleTypeDesc;
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
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
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerCertNo() {
		return ownerCertNo;
	}
	public void setOwnerCertNo(String ownerCertNo) {
		this.ownerCertNo = ownerCertNo;
	}
	public String getOwnerMobile() {
		return ownerMobile;
	}
	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
	}
	public String getExhaustCapacity() {
		return exhaustCapacity;
	}
	public void setExhaustCapacity(String exhaustCapacity) {
		this.exhaustCapacity = exhaustCapacity;
	}
	public String getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getVehicleTypeDesc() {
		return vehicleTypeDesc;
	}
	public void setVehicleTypeDesc(String vehicleTypeDesc) {
		this.vehicleTypeDesc = vehicleTypeDesc;
	}
	
}
