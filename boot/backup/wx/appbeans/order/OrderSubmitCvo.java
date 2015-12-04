package zbh.wx.appbeans.order;

public class OrderSubmitCvo {
	private String chargeComCode;//Y 支付渠道 ZFB WX
	private String chargeOrderId;//收费订单号 Y
	private String insuCom;//Y 产品供应商
	private String name; //Y?
	private String orderDatas;
	private String orderNo;//订单号 Y
	private String orderAmount;//金额Y
	private String rcvEmail;
	private String rcvMobile;//Y?
	private String rcvName;//Y?
	private String rcvPostAddress;//Y?
	private String rcvPostCode;//Y?
	private String rcvTel;
	private String userId;//会员名 Y?
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getChargeComCode() {
		return chargeComCode;
	}
	public String getChargeOrderId() {
		return chargeOrderId;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public String getName() {
		return name;
	}
	public String getOrderDatas() {
		return orderDatas;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public String getRcvEmail() {
		return rcvEmail;
	}
	public String getRcvMobile() {
		return rcvMobile;
	}
	public String getRcvName() {
		return rcvName;
	}
	public String getRcvPostAddress() {
		return rcvPostAddress;
	}
	public String getRcvPostCode() {
		return rcvPostCode;
	}
	public String getRcvTel() {
		return rcvTel;
	}
	public String getUserId() {
		return userId;
	}
	public void setChargeComCode(String setChargeComCode) {
		this.chargeComCode = setChargeComCode;
	}
	public void setChargeOrderId(String chargeOrderId) {
		this.chargeOrderId = chargeOrderId;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOrderDatas(String orderDatas) {
		this.orderDatas = orderDatas;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setRcvEmail(String rcvEmail) {
		this.rcvEmail = rcvEmail;
	}
	public void setRcvMobile(String rcvMobile) {
		this.rcvMobile = rcvMobile;
	}
	public void setRcvName(String rcvName) {
		this.rcvName = rcvName;
	}
	public void setRcvPostAddress(String rcvPostAddress) {
		this.rcvPostAddress = rcvPostAddress;
	}
	public void setRcvPostCode(String rcvPostCode) {
		this.rcvPostCode = rcvPostCode;
	}
	public void setRcvTel(String rcvTel) {
		this.rcvTel = rcvTel;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
