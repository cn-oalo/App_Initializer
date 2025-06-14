package cn.oalo.common.annotation;

import java.lang.annotation.*;

/**
 * 数据源切换注解
 * 
 * 该注解不需要实现，是为了使项目结构更加清晰，实际使用时请直接使用 @DS 注解
 * 实际数据源注解由 com.baomidou.dynamic.datasource.annotation.DS 提供
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {
    /**
     * 数据源名称
     */
    String value() default "master";
} 