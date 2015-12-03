package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;

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
	@Column(type=Types.CHAR, length = 500, columnDefinition = " ")
	private String desc;	//描述
	
	private ValueMappingGroup valueMappingGroup;
	
	@Column(type=Types.CHAR, length = 500, columnDefinition = " ")
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
