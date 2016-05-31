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
})
public class FieldMappingGroup extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6843228998756569768L;
	@Column(length = 500)
	private String desc;	//描述
	
	private FieldMappingGroup parentGroup;
	
	@OneToMany(mappedBy="fieldMappingGroup")
	private List<FieldMapping> fieldMappings;

	
	@Column(length = 200)
	private String name;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FieldMappingGroup getParentGroup() {
		return parentGroup;
	}
	public void setParentGroup(FieldMappingGroup parentGroup) {
		this.parentGroup = parentGroup;
	}
	public List<FieldMapping> getFieldMappings() {
		return fieldMappings;
	}
	public void setFieldMappings(List<FieldMapping> fieldMappings) {
		this.fieldMappings = fieldMappings;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
