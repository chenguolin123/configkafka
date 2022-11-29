package com.senghong.kafka.annotations;

import java.lang.annotation.*;

/**
 * note: mysql字段映射
 *
 * @author cgl
 * @since 2021/8/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CanalField {
    /**
     * 字段名
     */
    String value() default "";

    /**
     * 时间格式
     */
    String dateFormat();


}
