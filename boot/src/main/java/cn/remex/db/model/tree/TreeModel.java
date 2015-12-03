/**
 * MenuTree.java
 * 2014-1-20
 * author:lihejia  lihejia@qqbx.com.cn
 */


package cn.remex.db.model.tree;

import cn.remex.db.rsql.model.ModelableImpl;

/**
 * @author lihejia
 *
 */
public class TreeModel extends ModelableImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3443815820046575110L;
	
	
	private String url;
	private String nodeDesc;
	private String superNode;
	private String parentFlag;
	private String order;
	private String icon;
	private String iconSrc;
	



	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the nodeDesc
	 */
	public String getNodeDesc() {
		return nodeDesc;
	}
	/**
	 * @param nodeDesc the nodeDesc to set
	 */
	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}
	/**
	 * @return the superNode
	 */
	public String getSuperNode() {
		return superNode;
	}
	/**
	 * @param superNode the superNode to set
	 */
	public void setSuperNode(String superNode) {
		this.superNode = superNode;
	}



	/**
	 * @return the parentFlag
	 */
	public String getParentFlag() {
		return parentFlag;
	}
	/**
	 * @param parentFlag the parentFlag to set
	 */
	public void setParentFlag(String parentFlag) {
		this.parentFlag = parentFlag;
	}
	/**
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the iconSrc
	 */
	public String getIconSrc() {
		return iconSrc;
	}
	/**
	 * @param iconSrc the iconSrc to set
	 */
	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}

	
	
}
