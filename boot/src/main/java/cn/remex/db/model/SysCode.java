package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;

@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = { "codeType","code" }
		)
})
public class SysCode extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5450844183308334612L;
	@Column(type=Types.CHAR, length = 50, columnDefinition = " ")
	private String codeType;
	@Column(type=Types.CHAR,length=100, columnDefinition = " ")
	private String codeTypeDesc;
	@Column(type=Types.CHAR,length=10, columnDefinition = " ")
	private String code;
	@Column(type=Types.CHAR,length=50, columnDefinition = " ")
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
