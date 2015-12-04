package zbh.aibc.appBeans.vehicle;


public class VehicleBasicCvo {
	private String licenseNo;//Y
	
	private String cityCode;
	private String owner;
	private String ownerCertNo;
	private String ownerMobile;
	private String newVehicleFlag;
	
	public String getNewVehicleFlag() {
		return newVehicleFlag;
	}
	public void setNewVehicleFlag(String newVehicleFlag) {
		this.newVehicleFlag = newVehicleFlag;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
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
	
}
