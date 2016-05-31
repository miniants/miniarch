package cn.remex.core.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleSelectMap implements SelectMap {

	HashMap<String, String> selectMap = new HashMap<String, String>();
	@Override
	public Map<String, String> getIt() throws Exception {

		return getSelectMap();
	}
	public HashMap<String, String> getSelectMap() {
		return this.selectMap;
	}
	public void setSelectMap(final HashMap<String, String> selectMap) {
		this.selectMap = selectMap;
	}

}
