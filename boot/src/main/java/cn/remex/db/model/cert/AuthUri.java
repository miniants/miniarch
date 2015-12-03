package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.model.DataDic;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.sql.Column;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;

import javax.persistence.*;
import java.sql.Types;
import java.util.List;

@DataAccessScope(scope=DataAccessScope.everyone)
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"uri"}
				)
})
public class AuthUri extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2661818466025261442L;
	@Column(type=Types.CHAR, length = 100, columnDefinition = " ")
	private String uri;
	@Column(type=Types.CHAR, length = 100, columnDefinition = " ")
	private String uriName;
	@Column(type=Types.CHAR, length = 100, columnDefinition = " ")
	private String uriDesc;
	@Element(edittype=EditType.select,editoptions="{value:{forbidden:'禁止',permit:'允许',uncertainty:'待验证'}}")
	@Column(type=Types.CHAR, length = 20, columnDefinition = " ")
	private String verifyFlag;
	@OneToMany(mappedBy="authUri",cascade={CascadeType.PERSIST})
	private List<AuthParam> authParams;
	private DataDic group;
	@ManyToMany(mappedBy="authUris")
	private List<AuthRole> roles;
	public String getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	public List<AuthParam> getAuthParams() {
		return authParams;
	}
	public void setAuthParams(List<AuthParam> authParams) {
		this.authParams = authParams;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUriName() {
		return uriName;
	}
	public void setUriName(String uriName) {
		this.uriName = uriName;
	}
	public String getUriDesc() {
		return uriDesc;
	}
	public void setUriDesc(String uriDesc) {
		this.uriDesc = uriDesc;
	}
	public DataDic getGroup() {
		return group;
	}
	public void setGroup(DataDic group) {
		this.group = group;
	}
	public List<AuthRole> getRoles() {
		return roles;
	}
	public void setRoles(List<AuthRole> roles) {
		this.roles = roles;
	}
	
}
