package cn.remex.db.model.tree;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.model.cert.AuthUri;
import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.*;
import java.util.List;
@DataAccessScope(scope=DataAccessScope.everyone)
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"nodeUri"}
				)
})
public class MenuTreeNode extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4688004225136038858L;
	private AuthUri authUri;
	private boolean parentFlag;			//父节点标志
	private MenuTreeNode supNode;
	@OneToMany(mappedBy="supNode")
	private List<MenuTreeNode> subNodes;
	@Column(length = 100, columnDefinition = " ")
	private String nodeUri;
	@Column(length = 50, columnDefinition = " ")
	private String nodeDesc;
	@Column(length = 5, columnDefinition = " ")
	private String nodeOrder;
	@ManyToMany(mappedBy="menus")
	private List<AuthRole> roles;
	private boolean halfCheck;
	private boolean isHidden;
	private boolean checked;		//
	private Icon icon;			//图标
	private Icon iconClose;		//折叠图标
	private Icon iconOpen;		//展开图标
	private boolean opened;
	private boolean chkDisabled;
	@Column(length = 50, columnDefinition = " ")
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public AuthUri getAuthUri() {
		return authUri;
	}
	public void setAuthUri(AuthUri authUri) {
		this.authUri = authUri;
	}
	public boolean isParentFlag() {
		return parentFlag;
	}
	public void setParentFlag(boolean parentFlag) {
		this.parentFlag = parentFlag;
	}
	public MenuTreeNode getSupNode() {
		return supNode;
	}
	public void setSupNode(MenuTreeNode supNode) {
		this.supNode = supNode;
	}
	public List<MenuTreeNode> getSubNodes() {
		return subNodes;
	}
	public void setSubNodes(List<MenuTreeNode> subNodes) {
		this.subNodes = subNodes;
	}
	public String getNodeUri() {
		return nodeUri;
	}
	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}
	public String getNodeDesc() {
		return nodeDesc;
	}
	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}
	public String getNodeOrder() {
		return nodeOrder;
	}
	public void setNodeOrder(String nodeOrder) {
		this.nodeOrder = nodeOrder;
	}
	public List<AuthRole> getRoles() {
		return roles;
	}
	public void setRoles(List<AuthRole> roles) {
		this.roles = roles;
	}
	public boolean isHalfCheck() {
		return halfCheck;
	}
	public void setHalfCheck(boolean halfCheck) {
		this.halfCheck = halfCheck;
	}
	public boolean isHidden() {
		return isHidden;
	}
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	public Icon getIconClose() {
		return iconClose;
	}
	public void setIconClose(Icon iconClose) {
		this.iconClose = iconClose;
	}
	public Icon getIconOpen() {
		return iconOpen;
	}
	public void setIconOpen(Icon iconOpen) {
		this.iconOpen = iconOpen;
	}
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public boolean isChkDisabled() {
		return chkDisabled;
	}
	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	
}
