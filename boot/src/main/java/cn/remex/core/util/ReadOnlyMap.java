package cn.remex.core.util;

import java.util.LinkedHashMap;

public class ReadOnlyMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		// throw new
		// Exception("This map is defined for Reflect Tools, cannot change it!");
	}

	@Override
	public V remove(final Object arg0) {
		// TODO Auto-generated method stub
		// throw new
		// Exception("This map is defined for Reflect Tools, cannot change it!");
		return null;
	}

}
