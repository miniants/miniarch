package cn.remex.core;

import java.util.HashMap;
import java.util.Map;

public class CoreCvo implements Cvo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7954063904875819600L;


//	private Object bean;


	Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * 
	 */
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

//	@Override
//	public Object getBean() {
//		return bean;
//	}

//	@Override
//	public String getRequestBody() {
//		return RemexContext.getContext().getRequestBody();
//	}

	/* (non-Javadoc)
	 * @see cn.remex.core.Cvo#put(java.jstl.Map)
	 */
	@Override
	public void putParameters(final Map<String, Object> map) {
		// TODO Auto-generated method stub
		parameters.putAll(map);

	}

//	@Override
//	public void setBean(final Object bean) {
//		this.bean = bean;
//	}

	@Override
	public Map<String, Object> getParameters() {
		// TODO Auto-generated method stub
		return parameters;
	}

	@Override
	public boolean containsKey(Object key) {
		return parameters.containsKey(key);
	}

}
