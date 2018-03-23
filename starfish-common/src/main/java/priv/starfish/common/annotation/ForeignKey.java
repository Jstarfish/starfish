package priv.starfish.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public abstract @interface ForeignKey {
	String name() default "";

	Class<?> refEntityClass();

	String refFieldName();
	
	String desc() default "";	
}
