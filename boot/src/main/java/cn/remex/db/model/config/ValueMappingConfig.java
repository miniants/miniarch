package cn.remex.db.model.config;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "name" }
		)
})public class ValueMappingConfig extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6740571069859243817L;
	@Column(length = 500, columnDefinition = " ")
	private String desc;	//描述
	
	private String mapType="default";		//映射类型
	
	private ValueMappingConfig parentGroup;


	@OneToMany(mappedBy="valueMappingConfig")
	private List<ValueMappingGroup> valueMappingGroups;
	
	
	@Column(length = 200, columnDefinition = " ")
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ValueMappingGroup> getValueMappingGroups() {
		return valueMappingGroups;
	}

	public void setValueMappingGroups(List<ValueMappingGroup> valueMappingGroups) {
		this.valueMappingGroups = valueMappingGroups;
	}

	public String getDesc() {
		return desc;
	}
	
	public String getMapType() {
		return mapType;
	}
	
	public ValueMappingConfig getParentGroup() {
		return parentGroup;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setMapType(String mapType) {
		this.mapType = mapType;
	}
	public void setParentGroup(ValueMappingConfig parentGroup) {
		this.parentGroup = parentGroup;
	}
}
