/**
 * EnableMogo.java 代码解读
 * 这段代码是一个Java注解（Annotation）定义，用于在Java应用程序中提供配置信息。这个特定的注解名为EnableMogo，它被设计用来开启或配置某些功能。下面是对这段代码的详细解释：
 * <p>
 * 包声明 (package io.github.loserya.core.anno;): 这行代码声明了该注解属于io.github.loserya.core.anno包。
 * <p>
 * 导入语句:
 * <p>
 * import java.lang.annotation.Documented;
 * import java.lang.annotation.ElementType;
 * import java.lang.annotation.Retention;
 * import java.lang.annotation.RetentionPolicy;
 * import java.lang.annotation.Target;
 * 这些导入语句用于引入Java注解相关的类和接口。
 * <p>
 * 注解定义 (@Retention(RetentionPolicy.RUNTIME)):
 *
 * @Retention(RetentionPolicy.RUNTIME) 指定了这个注解在运行时是可用的。这意味着在运行时可以通过反射来访问这个注解。
 * 目标类型 (@Target({ElementType.TYPE})):
 * @Target({ElementType.TYPE}) 表明这个注解只能被应用于类型（比如类、接口、枚举）上。
 * 文档化 (@Documented):
 * @Documented 表示这个注解会被包含在JavaDoc中。
 * 注解本身 (public @interface EnableMogo):
 * <p>
 * 这是定义一个新的注解EnableMogo。
 * 注解内的元素:
 * <p>
 * boolean base() default true; - 是否开启基础功能，默认为true。
 * boolean logic() default false; - 是否开启逻辑删除功能，默认为false。
 * boolean autoFill() default false; - 是否开启逻辑字段自动填充功能，默认为false。
 * boolean dynamicDs() default false; - 是否开启多数据源，默认为false。 - boolean banFullTable() default false; - 禁止全表更新及全表删除，默认为false。
 * boolean transaction() default false; - 是否开启事务功能，默认为false。
 * boolean debugLog() default false; - 是否开启调试日志，默认为false。
 * 这个注解提供了一种灵活的方式来配置和扩展应用程序的功能，例如逻辑删除、自动填充字段、多数据源支持、事务处理等。通过在类上使用这个注解并设置相应的属性，可以轻松地开启或关闭这些功能。
 */
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
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface EnableMogo {

    /**
     * 是否开启基础功能
     */
    boolean base() default true;

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

    /**
     * 禁止全表更新及全表删除
     */
    boolean banFullTable() default false;

    /**
     * 是否开启事务功能
     */
    boolean transaction() default false;

    /**
     * 是否开启 debug 日志 (建议测试环境临时开启、杜绝线上开启)
     */
    boolean debugLog() default false;

}
