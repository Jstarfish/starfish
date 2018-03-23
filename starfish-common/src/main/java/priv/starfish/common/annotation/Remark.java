package priv.starfish.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注释 注解
 * created by starfish on 2017-12-19 14:01
 */

@Target(value = { ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Remark {

    // 描述文本
    String value() default "";
    //权限代码
    String code() default "";
}
