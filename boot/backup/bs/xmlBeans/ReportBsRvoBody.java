package cn.remex.db.bs.xmlBeans;

import java.util.List;

public class ReportBsRvoBody {
	private List<?>  beans;	//返回数据集
	public List<?> getBeans() {
		return beans;
	}
	public void setBeans(List<?> beans) {
		this.beans = beans;
	}
	private List<?> rows;
	private List<String> titles;
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public List<String> getTitles() {
		return titles;
	}
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}
	
	
}
