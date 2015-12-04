package zbh.aibc.appBeans.policyLastEndDate;

import anbox.aibc.appBeans.quotation.VehicleInfo;

public class EndDateCvo {
	private String insuCom;
	private String cityCode;
	private VehicleInfo vehicleInfo;
	private String flowNo;
	
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
	public VehicleInfo getVehicleInfo() {
		return vehicleInfo;
	}
	public void setVehicleInfo(VehicleInfo vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}
	
}
