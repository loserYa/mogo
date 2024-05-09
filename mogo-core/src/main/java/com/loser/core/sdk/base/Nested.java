package com.loser.core.sdk.base;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 查询条件封装
 * <p>嵌套</p>
 *
 * @author loser
 * @date 2023/2/4 22:44
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
     * @return 当前条件构造器
     */
    LambdaQueryWrapper or(boolean condition, Function<Param, Param> func);

}
