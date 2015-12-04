package zbh.aias.model.member;

import cn.remex.db.rsql.model.ModelableImpl;
import zbh.aias.model.order.ProductOrder;
import zbh.aias.model.quotation.AtinQuotation;

import javax.persistence.*;
import java.util.List;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"username"}
				)
})
public class Member  extends ModelableImpl{
	private static final long serialVersionUID = 5951009460667186069L;
	@OneToMany(mappedBy="member", cascade={CascadeType.PERSIST})
	private List<ProductOrder> productOrders; //车险订单
//	@OneToMany(mappedBy="member", cascade={CascadeType.PERSIST})
//	private List<CustomerVehicle> customerVehicles; //客户车辆
	@OneToMany(mappedBy="member", cascade={CascadeType.PERSIST})
	private List<AtinQuotation> atinQuotations; //我的询价单
//	@OneToMany(mappedBy="member", cascade={CascadeType.PERSIST})
//	private List< DeliveryInfo> deliverInfos; //递送信息
	
	//注册推广信息
	@Column(length = 20)
	private String memberType;//会员类型，Ordinary-普通会员，Business-商务会员
	@Column(length = 20)
	private String level;//级别，Small-小安，Big-大安，Supe-超级安r
	private Member supLine;//上线
	@OneToMany(mappedBy="supLine", cascade={CascadeType.PERSIST})
	private List<Member> subLines;//下线
	@OneToMany(mappedBy="member", cascade={CascadeType.PERSIST})
//	private List<Integral> integral;//积分
//	private InvitationCode invitationCode;//邀请码表
	@Column(length = 20)
	private String registerType;//注册方式
	@Column(length = 100)
	private String invitedCode;//被推广注册的key
	
	
	@Column(length = 30)
	private String username; //用户名
	@Column(length = 50)
	private String wxCode; //微信号
	@Column(length = 30)
	private String nickname; //昵称
	@Column(length = 50)
	private String realname; //真实名称
	@Column(length = 400)
	private String password; //密码
	@Column(length = 20)
	private String idNo; //身份证号
	@Column(length = 20)
	private String mobile; //手机
	@Column(length = 200)
	private String address; //地址
	@Column(length = 20)
	private String province; //省份
	@Column(length = 30)
	private String city; //城市
	@Column(length = 35)
	private String email; //邮箱
	@Column(length = 60)
	private String ownedInsuComName; //保险公司
	@Column(length = 5)
	private String entirePeriod;//从业年限
	private int beanCount;
	private int coinCount;
	private int customerCount;//客户数量
	private double assets; //资产
	@Column(length = 50)
	private String agntCertificateNo; //代理人资格证号
	public List<ProductOrder> getProductOrders() {
		return productOrders;
	}
	public void setProductOrders(List<ProductOrder> productOrders) {
		this.productOrders = productOrders;
	}
//	public List<CustomerVehicle> getCustomerVehicles() {
//		return customerVehicles;
//	}
//	public void setCustomerVehicles(List<CustomerVehicle> customerVehicles) {
//		this.customerVehicles = customerVehicles;
//	}
//	public List<AtinQuotation> getAtinQuotations() {
//		return atinQuotations;
//	}
	public void setAtinQuotations(List<AtinQuotation> atinQuotations) {
		this.atinQuotations = atinQuotations;
	}
//	public List<DeliveryInfo> getDeliverInfos() {
//		return deliverInfos;
//	}
//	public void setDeliverInfos(List<DeliveryInfo> deliverInfos) {
//		this.deliverInfos = deliverInfos;
//	}
//	public String getUsername() {
//		return username;
//	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOwnedInsuComName() {
		return ownedInsuComName;
	}
	public void setOwnedInsuComName(String ownedInsuComName) {
		this.ownedInsuComName = ownedInsuComName;
	}
	public int getBeanCount() {
		return beanCount;
	}
	public void setBeanCount(int beanCount) {
		this.beanCount = beanCount;
	}
	public int getCoinCount() {
		return coinCount;
	}
	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}
	public double getAssets() {
		return assets;
	}
	public void setAssets(double assets) {
		this.assets = assets;
	}
	public String getAgntCertificateNo() {
		return agntCertificateNo;
	}
	public void setAgntCertificateNo(String agntCertificateNo) {
		this.agntCertificateNo = agntCertificateNo;
	}
	public String getEntirePeriod() {
		return entirePeriod;
	}
	public void setEntirePeriod(String entirePeriod) {
		this.entirePeriod = entirePeriod;
	}
	public int getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public Member getSupLine() {
		return supLine;
	}
	public void setSupLine(Member supLine) {
		this.supLine = supLine;
	}
	public List<Member> getSubLines() {
		return subLines;
	}
	public void setSubLines(List<Member> subLines) {
		this.subLines = subLines;
	}
//	public List<Integral> getIntegral() {
//		return integral;
//	}
//	public void setIntegral(List<Integral> integral) {
//		this.integral = integral;
//	}
//	public InvitationCode getInvitationCode() {
//		return invitationCode;
//	}
//	public void setInvitationCode(InvitationCode invitationCode) {
//		this.invitationCode = invitationCode;
//	}
//	public String getRegisterType() {
//		return registerType;
//	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public String getInvitedCode() {
		return invitedCode;
	}
	public void setInvitedCode(String invitedCode) {
		this.invitedCode = invitedCode;
	}
	public String getWxCode() {
		return wxCode;
	}
	public void setWxCode(String wxCode) {
		this.wxCode = wxCode;
	}
	
}
