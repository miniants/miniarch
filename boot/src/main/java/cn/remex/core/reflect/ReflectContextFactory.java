package cn.remex.core.reflect;

/**
 * 反射上下文工厂
 * @
 * @author lanchenghao
 * @call Remex2-rsqlReflect中的ReflectContextRsqlFactory继承实现
 */
public abstract class ReflectContextFactory {
	/**
	 * @param target 目标类
	 * @param source 源类
	 * @param fieldMapType 属性映射map类型
	 * @param valueMapType 值映射map类型
	 * @return 映射上下文
	 */
	public abstract ReflectContext obtainReflectContext(Class<?> target,Class<?> source,String fieldMapType, String valueMapType);
}
