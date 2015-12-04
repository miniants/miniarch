package cn.remex.db.model;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;

import javax.persistence.Column;

@DataAccessScope(scope=DataAccessScope.everyone)
public class Organization extends ModelableImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7458430676822180715L;

	/**
	 * 单位地址
	 */
	@Column(length = 200, columnDefinition = " ")
	private String address;

	/**
	 * 经营范围或者工作内
	 */
	@Column(length = 200, columnDefinition = " ")
	private String business;

	/**
	 * 法人代表
	 */
	@Column(length = 30, columnDefinition = " ")
	private String legalPerson;
	/**
	 * 组织机构代码，或者内部的单位编码
	 */
	@Column(length = 50, columnDefinition = " ")
	private String organizationSn;

	/**
	 * 上级单位
	 */
	private Organization superordinate;






	public Organization() {
	}

	/***************************************************/
	@Element(label="注册地址")
	public String getAddress() {
		return this.address;
	}

	@Element(label="经营范围")
	public String getBusiness() {
		return this.business;
	}

	@Element(label="法人",hidden=true)
	public String getLegalPerson() {
		return this.legalPerson;
	}

	@Element(label="组织机构代码")
	public String getOrganizationSn() {
		return this.organizationSn;
	}

	public Organization getSuperordinate() {
		return this.superordinate;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setBusiness(final String business) {
		this.business = business;
	}

	public void setLegalPerson(final String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public void setOrganizationSn(final String organizationSn) {
		this.organizationSn = organizationSn;
	}

	public void setSuperordinate(final Organization superordinate) {
		this.superordinate = superordinate;
	}



}
