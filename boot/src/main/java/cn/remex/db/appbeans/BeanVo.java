package cn.remex.db.appbeans;

import java.util.List;

public class BeanVo<T> extends CommVo{
	private List<T> beans;

	public List<T> getBeans() {
		return beans;
	}

	public void setBeans(List<T> list) {
		this.beans = list;
	}
	
}
