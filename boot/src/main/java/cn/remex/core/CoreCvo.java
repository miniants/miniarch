package cn.remex.core;

import java.util.HashMap;
import java.util.Map;

public class CoreCvo implements Cvo {

	private static final long serialVersionUID = -7954063904875819600L;

	Map<String, Object> parameters = new HashMap<String, Object>();

	public CoreCvo() {
	}

	@Override
	public void $S(final String name, final Object value) {
		parameters.put(name, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T $V(final String name) {
		return (T)parameters.get(name);
	}

	@Override
	public void putParameters(final Map<String, Object> map) {
		parameters.putAll(map);

	}

	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}

	@Override
	public boolean containsKey(Object key) {
		return parameters.containsKey(key);
	}

}
