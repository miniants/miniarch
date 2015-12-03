/**
 * 
 */
package cn.remex.db.model;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.OneToMany;
import java.util.List;

/** 
 * @author liuhengyang 
 * @date 2015-8-29 下午6:11:54
 * @version 版本号码
 * 
 * 本模型为系统菜单的模型
 * 
 */
public class SysMenu extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2851287093826738709L;
	
	//关系结构
	private SysMenu parent; // 菜单
	@OneToMany(mappedBy="parent")
	private List<SysMenu> subMenus;
	
	//基本信息
	private String nodeType;		//节点类型，默认为点击触发url事件
	private String nodeUri;			//节点url
	private String nodeName;		//节点名称
	private String nodeDesc;		//节点描述
	private String nodeOrder;		//顺序编号
	private String level;			//层级

	private boolean parentFlag;		//标识自己是否是父节点标志
	private boolean halfCheck;		//标识自己是否半选状态
	private boolean isHidden;		//是否隐藏
	private boolean checked;		//是否选中
	private String icon;			//图标
	private String iconClose;		//折叠图标
	private String iconOpen;		//展开图标
	private boolean opened;		//默认是否展开
	private boolean chkDisabled;//默认是否禁止选中状态
	
	
	
	public SysMenu getParent() {
		return parent;
	}
	public void setParent(SysMenu parent) {
		this.parent = parent;
	}
	
	public List<SysMenu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<SysMenu> subMenus) {
		this.subMenus = subMenus;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
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
	public boolean isParentFlag() {
		return parentFlag;
	}
	public void setParentFlag(boolean parentFlag) {
		this.parentFlag = parentFlag;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIconClose() {
		return iconClose;
	}
	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}
	public String getIconOpen() {
		return iconOpen;
	}
	public void setIconOpen(String iconOpen) {
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
