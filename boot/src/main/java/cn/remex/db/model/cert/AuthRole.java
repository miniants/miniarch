package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.model.SysUri;
import cn.remex.db.model.SysMenu;
import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;
@DataAccessScope(scope=DataAccessScope.everyone)
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"name"}
				)
})
public class AuthRole  extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4219376074850276724L;
	@ManyToMany(mappedBy="roles",targetEntity=SysUri.class)
	private List<SysUri> sysUris;
	@ManyToMany(mappedBy="roles",targetEntity=SysMenu.class)
	private List<SysMenu> menus;
	@ManyToMany(mappedBy="roles")
	private List<AuthUser> users;
	/**
	 * 允许访问的数据
	 * 根据组织结构中的部门来决定。
	 */
	public AuthRole(final String name) {
		super(name);
	}
	public AuthRole() {}
	public List<SysUri> getSysUris() {
		return sysUris;
	}
	public void setSysUris(List<SysUri> sysUris) {
		this.sysUris = sysUris;
	}
	public List<SysMenu> getMenus() {
		return menus;
	}
	public void setMenus(List<SysMenu> menus) {
		this.menus = menus;
	}
	public List<AuthUser> getUsers() {
		return users;
	}
	public void setUsers(List<AuthUser> users) {
		this.users = users;
	}
}
