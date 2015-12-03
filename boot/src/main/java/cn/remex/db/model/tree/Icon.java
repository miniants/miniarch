package cn.remex.db.model.tree;

import cn.remex.db.rsql.model.ModelableImpl;

public class Icon extends ModelableImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5643753502570504449L;
	private String iconSrc;
	private String iconName;
	private String iconDesc;
	public String getIconSrc() {
		return iconSrc;
	}
	public void setIconSrc(String iconSrc) {
		this.iconSrc = iconSrc;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getIconDesc() {
		return iconDesc;
	}
	public void setIconDesc(String iconDesc) {
		this.iconDesc = iconDesc;
	}
	
}
