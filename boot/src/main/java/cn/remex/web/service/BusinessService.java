/**
 * 
 */
package cn.remex.web.service;

import cn.remex.RemexConstants;
import cn.remex.RemexConstants.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cn.remex.RemexConstants.UserType.ADMIN;

/** 
 * @author liuhengyang 
 * @date 2014-12-9 上午11:42:49
 * @version 版本号码
 * @TODO 描述
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD,ElementType.TYPE})
public @interface BusinessService {
    String name() default "";
    ServiceType type() default ServiceType.normal;
	boolean withMultiPart() default false;
    boolean requestBody() default false;
    String bodyParamName() default "";
    String desc() default "";
    boolean needAuth() default true;
    UserType authLevel() default ADMIN;

	enum ServiceType {
		normal,AsyncService
	}
}
