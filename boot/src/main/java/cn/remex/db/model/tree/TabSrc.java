package cn.remex.db.model.tree;

import cn.remex.db.rsql.model.ModelableImpl;
import cn.remex.db.view.Element;

public class TabSrc extends ModelableImpl {
 /**
	 * 
	 */
	private static final long serialVersionUID = 4713338785645886433L;
	private String url;
	private String fn;
	private TabSettings tabsettings;

	@Element(label = "目标窗口的路径")
	public String getUrl() {
		return this.url;
	}
	
	
	public String getFn() {
		return this.fn;
	}
	
	
	public TabSettings getTabsettings() {
		return this.tabsettings;
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}
	public void setFn(String fn) {
		this.fn = fn;
	}
	public void setTabsettings(TabSettings tabsettings) {
		this.tabsettings = tabsettings;
	}
	
}
