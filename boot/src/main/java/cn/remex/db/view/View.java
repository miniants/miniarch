/**
 * 
 */
package cn.remex.db.view;

import cn.remex.db.model.DataDic;
import cn.remex.db.rsql.model.ModelableImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hengyang Liu
 * @since 2012-4-28
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE})
public @interface View {
	public String viewName() default "";
}
