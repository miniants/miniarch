package cn.remex.db.bs;

import java.io.Serializable;
import java.util.List;

public class DataBsRvoBody implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1523360343741605686L;
	private List<?>  beans;	//返回数据集
	public List<?> getBeans() {
		return beans;
	}
	public void setBeans(List<?> beans) {
		this.beans = beans;
	}
	
}
