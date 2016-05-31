package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "environment","confClass","confKind","confType","group", "key","value"}
		)
})
public class SysConfig extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4986751129808284721L;
	@Column(length = 10)
	private String environment;
	@Column(length = 25)
	private String confClass;
	@Column(length = 25)
	private String confKind;
	@Column(length = 25)
	private String confType;
	@Column(length = 10)
	private String group;
	@Column(length = 30)
	private String key;
	@Column(length = 120)
	private String value;
	@Column(length = 300)
	private String keyDesc;
	public String getConfKind() {
		return confKind;
	}
	public void setConfKind(String confKind) {
		this.confKind = confKind;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getConfClass() {
		return confClass;
	}
	public void setConfClass(String confClass) {
		this.confClass = confClass;
	}
	public String getConfType() {
		return confType;
	}
	public void setConfType(String confType) {
		this.confType = confType;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKeyDesc() {
		return keyDesc;
	}
	public void setKeyDesc(String keyDesc) {
		this.keyDesc = keyDesc;
	}
	
}
