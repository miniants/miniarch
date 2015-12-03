package cn.remex.core.cache;

import cn.remex.core.exception.BeansException;
/**
 * @rmx.summary 自定义克隆异常类，当类没有覆写克隆方法或实现Cloneable接口
 * <p>调用clone()方法时，抛出此异常
 * @rmx.call {@link cn.remex.core.cache.DataCachePool<T>}
 * @author HengYang Liu
 * @since  2014-6-11
 */
public class PoolNotSupportedBeansException extends BeansException {

	private static final long serialVersionUID = -631175288129187638L;

	public PoolNotSupportedBeansException(final Class<?> beanClass) {
		super("Bean Class "+beanClass+"没有定义clone()方法");
	}

}
