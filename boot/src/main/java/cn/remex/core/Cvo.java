package cn.remex.core;

import java.io.Serializable;
import java.util.Map;

/**
 * Cvo是Remex框架中定义的通用接口，所有的【输入】都可抽象为一个Cvo。<br>
 * 
 * 
 * 
 */
public interface Cvo extends Serializable {

	public <T> T $V(final String name);

	public void $S(final String name, final Object value);

	public boolean containsKey(final Object key);
	
	public void putParameters(Map<String, Object> map);

	public Map<String, Object> getParameters();

//	public void setBean(Object o);
//
//	public Object getBean();
//
//	public String getRequestBody();
}
