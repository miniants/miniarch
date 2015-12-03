package anbox.aibc.model.order;

import java.sql.Types;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;

public class ProductOrderDetail  extends ModelableImpl{
	private static final long serialVersionUID = 3649990524803010379L;
	private ProductOrder productOrder;
	private double orderPremium;//订单费用
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
    private String orderType;//订单类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String orderStatus;//订单状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
    private String policyNo;
    @SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
    private String productBrandCode;//产品品牌编码
    @SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
    private String productBrandName;//产品品牌名称
    @SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
    private String skuCode;//编码
    @SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
    private String skuName;//名称
    @SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
    private String skuDesc;//描述
    private String orderDetailDesc;//描述
    
	public String getOrderDetailDesc() {
		return orderDetailDesc;
	}
	public void setOrderDetailDesc(String orderDetailDesc) {
		this.orderDetailDesc = orderDetailDesc;
	}
	public ProductOrder getProductOrder() {
		return productOrder;
	}
	public void setProductOrder(ProductOrder productOrder) {
		this.productOrder = productOrder;
	}
	public double getOrderPremium() {
		return orderPremium;
	}
	public void setOrderPremium(double orderPremium) {
		this.orderPremium = orderPremium;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getProductBrandCode() {
		return productBrandCode;
	}
	public void setProductBrandCode(String productBrandCode) {
		this.productBrandCode = productBrandCode;
	}
	public String getProductBrandName() {
		return productBrandName;
	}
	public void setProductBrandName(String productBrandName) {
		this.productBrandName = productBrandName;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuDesc() {
		return skuDesc;
	}
	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
