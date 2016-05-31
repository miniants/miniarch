package cn.remex.core.aop;

import cn.remex.RemexConstants;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.util.Assert;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/**
 * @rmx.summary 切面访问器，调用自定义的切面方法提供额外的代理功能，必须实现MethodInterceptor类的拦截器
 * <p>在拦截器方法intercept(),使用者调用切面方法或执行被代理类的方法，处理里逻辑使用者可以自定义，当调用切面方</p>
 * <p>法前，先执行拦截器中的方法
 * @author HengYang Liu
 * @since  2014-6-9
 */
public class AOPCaller implements MethodInterceptor,RemexConstants,Serializable {

	private static final long serialVersionUID = -4610552176621251394L;
	private Class<? extends Annotation> annotationClass;
	private AOPAspect Iaop;
	private boolean interceptorAllMethod = true;
	
	/**
	 * 初始化切面访问器的注解类，如果注解类不为null，默认拦截所有被代理类的方法，调用调用切面处理逻辑
	 * @rmx.summary 初始化切面访问器的注解类，如果注解类不为null，默认拦截所有被代理类的方法，调用调用切面处理逻辑
	 * @param annotationClass 自定义的注解类，该注解类必须实现Annotation
	 */
	public AOPCaller(final Class<? extends Annotation> annotationClass) {
		super();
		this.annotationClass = annotationClass;
		interceptorAllMethod = null==this.annotationClass;
	}

	public AOPAspect getIaop() {
		return this.Iaop;
	}

	/**
	 * 代理类的入口方法或回调方法，此方法实现添加代理功能，内部处理逻辑开发者可以自定义
	 * @rmx.summary 代理类的入口方法或回调方法，此方法实现添加代理功能，内部处理逻辑开发者可以自定义
	 * <p>当被代理类或被代理类的方法被切面访问器AOPCaller注解变量注解时，自动调用切面的方法</p>
	 * @param obj 被代理对象
	 * @param method 被代理方法
	 * @param args 被代理方法参数
	 * @param proxy 代理方法
	 * @return Object 目标类方法的返回值或通过执行的相应的advice返回
	 */
	public Object intercept(final Object obj, final Method method, final Object[] args,
			final MethodProxy proxy) throws Throwable {
		Object result = null;
		AOPPoint aopPoint=new AOPPoint(obj, method, args, proxy);
		/**
		 * 当Annotation在类上方的时候，应该判断superclass是否有Annotation
		 * 因为cglib帮你创建出来的已经是动态继承了你的类，成了子类！！ 关键！！
		 */
		//		Class<?> c = obj.getClass().getSuperclass();
		//		Annotation[] cas = c.getAnnotations();
		//		Annotation[] mas = method.getAnnotations();
		//
		//		Annotation[] ras = RemexActionAspect.class.getAnnotations();
		if (interceptorAllMethod || method.isAnnotationPresent(this.annotationClass)
				|| obj.getClass().getSuperclass().isAnnotationPresent(this.annotationClass)) {
			try {
				this.Iaop.beforeMethod(aopPoint);
				result =this.Iaop.aroundMethod(aopPoint);
				this.Iaop.afterMethod(aopPoint);
				//result = invokeMethod(obj, args, proxy); // 方法在下面
			}catch (Exception e) {
				this.Iaop.afterThrowing(aopPoint,e);
			}
			return result;
		} else {
			return invokeMethod(obj, args, proxy);
		}
	}

	public void setIaop(final AOPAspect iaop) {
		Assert.notNull(iaop, ServiceCode.FAIL, "处理切面的方法对象AOPAspect不能为null！");
		this.Iaop = iaop;
	}

	private Object invokeMethod(final Object obj, final Object[] args, final MethodProxy proxy)
			throws Throwable {
		return proxy.invokeSuper(obj, args);
	}

	public Class<? extends Annotation> getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public boolean isInterceptorAllMethod() {
		return interceptorAllMethod;
	}

	public void setInterceptorAllMethod(boolean interceptorAllMethod) {
		this.interceptorAllMethod = interceptorAllMethod;
	}

	
}
