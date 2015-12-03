package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "environment","confClass","confKind","confType","group", "key","value"}
		)
})
public class ConfigInfo extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4986751129808284721L;
	@Column(type=Types.CHAR, length = 10, columnDefinition = " ")
	private String environment;
	@Column(type=Types.CHAR, length = 25, columnDefinition = " ")
	private String confClass;
	@Column(type=Types.CHAR, length = 25, columnDefinition = " ")
	private String confKind;
	@Column(type=Types.CHAR, length = 25, columnDefinition = " ")
	private String confType;
	@Column(type=Types.CHAR, length = 10, columnDefinition = " ")
	private String group;
	@Column(type=Types.CHAR, length = 30, columnDefinition = " ")
	private String key;
	@Column(type=Types.CHAR, length = 120, columnDefinition = " ")
	private String value;
	@Column(type=Types.CHAR, length = 300, columnDefinition = " ")
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
