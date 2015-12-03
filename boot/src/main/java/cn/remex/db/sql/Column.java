package cn.remex.db.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.FIELD,ElementType.METHOD})

public @interface Column {

	public abstract int length() default 50;
	public abstract String columnDefinition() default "";
	public abstract int type() ;
}
