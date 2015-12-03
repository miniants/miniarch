package cn.remex.db.bs;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  本类为数据库操作的操作属性，对数据库操作相关的指令集合
 */
@XStreamAlias("DataCmd")
public class JqgColModelBsCvoExtend extends Extend{
	public JqgColModelBsCvoExtend() {
		super(false, "");
	}
	private static final long serialVersionUID = -3187876713117061082L;
	private String viewName;
	private String beanName;
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	
}
