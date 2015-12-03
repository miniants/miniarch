package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;


@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "environment","mapClass","mapType","targetClass","sourceClass" }
		)
})
public class ReflectMapping extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8026981537003719572L;
	private String environment;	//环境
	private String mapClass;		//映射种类
	private String mapType;	
	//映射类型
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String targetClass;	//目标对象
	@Column(type=Types.CHAR, length = 200, columnDefinition = " ")
	private String sourceClass;	//源对象
	private FieldMappingGroup fieldMappingGroup;
	@Column(type=Types.CHAR, length = 500, columnDefinition = " ")
	private String desc;	//描述

	private String parentTargetClass;	//目标对象
	private String parentSourceClass;	//源对象
	
	@Column(type=Types.CHAR, length = 500, columnDefinition = " ")
	private String name;
	
	private String note;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDesc() {
		return desc;
	}
	public String getEnvironment() {
		return environment;
	}
	public FieldMappingGroup getFieldMappingGroup() {
		return fieldMappingGroup;
	}
	public String getMapClass() {
		return mapClass;
	}
	public String getMapType() {
		return mapType;
	}
	
	public String getParentSourceClass() {
		return parentSourceClass;
	}
	public String getParentTargetClass() {
		return parentTargetClass;
	}
	public String getSourceClass() {
		return sourceClass;
	}
	public String getTargetClass() {
		return targetClass;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public void setFieldMappingGroup(FieldMappingGroup fieldMappingGroup) {
		this.fieldMappingGroup = fieldMappingGroup;
	}
	public void setMapClass(String mapClass) {
		this.mapClass = mapClass;
	}
	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
	public void setParentSourceClass(String parentSourceClass) {
		this.parentSourceClass = parentSourceClass;
	}
	public void setParentTargetClass(String parentTargetClass) {
		this.parentTargetClass = parentTargetClass;
	}
	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
}
