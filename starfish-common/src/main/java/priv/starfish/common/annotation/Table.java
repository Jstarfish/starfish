package priv.starfish.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public abstract @interface Table {
	String name();

	String catalog() default "";

	String schema() default "";

	UniqueConstraint[] uniqueConstraints() default {};

	String desc() default "";
}
