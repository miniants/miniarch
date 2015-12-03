package cn.remex.db.model.tree;

import cn.remex.db.cert.DataAccessScope;
import cn.remex.db.model.cert.AuthRole;
import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;
@DataAccessScope(scope=DataAccessScope.everyone)
@Table(uniqueConstraints={
		@UniqueConstraint(columnNames = {"nodeUri"}
				)
})
public class ZTreeNode extends ModelableImpl {
	private static final long serialVersionUID = 6022851843750266708L;
	private boolean parentFlag;			//父节点标志
	private ZTreeNode parentNode;
	private List<ZTreeNode> childrenNode;
	private String nodeUri;
	private String nodeName;
	private String nodeDesc;
	private String nodeOrder;
	private String level;
	private List<AuthRole> roles;
	private boolean halfCheck;
	private boolean isHidden;
	private boolean checked;		//
	private Icon icon;			//图标
	private Icon iconClose;		//折叠图标
	private Icon iconOpen;		//展开图标
	private boolean opened;
	private boolean chkDisabled;
	
	public ZTreeNode() {
		// TODO Auto-generated constructor stub
	}

	public ZTreeNode(final String name) {
		super(name);
	}

	public ZTreeNode(final String name, final boolean opened) {
		super(name);
		this.opened = opened;
	}
	public boolean isParentFlag() {
		return parentFlag;
	}
	public void setParentFlag(boolean parentFlag) {
		this.parentFlag = parentFlag;
	}
	public ZTreeNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(ZTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	public List<ZTreeNode> getChildrenNode() {
		return childrenNode;
	}
	public void setChildrenNode(List<ZTreeNode> childrenNode) {
		this.childrenNode = childrenNode;
	}
	public String getNodeUri() {
		return nodeUri;
	}
	public void setNodeUri(String nodeUri) {
		this.nodeUri = nodeUri;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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
	
//	private String iconSkin;		//图标皮肤
//	private boolean nocheck;
//	private String target;
//	private TabSrc tabSrc;
//	private String click;
//	public TabSrc getTabSrc() {
//		return tabSrc;
//	}
//	public void setTabSrc(TabSrc tabSrc) {
//		this.tabSrc = tabSrc;
//	}
//	@OneToMany(mappedBy = "nodeParent")
//	public List<ZTreeNode> getChildren() {
//		return this.children;
//	}
//
//	/**
//	 * @return the nodeParent
//	 */
//	@Element(label = "父节点")
//	@JSON(name = "pid")
//	@JSONField(name = "pid")
//	public ZTreeNode getNodeParent() {
//		return this.nodeParent;
//	}
//
//	/**
//	 * @return the tabSrc
//	 */
///*	@Element(label="tab页内容设置",width=250)
//	public String getTabSrc() {
//		return this.tabSrc;
//	}*/
//
//	public void setChildren(final List<ZTreeNode> children) {
//		this.children = children;
//	}
//
//	/**
//	 * @param nodeParent
//	 *            the nodeParent to set
//	 */
//	public void setNodeParent(final ZTreeNode nodeParent) {
//		this.nodeParent = nodeParent;
//	}
//	@Element(label = "图标")
//	public String getIcon() {
//		return this.icon;
//	}
//	@Element(label = "折叠图标")
//	public String getIconClose() {
//		return this.iconClose;
//	}
//	@Element(label = "展开图标")
//	public String getIconOpen() {
//		return this.iconOpen;
//	}
//	@Element(label = "图标皮肤")
//	public String getIconSkin() {
//		return this.iconSkin;
//	}
//	@Element(label = "超链接目标窗体")
//	public String getTarget() {
//		return this.target;
//	}
//	@Element(label = "超链接")
//	public String getUrl() {
//		return this.url;
//	}
//	public String getClick() {
//		return this.click;
//	}
//	@Element(label = "节点是否隐藏 checkbox / radio ")
//	public boolean isNocheck() {
//		return this.nocheck;
//	}
//	@Element(label = "节点的 checkbox / radio 是否禁用")
//	public boolean isChkDisabled() {
//		return this.chkDisabled;
//	}
//	@Element(label = "半勾选状态")
//	public boolean isHalfCheck() {
//		return this.halfCheck;
//	}
//	@Element(label = "节点是否被隐藏")
//	public boolean isHidden() {
//		return this.isHidden;
//	}
//	@Element(label = "是否父节点")
//	public boolean getParentFlag() {
//		return this.parentFlag;
//	}
//	@Element(label = "checked")
//	public boolean isChecked() {
//		return this.checked;
//	}
//	@JSON(name = "open")
//	@JSONField(name = "open")
//	@Element(label = "是否展开")
//	public boolean isOpened() {
//		return this.opened;
//	}
//	public void setClick(String click) {
//		this.click = click;
//	}
//	public void setParentFlag(final boolean parentFlag) {
//		this.parentFlag = parentFlag;
//	}
//	public void setChecked(final boolean checked) {
//		this.checked = checked;
//	}
//	public void setIcon(final String icon) {
//		this.icon = icon;
//	}
//	public void setIconClose(final String iconClose) {
//		this.iconClose = iconClose;
//	}
//	public void setIconOpen(final String iconOpen) {
//		this.iconOpen = iconOpen;
//	}
//	public void setIconSkin(final String iconSkin) {
//		this.iconSkin = iconSkin;
//	}
//	public void setOpened(final boolean open) {
//		this.opened = open;
//	}
//	public void setTarget(final String target) {
//		this.target = target;
//	}
//	public void setUrl(final String url) {
//		this.url = url;
//	}
//	public void setNocheck(boolean nocheck) {
//		this.nocheck = nocheck;
//	}
//	public void setHalfCheck(boolean halfCheck) {
//		this.halfCheck = halfCheck;
//	}
//	public void setHidden(boolean isHidden) {
//		this.isHidden = isHidden;
//	}

}
