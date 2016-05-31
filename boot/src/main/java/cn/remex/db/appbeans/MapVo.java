package cn.remex.db.appbeans;

import java.util.List;

public class MapVo extends CommVo{
	private List<java.util.Map<String, Object>> rows;

	public List<java.util.Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<java.util.Map<String, Object>> list) {
		this.rows = list;
	}
	
}
