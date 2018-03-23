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
public @interface Column {
	String name() default "";

	int type() default Types.VARCHAR;

	boolean nullable() default true;
	
	boolean updatable() default true;

	String defaultValue() default "";

	int length() default (int) 0;

	// 数字的总位数
	int precision() default (int) 0;

	// 小数点总位数
	int scale() default (int) 0;

	String desc() default "";
}
