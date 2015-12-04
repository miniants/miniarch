package zbh.wx.appbeans.policyLastEndDate;

public class EndDateRvo {
	private String insuCom;//保险公司
	private String tciLastEndDate;//交强险上年之前
	private String vciLastEndDate;//商业险上年之前
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public String getTciLastEndDate() {
		return tciLastEndDate;
	}
	public void setTciLastEndDate(String tciLastEndDate) {
		this.tciLastEndDate = tciLastEndDate;
	}
	public String getVciLastEndDate() {
		return vciLastEndDate;
	}
	public void setVciLastEndDate(String vciLastEndDate) {
		this.vciLastEndDate = vciLastEndDate;
	}
}
