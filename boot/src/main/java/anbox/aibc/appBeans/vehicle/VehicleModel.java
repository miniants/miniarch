package anbox.aibc.appBeans.vehicle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleModel {
	private String brandName;//厂牌型号
	@JsonProperty(value="rBCode")
	private String rbCode;
	private String vehicleBrand;//车辆品牌
	private String purchasePrice;
	private String vehicleType;
	private String vehicleTypeDesc;
	private String exhaustCapacity;
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getRbCode() {
		return rbCode;
	}
	public void setRbCode(String rbCode) {
		this.rbCode = rbCode;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
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
	public String getExhaustCapacity() {
		return exhaustCapacity;
	}
	public void setExhaustCapacity(String exhaustCapacity) {
		this.exhaustCapacity = exhaustCapacity;
	}
	
}
