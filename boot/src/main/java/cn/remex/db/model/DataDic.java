package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"dataType","dataCode"}
				)
})
public class DataDic extends ModelableImpl{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1090462642025409610L;
	@Column(length = 30, columnDefinition = " ")
	private String dataType;
	@Column(length = 50, columnDefinition = " ")
	private String TypeDesc;
	@Column(length = 30, columnDefinition = " ")
	private String dataCode;
	@Column(length = 100, columnDefinition = " ")
	private String dataDesc;
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getTypeDesc() {
		return TypeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		TypeDesc = typeDesc;
	}
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	public String getDataDesc() {
		return dataDesc;
	}
	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
}
