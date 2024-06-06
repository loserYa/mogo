/**
 * Nested.java 代码解读
 * 这段代码定义了一个名为 Nested 的接口，它用于封装查询条件，特别是在构建数据库查询时支持嵌套条件的构造。这个接口是泛型的，有两个类型参数：Param 和 LambdaQueryWrapper。Param 代表嵌套条件的参数类型，而 LambdaQueryWrapper 代表查询包装器的类型。
 * <p>
 * 接口中定义了四个方法，每个方法都接受一个 Function<Param, Param> 类型的参数 func，这个函数用于定义嵌套的条件。这些方法允许用户以链式调用的方式构建复杂的查询条件。
 * <p>
 * default LambdaQueryWrapper and(Function<Param, Param> func): 这个默认方法提供了一个构造嵌套条件的简便方式，使用 AND 逻辑连接。它内部调用了 and(true, func) 方法。
 * <p>
 * LambdaQueryWrapper or(Function<Param, Param> func): 这个方法用于构造使用 OR 逻辑连接的嵌套条件。
 * <p>
 * LambdaQueryWrapper and(boolean condition, Function<Param, Param> func): 这个方法允许用户指定是否开启嵌套（通过 condition 参数），并使用 AND 逻辑连接嵌套条件。
 * <p>
 * LambdaQueryWrapper or(boolean condition, Function<Param, Param> func): 类似于上一个方法，但这个方法使用 OR 逻辑连接嵌套条件。
 * <p>
 * 这个接口的设计使得构建复杂的数据库查询变得更加灵活和直观，特别是在需要嵌套多个条件时。通过使用 Java 8 的函数式编程特性（如 Function 接口），它提供了一种声明式的方式来构建查询，使得代码更加简洁和易于理解。
 */
package io.github.loserya.core.sdk.base;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 查询条件封装
 * <p>嵌套</p>
 *
 * @author loser
 * @since 1.0.0
 */
public interface Nested<Param, LambdaQueryWrapper> extends Serializable {
    /**
     * 嵌套条件构造器
     *
     * @param func 嵌套条件
     * @return 当前条件构造器
     */
    default LambdaQueryWrapper and(Function<Param, Param> func) {
        return and(true, func);
    }

    /**
     * 嵌套条件构造器 使用or连接
     *
     * @param func 子条件
     * @return 当前条件构造器
     */
    LambdaQueryWrapper or(Function<Param, Param> func);

    /**
     * 嵌套条件构造器
     *
     * @param condition 是否开启嵌套
     * @param func      嵌套条件
     * @return 当前条件构造器
     */
    LambdaQueryWrapper and(boolean condition, Function<Param, Param> func);

    /**
     * 嵌套条件构造器 使用or连接
     *
     * @param condition 是否开启嵌套
     * @param func      嵌套条件
     * @return 当前条件构造器
     */
    LambdaQueryWrapper or(boolean condition, Function<Param, Param> func);

}
