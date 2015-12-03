package anbox.aibc.model.order;

import java.sql.Types;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import anbox.aibc.model.CommModel;
import anbox.aibc.model.member.Member;
import cn.remex.db.sql.SqlTypeAnnotation;

public class ProductOrder  extends CommModel{
	private static final long serialVersionUID = -9222602180164812662L;
	@SqlTypeAnnotation(type=Types.CHAR, length = 35, sqlType = " ")
	private String orderNo;//订单号
	
	private Member member;//所属会员
	
	@OneToMany(mappedBy="productOrder", cascade={CascadeType.PERSIST})
	private List< ProductOrderDetail> orderDetails;//订单明细
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String insuCom;//行销渠道
	private double orderPremium;//订单总费用
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String orderStatus;//订单状态
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String transChannel;//行销渠道
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String payType;//支付方式
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String orderPayNo;
	@SqlTypeAnnotation(type=Types.CHAR, length = 30, sqlType = " ")
	private String supplierCode;//供应商代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String supplierName;//供应商名称
	private double detailQuantity;//订单明细数量
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String payOrderId;//付费订单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String bizOrderId;//业务订单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String chargeComCode;//收费商代码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String chargeComName;//收费商名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 500, sqlType = " ")
	private String chargeComUrl;//收费商链接	
	
	//客户信息（投保人、被保人、车主）
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String applicantName;//投保人姓名
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String applicantId;//投保人id
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String applicantSex;//投保人性别
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String applicantMobile;//投保人手机
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String applicantAddress;//投保人地址
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String applicantCertType;//投保人证件类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String applicantCertNo;//投保人证件号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String insuredName;//被保人姓名
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String insuredId;//被保人id
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String insuredSex;//被保人性别
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String insuredMobile;//被保人手机
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String insuredAddress;//被保人地址
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String insuredCertType;//被保人证件类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String insuredCertNo;//被保人证件号码
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String ownerName;//车主姓名
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String ownerId;//车主id
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String ownerSex;//车主性别
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String ownerMobil;//车主手机
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String ownerAddress;//车主地址
	@SqlTypeAnnotation(type=Types.CHAR, length = 10, sqlType = " ")
	private String ownerCertType;//车主证件类型
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String ownerCertNos;//车主证件号码
	
	//会员信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String userId;//用户id
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String realname;//真实姓名
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String nickname;//昵称
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String idNo;//身份证号
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String userMobile;//用户手机
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String userPhone;//用户电话
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String agntCertificat;//代理人资格证号
	
	//寄送信息
	@SqlTypeAnnotation(type=Types.CHAR, length = 40, sqlType = " ")
	private String mailNo;//运单号
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String expressCom;//递送公司编号
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String expressComName ;//递送公司名称
	@SqlTypeAnnotation(type=Types.CHAR, length = 50, sqlType = " ")
	private String postContact;//联系人
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String postAddress;//寄送地址
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postMobile;//寄送联系人电话
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postProvince;//寄送省份
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String postCity;//寄送城市
	
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public List<ProductOrderDetail> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<ProductOrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public double getOrderPremium() {
		return orderPremium;
	}
	public void setOrderPremium(double orderPremium) {
		this.orderPremium = orderPremium;
	}
	public String getTransChannel() {
		return transChannel;
	}
	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOrderPayNo() {
		return orderPayNo;
	}
	public void setOrderPayNo(String orderPayNo) {
		this.orderPayNo = orderPayNo;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public double getDetailQuantity() {
		return detailQuantity;
	}
	public void setDetailQuantity(double detailQuantity) {
		this.detailQuantity = detailQuantity;
	}
	public String getPayOrderId() {
		return payOrderId;
	}
	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	public String getBizOrderId() {
		return bizOrderId;
	}
	public void setBizOrderId(String bizOrderId) {
		this.bizOrderId = bizOrderId;
	}
	public String getChargeComCode() {
		return chargeComCode;
	}
	public void setChargeComCode(String chargeComCode) {
		this.chargeComCode = chargeComCode;
	}
	public String getChargeComName() {
		return chargeComName;
	}
	public void setChargeComName(String chargeComName) {
		this.chargeComName = chargeComName;
	}
	public String getChargeComUrl() {
		return chargeComUrl;
	}
	public void setChargeComUrl(String chargeComUrl) {
		this.chargeComUrl = chargeComUrl;
	}
	public String getApplicantName() {
		return applicantName;
	}
	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}
	public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getApplicantSex() {
		return applicantSex;
	}
	public void setApplicantSex(String applicantSex) {
		this.applicantSex = applicantSex;
	}
	public String getApplicantMobile() {
		return applicantMobile;
	}
	public void setApplicantMobile(String applicantMobile) {
		this.applicantMobile = applicantMobile;
	}
	public String getApplicantAddress() {
		return applicantAddress;
	}
	public void setApplicantAddress(String applicantAddress) {
		this.applicantAddress = applicantAddress;
	}
	public String getApplicantCertType() {
		return applicantCertType;
	}
	public void setApplicantCertType(String applicantCertType) {
		this.applicantCertType = applicantCertType;
	}
	public String getApplicantCertNo() {
		return applicantCertNo;
	}
	public void setApplicantCertNo(String applicantCertNo) {
		this.applicantCertNo = applicantCertNo;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getInsuredId() {
		return insuredId;
	}
	public void setInsuredId(String insuredId) {
		this.insuredId = insuredId;
	}
	public String getInsuredSex() {
		return insuredSex;
	}
	public void setInsuredSex(String insuredSex) {
		this.insuredSex = insuredSex;
	}
	public String getInsuredMobile() {
		return insuredMobile;
	}
	public void setInsuredMobile(String insuredMobile) {
		this.insuredMobile = insuredMobile;
	}
	public String getInsuredAddress() {
		return insuredAddress;
	}
	public void setInsuredAddress(String insuredAddress) {
		this.insuredAddress = insuredAddress;
	}
	public String getInsuredCertType() {
		return insuredCertType;
	}
	public void setInsuredCertType(String insuredCertType) {
		this.insuredCertType = insuredCertType;
	}
	public String getInsuredCertNo() {
		return insuredCertNo;
	}
	public void setInsuredCertNo(String insuredCertNo) {
		this.insuredCertNo = insuredCertNo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerSex() {
		return ownerSex;
	}
	public void setOwnerSex(String ownerSex) {
		this.ownerSex = ownerSex;
	}
	public String getOwnerMobil() {
		return ownerMobil;
	}
	public void setOwnerMobil(String ownerMobil) {
		this.ownerMobil = ownerMobil;
	}
	public String getOwnerAddress() {
		return ownerAddress;
	}
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	public String getOwnerCertType() {
		return ownerCertType;
	}
	public void setOwnerCertType(String ownerCertType) {
		this.ownerCertType = ownerCertType;
	}
	public String getOwnerCertNos() {
		return ownerCertNos;
	}
	public void setOwnerCertNos(String ownerCertNos) {
		this.ownerCertNos = ownerCertNos;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getAgntCertificat() {
		return agntCertificat;
	}
	public void setAgntCertificat(String agntCertificat) {
		this.agntCertificat = agntCertificat;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getExpressCom() {
		return expressCom;
	}
	public void setExpressCom(String expressCom) {
		this.expressCom = expressCom;
	}
	public String getExpressComName() {
		return expressComName;
	}
	public void setExpressComName(String expressComName) {
		this.expressComName = expressComName;
	}
	public String getPostContact() {
		return postContact;
	}
	public void setPostContact(String postContact) {
		this.postContact = postContact;
	}
	public String getPostAddress() {
		return postAddress;
	}
	public void setPostAddress(String postAddress) {
		this.postAddress = postAddress;
	}
	public String getPostMobile() {
		return postMobile;
	}
	public void setPostMobile(String postMobile) {
		this.postMobile = postMobile;
	}
	public String getPostProvince() {
		return postProvince;
	}
	public void setPostProvince(String postProvince) {
		this.postProvince = postProvince;
	}
	public String getPostCity() {
		return postCity;
	}
	public void setPostCity(String postCity) {
		this.postCity = postCity;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
