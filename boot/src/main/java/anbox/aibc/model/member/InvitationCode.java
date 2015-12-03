package anbox.aibc.model.member;

import java.sql.Types;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.SqlTypeAnnotation;
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"invitationCode"}
				)
})
public class InvitationCode   extends ModelableImpl{
	private static final long serialVersionUID = -8386156746338480113L;
	private Member member;//所属会员
	@SqlTypeAnnotation(type=Types.CHAR, length = 100, sqlType = " ")
	private String invitationCode;//邀请码
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String generationTime;//生成时间
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String invalidTime;//失效时间
//	@SqlTypeAnnotation(type=Types.CHAR, length = 1, sqlType = " ")
	private int validityPeriod;//有效期
	@SqlTypeAnnotation(type=Types.CHAR, length = 20, sqlType = " ")
	private String status;//状态
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	
}
