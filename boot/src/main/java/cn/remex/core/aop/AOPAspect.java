package cn.remex.core.aop;

import java.io.Serializable;
/**
 * @rmx.summary 切面接口，提供四种不同的advice方式，所有自定义切面需实现该接口
 * <p>开发者使用时，在切面实现类中实现相应的方法</p>
 * @author HengYang Liu
 * @since  2014-6-9
 */
public interface AOPAspect extends Serializable{
	/**
	 * 定义被代理类方法执行后的处理，实现类方法中写具体处理逻辑
	 * @rmx.summary 定义被代理类方法执行后的处理，实现类方法中写具体处理逻辑
	 * @param aopPoint 切点变量，包括：
	 * <li>被代理对象<li>
     * <li>被代理方法</li>
     * <li>被代理方法参数</li>
     * <li>代理方法</li>
	 */
	public void afterMethod(AOPPoint aopPoint);
	/**
	 * 定义被代理类方法抛出异常后的处理，实现类方法中写具体处理逻辑
	 * @rmx.summary 定义被代理类方法抛出异常后的处理，实现类方法中写具体处理逻辑
	 * @param aopPoint 切点变量，包括：
	 * <li>被代理对象<li>
     * <li>被代理方法</li>
     * <li>被代理方法参数</li>
     * <li>代理方法</li>
	 */
	public void afterThrowing(AOPPoint aopPoint,Exception ex);
	/**
	 * 定义被代理类方法执行过程的处理，实现类方法中写具体处理逻辑
	 * @rmx.summary 定义被代理类方法执行过程的处理，实现类方法中写具体处理逻辑
	 * @param aopPoint 切点变量，包括：
	 * <li>被代理对象<li>
     * <li>被代理方法</li>
     * <li>被代理方法参数</li>
     * <li>代理方法</li>
	 * @return Object 一般是执行被代理类方法后的结果
     */
	public Object aroundMethod(AOPPoint aopPoint) throws Throwable;
	/**
	 * 定义被代理类方法执行前的处理，实现类方法中写具体处理逻辑
	 * @rmx.summary 定义被代理类方法执行前的处理，实现类方法中写具体处理逻辑
	 * @param aopPoint 切点变量，包括：
	 * <li>被代理对象<li>
     * <li>被代理方法</li>
     * <li>被代理方法参数</li>
     * <li>代理方法</li>
     */
	public void beforeMethod(AOPPoint aopPoint);
}

