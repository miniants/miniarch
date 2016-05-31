package cn.remex.wechat.models;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/5/25 0025.
 */
public class WeChatUser extends ModelableImpl {
	@Column(length = 4)
	private String subscribe;
	@Column(length = 32)
	private String openid;
	@Column(length = 128)
	private String nickname;
	@Column(length = 4)
	private String sex;
	@Column(length = 16)
	private String language;
	@Column(length = 16)
	private String city;
	@Column(length = 16)
	private String province;
	@Column(length = 16)
	private String country;
	@Column(length = 256)
	private String headimgurl;
	@Column(length = 10)
	private long subscribeTime;
	@Column(length = 32)
	private String unionid;
	@Column(length = 256)
	private String remark;
	@Column(length = 256)
	private String tagidList;

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

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public long getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(long subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getTagidList() {
		return tagidList;
	}

	public void setTagidList(String tagidList) {
		this.tagidList = tagidList;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
}
