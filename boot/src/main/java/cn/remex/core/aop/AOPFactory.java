package cn.remex.core.aop;

import net.sf.cglib.proxy.Enhancer;

import java.io.Serializable;
import java.lang.annotation.Annotation;
/**
 * @rmx.summary 创建切面的工厂类，采用cglib方式实现AOP，通过调用getBean（）方法手动装配目标类，生成代理子类
 * <p>开发者使用时，需先通过AOPFactory的构造方法创建实例，然后通过getBean方法获取代理类，参数为被代理类</p>
 * @author HenYang Liu
 * @since  2014-6-9
 */
public class AOPFactory implements Serializable {
	private static final long serialVersionUID = -8264337968632924715L;
	private AOPCaller aopCaller;
	private AOPAspect handler;
    /**
     * 创建工厂实例，装配切面访问器
     * @rmx.summary 创建工厂实例，装配切面访问器
     * <p>当开发者不打算自定义切面类，只需要创建一个AOPCaller的实例，</p>
     * <p>并重写拦截器方法，使用此构函数初始化切面访问器</p>
     * @param aopCaller 切面访问器，通过切面访问器调用拦截器方法
     */
	public AOPFactory(final AOPCaller aopCaller){
		this.aopCaller = aopCaller;
	}
	/**
	 * 创建工厂实例，装配切面和切面访问器
	 * @rmx.summary 创建工厂实例，装配切面和切面访问器
	 * <p>当开发者需要通过自定义切面对被代理方法进行预处理或后处理，调用此构造函数初始</p>
	 * <p>切面类、切面访问器，并为访问器装配自定义的切面，无论被代理类或被代理类方法是</p>
	 * <p>否被注解，访问器器都会调用切面方法</p>
	 * @param aopAspect 自定义切面实例
	 */
	public AOPFactory(final AOPAspect aopAspect){
		this.handler = aopAspect;
		this.aopCaller = new AOPCaller(null);
		this.aopCaller.setIaop(this.handler);
	}
	/**
	 * 创建工厂实例，装配自定义的切面和切面访问器
	 * @rmx.summary 创建工厂实例，装配自定义的切面和切面访问器
	 * <p>开发者需要调用自定义切面和切面访问器，在切面访问器中覆写拦截器方法</p>
	 * @param aopAspect
	 * @param aopCaller
	 */
	public AOPFactory(final AOPAspect aopAspect, final AOPCaller aopCaller) {
		// 装配AOP
		this.handler = aopAspect;
		this.aopCaller = aopCaller;
		this.aopCaller.setIaop(this.handler);
	}
	/**
	 *  创建工厂实例，装配自定义切面，并使用自定义的注解类
	 * @rmx.summary 创建工厂实例，装配自定义切面，并使用自定义的注解类
	 * <p>开发者需要自定义切面类和注解类，当被代理类被注解时，执行所有的被代理方法都会调用切面逻辑，</p>
	 * <p>若被代理类未注解，只有当被注解的方法执行时，才会调用切面</p>
	 * @param aopAspect
	 * @param annotationClass
	 */
	public AOPFactory(final AOPAspect aopAspect, final Class<? extends Annotation> annotationClass) {
		// 装配AOP
		this.handler = aopAspect;
		this.aopCaller = new AOPCaller(annotationClass);
		this.aopCaller.setIaop(this.handler);
	}

	/**
	 * 通过Cglib方式实现AOP，使用织入器Enhancer为指定的类创建代理子类
	 * @rmx.summary 通过Cglib方式实现AOP，使用织入器Enhancer为指定的类创建代理子类
	 * <p>开发者使用时,需要传入代理目标类</p>
	 * @param clazz 代理目标类
	 * @return T 代理子类 
	 * @rmx.call {@link cn.remex.db.rsql.RDBUtils}
	 * @rmx.call {@link cn.remex.db.rsql.sql.SqlPredicate<T>}
	 * @note Cglib是针对类来实现代理的，它的原理是对指定的目标类生成一个子类，因此目标类不能对final修饰的类进行代理
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(final Class<T> clazz){ 
		//创建一个织入器
		Enhancer enhancer = new Enhancer();
		//设置父类
		enhancer.setSuperclass(clazz);
		//设置需要织入的逻辑
		enhancer.setCallback(this.aopCaller);
		//使用织入器创建子类
		T o = (T) enhancer.create();
		return o;
	}
	public AOPCaller getAopCaller() {
		return aopCaller;
	}
	public void setAopCaller(AOPCaller aopCaller) {
		this.aopCaller = aopCaller;
	}
	public AOPAspect getHandler() {
		return handler;
	}
	public void setHandler(AOPAspect handler) {
		this.handler = handler;
	}
}
