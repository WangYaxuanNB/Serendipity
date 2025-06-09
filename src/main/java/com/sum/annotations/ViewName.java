package com.sum.annotations;

import java.lang.annotation.*;

/**
 * 视图名称
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ViewName {

    String code() default "";
    String name() default "";
    int sorted() default 0;
}
