package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "valueMappingGroup","sourceCode","targetCode" }
		)
})
public class ValueMapping extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1001522598621838002L;
	private String sourceCode;	//源对象字段名
	private String targetCode;	//
	@Column(length = 500)
	private String desc;	//描述
	
	private ValueMappingGroup valueMappingGroup;
	
	@Column(length = 500)
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ValueMappingGroup getValueMappingGroup() {
		return valueMappingGroup;
	}
	public void setValueMappingGroup(ValueMappingGroup valueMappingGroup) {
		this.valueMappingGroup = valueMappingGroup;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
