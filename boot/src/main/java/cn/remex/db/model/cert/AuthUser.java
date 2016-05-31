package cn.remex.db.model.cert;

import cn.remex.RemexConstants.UserType;
import cn.remex.db.model.SysUri;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.struts2.json.annotations.JSON;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = {"username","password"})
})
public class AuthUser extends ModelableImpl{
	/**
	 *
	 */
	private static final long serialVersionUID = -3851128250538182731L;
	/**登陆名*/
	@Element(label="用户名")
	@Column(length = 20)
	private String username;
	/** 登陆密码*/
	@Element(label="密码")
	@Column(length = 30)
	private String password;
	/** 所属角色*/
	@Element(label="所属角色")
	@ManyToMany(mappedBy="users",targetEntity=AuthRole.class)
	private List<AuthRole> roles;
	private Map<String, SysUri> uriAuthMap;
	private String effectFlag;		//用户是否有效
	private String effectPeriod;			//用户有效期
	private UserType userType;//用户类型，目前分为：SYSTEM / ADMIN / B_USER / C_USER

	/**
	 * @date 2016-2-29
	 * @author guoqianyou
	 * 微信用户字段
	 */

	@Column(length = 4)
	private String subscribe;
	@Column(length = 32)
	private String openid; //	用户的标识，对当前公众号唯一
	@Column(length = 128)
	private String nickname; //	用户的昵称
	@Column(length = 4)
	private String realname; //	用户的真实姓名
	private String sex; //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	@Column(length = 16)
	private String city; //	用户所在城市
	@Column(length = 16)
	private String country; //用户所在国家
	@Column(length = 16)
	private String province; //用户所在省份
	@Column(length = 16)
	private String language; //用户的语言，简体中文为zh_CN
	@Column(length = 512)
	private String headimgurl; //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	@Column(length = 10)
	private String subscribeTime; //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	@Column(length = 32)
	private String unionid; //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
	@Column(length = 256)
	private String remark; //	公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	@Column(length = 256)
	private String groupid; //用户所在的分组ID
	@Column(length = 256)
	private String tagidList;
	//我们扩展微信的字段
	@Column(length = 10)
	private long weixinLastTime;//最后一次访问微信的时间，包含消息，关注，取消关注，扫描二维码，菜单等。

	/**
	 *
	 */
	@Column(length = 11)
	private String mobile;//手机号
	@Column(length = 18)
	private String idNo;//身份证号

	public long getWeixinLastTime() {
		return weixinLastTime;
	}

	public void setWeixinLastTime(long weixinLastTime) {
		this.weixinLastTime = weixinLastTime;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getTagidList() {
		return tagidList;
	}

	public void setTagidList(String tagidList) {
		this.tagidList = tagidList;
	}

	public Map<String, SysUri> getUriAuthMap() {
		return uriAuthMap;
	}

	public void setUriAuthMap(Map<String, SysUri> uriAuthMap) {
		this.uriAuthMap = uriAuthMap;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public AuthUser() {
		super("undefined");
		uriAuthMap = new HashMap<String, SysUri>();
	}
	public AuthUser(final String name) {
		super(name);
		setName(name);
		setUsername(name);
		uriAuthMap = new HashMap<String, SysUri>();
	}
	public String getPassword() {
		return this.password;
	}
	public List<AuthRole> getRoles() {
		return this.roles;
	}
	public String getUsername() {
		return this.username;
	}
	public void setPassword(final String password) {
		this.password = password;
	}
	public void setRoles(final List<AuthRole> roles) {
		this.roles = roles;
	}
	public void setUsername(final String username) {
		this.username = username;
		super.setName(username);
	}
	public Map<String, SysUri> obtainUriAuthMap() {
		return uriAuthMap;
	}
	public void putUriAuthMap(Map<String, SysUri> uriAuthMap) {
		this.uriAuthMap = uriAuthMap;
	}
	public String getEffectFlag() {
		return effectFlag;
	}
	public void setEffectFlag(String effectFlag) {
		this.effectFlag = effectFlag;
	}
	public String getEffectPeriod() {
		return effectPeriod;
	}
	public void setEffectPeriod(String effectPeriod) {
		this.effectPeriod = effectPeriod;
	}

}
