package anbox.aibc.model.member;

import java.sql.Types;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"code"}
				)
})
public class PhoneCheckCode   extends ModelableImpl{
	private static final long serialVersionUID = -2452020592765888141L;
	private String phoneNo;//所属会员
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String code;//邀请码
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String generationTime;//生成时间
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String invalidTime;//失效时间
//	@SqlTypeAnnotation(type=Types.CHAR, length = 1, sqlType = " ")
	private int validityPeriod;//有效期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String status;//状态
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGenerationTime() {
		return generationTime;
	}
	public void setGenerationTime(String generationTime) {
		this.generationTime = generationTime;
	}
	public String getInvalidTime() {
		return invalidTime;
	}
	public void setInvalidTime(String invalidTime) {
		this.invalidTime = invalidTime;
	}
	public int getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
