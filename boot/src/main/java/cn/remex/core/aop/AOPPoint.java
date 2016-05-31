/**
 * 
 */
package cn.remex.core.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @rmx.summary 封装切点数据
 * <li>被代理对象</li>
 * <li>被代理方法</li>
 * <li>被代理方法参数</li>
 * <li>代理方法
 * @author Hengyang Liu
 * @since 2012-4-3
 *
 */
public class AOPPoint {
	private Object[] args;
	private Method method;
	private MethodProxy proxy;
	private Object target;
	public AOPPoint(final Object target, final Method method, final Object[] args,
			final MethodProxy proxy) {
		super();
		this.target = target;
		this.method = method;
		this.args = args;
		this.proxy = proxy;
	}
	public Object[] getArgs() {
		return this.args;
	}
	public Method getMethod() {
		return this.method;
	}
	public MethodProxy getProxy() {
		return this.proxy;
	}
	public Object getTarget() {
		return this.target;
	}
	/**
	 * 执行被代理类的实例方法，返回执行结果
	 * @rmx.summary 执行被代理类的实例方法，返回执行结果
	 * @return 被代理类方法的返回值
	 * @throws Throwable  当未找到方法时，抛出异常
	 */
	public Object proceed() throws Throwable {
		return this.proxy.invokeSuper(this.target, this.args);
	}
	public void setArgs(final Object[] args) {
		this.args = args;
	}
	public void setMethod(final Method method) {
		this.method = method;
	}
	public void setProxy(final MethodProxy proxy) {
		this.proxy = proxy;
	}
	public void setTarget(final Object target) {
		this.target = target;
	}
}
