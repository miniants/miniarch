/**
 * 
 */
package cn.remex.db.cert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hengyang Liu
 * @since 2012-5-17
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface DataAccessScope {
	final public static String everyone = "everyone";
	final public static String roleControl = "roleControl";

	public String scope() default roleControl;

}
