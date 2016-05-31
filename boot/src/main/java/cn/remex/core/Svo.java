package cn.remex.core;


/**
 * 系统当前状态变量，如session，用户，线程等
 * 存储状态信息
 * 线程问题[存/取变量]
 * 会话问题[存/取变量][有效期]
 * @author lanchenghao
 * @since 2013-7-23下午7:40:37
 */
public interface Svo {	
	
	/**
	 * 从当前线程取值
	 * @param key
	 */
	public Object $VL(String key);
	
	/**
	 * 从当前线程存值
	 * @param key
	 * @param value
	 */
	public void $SL(String key,Object value);
	
	/**
	 * 从当前会话取值
	 * @param key
	 */
	public Object $VS(String key);
	
	/**
	 * 从当前会话存值
	 * @param key
	 * @param value
	 */
	public void $SS(String key,Object value);
	
}
