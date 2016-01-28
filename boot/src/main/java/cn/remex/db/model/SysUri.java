package cn.remex.db.model;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.model.DataDic;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.EditType;
import cn.remex.db.view.Element;

import javax.persistence.*;
import java.util.List;

@DataAccessScope(scope=DataAccessScope.everyone)
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"uri"}
				)
})
public class SysUri extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2661818466025261442L;
	@Column(length = 100)
	private String uri;
	@Column(length = 100)
	private String uriName;
	@Column(length = 100)
	private String uriDesc;
	@ManyToMany(mappedBy="sysUris")
	private List<AuthRole> roles;
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
	public List<AuthRole> getRoles() {
		return roles;
	}
	public void setRoles(List<AuthRole> roles) {
		this.roles = roles;
	}
	
}
