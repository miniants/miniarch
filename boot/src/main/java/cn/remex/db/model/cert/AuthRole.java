package cn.remex.db.model.cert;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.model.tree.MenuTreeNode;
import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;

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
	@ManyToMany(mappedBy="roles",targetEntity=AuthUri.class)
	private List<AuthUri> authUris;
	@ManyToMany(mappedBy="roles",targetEntity=MenuTreeNode.class)
	private List<MenuTreeNode> menus;
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
	public List<AuthUri> getAuthUris() {
		return authUris;
	}
	public void setAuthUris(List<AuthUri> authUris) {
		this.authUris = authUris;
	}
	public List<MenuTreeNode> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuTreeNode> menus) {
		this.menus = menus;
	}
	public List<AuthUser> getUsers() {
		return users;
	}
	public void setUsers(List<AuthUser> users) {
		this.users = users;
	}
}
