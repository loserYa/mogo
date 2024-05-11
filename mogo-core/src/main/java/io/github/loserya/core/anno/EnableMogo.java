package io.github.loserya.core.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启其他扩展功能如逻辑删除、动态数据源等
 * 不是使用则默认只存在 lambda 简化 CRUD 功能
 *
 * @author loser
 * @date 2024/5/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface EnableMogo {

    /**
     * 是否开启基础功能
     */
    boolean base() default false;

    /**
     * 是否开启逻辑删除功能
     */
    boolean logic() default false;

    /**
     * 是否开启逻辑字段自动填充功能
     */
    boolean autoFill() default false;

    /**
     * 是否开启多数据源
     */
    boolean dynamicDs() default false;

}
