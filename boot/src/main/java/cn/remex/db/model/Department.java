package cn.remex.db.model;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;

import javax.persistence.Column;

@DataAccessScope(scope=DataAccessScope.everyone)
public class Department extends ModelableImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = -417181379899486760L;

	/**
	 * 部门编号
	 */
	@Column(length = 30, columnDefinition = " ")
	private String sn;

	/**
	 * 上级部门
	 */
	private Department supDepartment;

	/**
	 * 上级单位(法人)
	 */
	private Organization superordinate;
	public Department() {
	}
	public Department(final String name) {
		super(name);
	}

	/**
	 * 机构代码
	 * @return String
	 */
	@Element(label="机构代码")
	public String getSn() {
		return this.sn;
	}

	@Element(label="上级部门")
	public Department getSupDepartment() {
		return this.supDepartment;
	}

	@Element(label="上级单位")
	public Organization getSuperordinate() {
		return this.superordinate;
	}

	public void setSn(final String sn) {
		this.sn = sn;
	}

	public void setSupDepartment(final Department supDepartment) {
		this.supDepartment = supDepartment;
	}

	public void setSuperordinate(final Organization superordinate) {
		this.superordinate = superordinate;
	}


}
