package anbox.aibc.appBeans.order;

public class OrderSubmitRvo {
	private String status;
	private String chargeOrderId;//收费订单号
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChargeOrderId() {
		return chargeOrderId;
	}
	public void setChargeOrderId(String chargeOrderId) {
		this.chargeOrderId = chargeOrderId;
	}
	
}
