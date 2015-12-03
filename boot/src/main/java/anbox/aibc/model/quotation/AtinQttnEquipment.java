package anbox.aibc.model.quotation;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
/**
 * 询价单设备信息
 * @author guoshaopeng
 * @since  2013-8-28
 */
public class AtinQttnEquipment extends ModelableImpl {
	private static final long serialVersionUID = -5455842401472651628L;
	private AtinQuotation atinQuotation;
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String deviceName;        //新增设备名称
	private int quantity;             //数量
	private double actualValue;       //设备实际价值
	private double totalPrice;        //新件重置价合计
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String purchaseDate;      //购置日期
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String devicePrdcArea;     //设备产地
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String deviceType;         //设备品牌型号
	private double purchasePrice;      //设备购买金额

	public AtinQuotation getAtinQuotation() {
		return atinQuotation;
	}
	public void setAtinQuotation(AtinQuotation atinQuotation) {
		this.atinQuotation = atinQuotation;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getActualValue() {
		return actualValue;
	}
	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getDevicePrdcArea() {
		return devicePrdcArea;
	}
	public void setDevicePrdcArea(String devicePrdcArea) {
		this.devicePrdcArea = devicePrdcArea;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public double getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
}
