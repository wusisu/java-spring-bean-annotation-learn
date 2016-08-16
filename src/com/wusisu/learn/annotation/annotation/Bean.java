package com.wusisu.learn.annotation.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    // Bean名称,不指明则使用方法名
    String value() default "";
}
