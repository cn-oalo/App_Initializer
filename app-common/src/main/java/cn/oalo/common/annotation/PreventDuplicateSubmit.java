package cn.oalo.common.annotation;

import java.lang.annotation.*;

/**
 * 防止重复提交注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventDuplicateSubmit {
    
    /**
     * 防重复操作过期时间（毫秒）
     * 默认1秒
     */
    int expireMillis() default 1000;
    
    /**
     * 提示消息
     */
    String message() default "请勿重复提交";
} 