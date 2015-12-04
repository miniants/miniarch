package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "fieldMappingGroup","sourceField","targetField" }
		)
})
public class FieldMapping extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6843228998756569768L;
	
	private String sourceField;	//源对象字段名
	
	private String targetField;	//
	@Column(length = 500, columnDefinition = " ")
	private String desc;	//描述
	@Column(length = 500, columnDefinition = " ")
	private String name;
	
	private FieldMappingGroup fieldMappingGroup;//n:1所属的配置组
	
	private ValueMappingConfig valueMappingConfig;//所拥有的代码映射组
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ValueMappingConfig getValueMappingConfig() {
		return valueMappingConfig;
	}
	public void setValueMappingConfig(ValueMappingConfig valueMappingConfig) {
		this.valueMappingConfig = valueMappingConfig;
	}
	public FieldMappingGroup getFieldMappingGroup() {
		return fieldMappingGroup;
	}
	public void setFieldMappingGroup(FieldMappingGroup fieldMappingGroup) {
		this.fieldMappingGroup = fieldMappingGroup;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	public String getTargetField() {
		return targetField;
	}
	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}
}
