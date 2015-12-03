package anbox.aibc.appBeans.vehicle;

import java.util.List;

public class InsuVehicleInfoRvo {
	private String insuCom;
	//返回
	private int iqvCount;
	private List<InsuVehicle> insuVehicles;
	public int getIqvCount() {
		return iqvCount;
	}
	public void setIqvCount(int iqvCount) {
		this.iqvCount = iqvCount;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public List<InsuVehicle> getInsuVehicles() {
		return insuVehicles;
	}
	public void setInsuVehicles(List<InsuVehicle> insuVehicles) {
		this.insuVehicles = insuVehicles;
	}
	
}
