package zbh.aias.model.member;

import cn.remex.db.rsql.model.ModelableImpl;
import javax.persistence.Column;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Types;
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"invitationCode"}
				)
})
public class InvitationCode   extends ModelableImpl{
	private static final long serialVersionUID = -8386156746338480113L;
	private Member member;//所属会员
	@Column(length = 100)
	private String invitationCode;//邀请码
	@Column(length = 20)
	private String generationTime;//生成时间
	@Column(length = 20)
	private String invalidTime;//失效时间
//	@Column(length = 1)
	private int validityPeriod;//有效期
	@Column(length = 20)
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
