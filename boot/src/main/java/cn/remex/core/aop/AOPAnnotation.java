package cn.remex.core.aop;

import java.lang.annotation.*;
/**
 * @rmx.summary 自定义注解类，设置保持性策略（在运行时可获取注解），并指定该注释只能用于标注普通方法、类、接口和枚举
 * <p>handlerClass 被注解的类</p>
 * @author HengYang Liu
 * @since  2014-6-9
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface AOPAnnotation {
	public Class<?> handlerClass();
}


