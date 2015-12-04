//package cn.com.qqbx.wx.appbean;
//
//import java.io.Serializable;
//import java.sql.Types;
//import java.util.List;
//
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnAddedService;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnAfterService;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnCoverage;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnCustomer;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnDriver;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnEngaged;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnEquipment;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnFeeRate;
//import zbh.com.qqbx.wx.model.wxquotation.AtinQttnTravelTax;
//import zbh.com.qqbx.wx.model.wxquotation.Quotation;
//import zbh.remex.db.sql.SqlTypeAnnotation;
//
///**
// * 车险询价单信息
// * @author guoshaopeng
// * @since 20130712
// */
//public class WxInsure implements Serializable{
//	
//	private String openId;   //询价单号
//	private String quoteNo;   //询价单号
//	private String insuCom;   //投保公司
//	private String cityCode;  //投保城市
//	private String vciQuestion;				//商业险验证码问题
//	private String vciAnswer;					//商业险验证码问题答案
//	private String tciQuestion;				//交强险验证码问题
//	private String tciAnswer;					//交强险验证码问题答案
//	public String getQuoteNo() {
//		return quoteNo;
//	}
//	public void setQuoteNo(String quoteNo) {
//		this.quoteNo = quoteNo;
//	}
//	public String getInsuCom() {
//		return insuCom;
//	}
//	public void setInsuCom(String insuCom) {
//		this.insuCom = insuCom;
//	}
//	public String getCityCode() {
//		return cityCode;
//	}
//	public void setCityCode(String cityCode) {
//		this.cityCode = cityCode;
//	}
//	public String getVciQuestion() {
//		return vciQuestion;
//	}
//	public void setVciQuestion(String vciQuestion) {
//		this.vciQuestion = vciQuestion;
//	}
//	public String getVciAnswer() {
//		return vciAnswer;
//	}
//	public void setVciAnswer(String vciAnswer) {
//		this.vciAnswer = vciAnswer;
//	}
//	public String getTciQuestion() {
//		return tciQuestion;
//	}
//	public void setTciQuestion(String tciQuestion) {
//		this.tciQuestion = tciQuestion;
//	}
//	public String getTciAnswer() {
//		return tciAnswer;
//	}
//	public void setTciAnswer(String tciAnswer) {
//		this.tciAnswer = tciAnswer;
//	}
//	public String getOpenId() {
//		return openId;
//	}
//	public void setOpenId(String openId) {
//		this.openId = openId;
//	}
//	
//}                                                                              
