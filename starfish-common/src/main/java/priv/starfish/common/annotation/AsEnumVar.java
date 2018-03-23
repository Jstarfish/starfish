package priv.starfish.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识作为枚举js变量 Enums.<枚举类型名称> = { member1 : "member1", member2 : "member2"}
 * 
 * @author koqiui
 * @date 2015年7月12日 下午11:54:22
 *
 */
@Target(value = { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AsEnumVar {
	// 默认为枚举类型名称
	String varName() default "";

	// 默认为枚举类型成员名称
	String valueField() default "";
}
