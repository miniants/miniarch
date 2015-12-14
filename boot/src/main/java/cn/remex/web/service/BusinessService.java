/**
 * 
 */
package cn.remex.web.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * @author liuhengyang 
 * @date 2014-12-9 上午11:42:49
 * @version 版本号码
 * @TODO 描述
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD,ElementType.TYPE})
public @interface BusinessService {
    boolean withMultiPart() default false;
    boolean requestBody() default false;
}
