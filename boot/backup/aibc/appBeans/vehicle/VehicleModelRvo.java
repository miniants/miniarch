package zbh.aibc.appBeans.vehicle;

import java.util.List;

public class VehicleModelRvo {
	private String frameNo;
	private String brandName;
	private int vmCount;
	private List<VehicleModel> vehicleModels;
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public int getVmCount() {
		return vmCount;
	}
	public void setVmCount(int vmCount) {
		this.vmCount = vmCount;
	}
	public List<VehicleModel> getVehicleModels() {
		return vehicleModels;
	}
	public void setVehicleModels(List<VehicleModel> vehicleModels) {
		this.vehicleModels = vehicleModels;
	}
	
}
