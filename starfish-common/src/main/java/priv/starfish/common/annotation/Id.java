package priv.starfish.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;

/**
 * 用于标明数据模型对应的数据库表列名称
 * 
 * @author Hu Changwei
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
	String name() default "";

	int type() default Types.INTEGER;

	int length() default (int) 0;

	boolean auto() default true;

	String desc() default "";
}
