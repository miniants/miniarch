package zbh.wx.appbeans.quotation;

import java.util.List;

public class QuotePriceRvo {
	private String insuCom;                 //投保公司
	private List<CoverageInfoRvo> atinCoverages;   //险种信息
	private String quoteNo;                 //询价单号
	private String benchmarkPremium;        //折前保费
	private String premium;                 //折后保费
	private String sumTravelTax;            //总车船税
	private String discount;            //折扣
	
	private String vciQuestion;				//商业险验证码问题
	private String tciQuestion;				//交强险验证码问题
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public List<CoverageInfoRvo> getAtinCoverages() {
		return atinCoverages;
	}
	public void setAtinCoverages(List<CoverageInfoRvo> atinCoverages) {
		this.atinCoverages = atinCoverages;
	}
	public String getQuoteNo() {
		return quoteNo;
	}
	public void setQuoteNo(String quoteNo) {
		this.quoteNo = quoteNo;
	}
	public String getBenchmarkPremium() {
		return benchmarkPremium;
	}
	public void setBenchmarkPremium(String benchmarkPremium) {
		this.benchmarkPremium = benchmarkPremium;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getSumTravelTax() {
		return sumTravelTax;
	}
	public void setSumTravelTax(String sumTravelTax) {
		this.sumTravelTax = sumTravelTax;
	}
	public String getVciQuestion() {
		return vciQuestion;
	}
	public void setVciQuestion(String vciQuestion) {
		this.vciQuestion = vciQuestion;
	}
	public String getTciQuestion() {
		return tciQuestion;
	}
	public void setTciQuestion(String tciQuestion) {
		this.tciQuestion = tciQuestion;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	
}
