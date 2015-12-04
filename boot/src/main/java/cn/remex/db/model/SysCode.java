package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "codeType","code" }
		)
})
public class SysCode extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5450844183308334612L;
	@Column(length = 50)
	private String codeType;
	@Column(length=100)
	private String codeTypeDesc;
	@Column(length=10)
	private String code;
	@Column(length=50)
	private String codeName;
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCodeTypeDesc() {
		return codeTypeDesc;
	}
	public void setCodeTypeDesc(String codeTypeDesc) {
		this.codeTypeDesc = codeTypeDesc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
}
