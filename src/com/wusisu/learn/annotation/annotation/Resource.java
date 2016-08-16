package com.wusisu.learn.annotation.annotation;

import java.lang.annotation.*;

// 用在字段上,表示引用的Bean
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
    // 引用bean名称
    String value();
}