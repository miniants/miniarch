package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.*;
import java.util.List;

@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class ValueMappingGroup extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1369595788982316585L;
	@Column(length = 500)
	private String desc; // 描述

	private String mapType; // 映射类型

	private ValueMappingGroup parentGroup;

	@OneToMany(mappedBy = "valueMappingGroup",cascade={CascadeType.PERSIST})
	private List<ValueMapping> valueMappings;

	private ValueMappingConfig valueMappingConfig;

	@Column(length = 200)
	private String name;

	public ValueMappingConfig getValueMappingConfig() {
		return valueMappingConfig;
	}

	public void setValueMappingConfig(ValueMappingConfig valueMappingConfig) {
		this.valueMappingConfig = valueMappingConfig;
	}

	public String getDesc() {
		return desc;
	}

	public String getMapType() {
		return mapType;
	}

	public ValueMappingGroup getParentGroup() {
		return parentGroup;
	}

	public List<ValueMapping> getValueMappings() {
		return valueMappings;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public void setParentGroup(ValueMappingGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	public void setValueMappings(List<ValueMapping> valueMappings) {
		this.valueMappings = valueMappings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
