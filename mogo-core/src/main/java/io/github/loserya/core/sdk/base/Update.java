package io.github.loserya.core.sdk.base;

import java.io.Serializable;

/**
 * 更新字段接口
 *
 * @author loser
 * @since 1.1.5
 */
public interface Update<LambdaQueryWrapper, R> extends Serializable {

    /**
     * set 字段
     *
     * @param column 更新的类
     * @param val    更新的值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper set(R column, Object val) {
        return set(true, column, val);
    }

    /**
     * set 字段
     *
     * @param condition 是否更新该字段
     * @param column    更新的类
     * @param val       更新的值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper set(boolean condition, R column, Object val);

    /**
     * 递增 字段
     *
     * @param column 更新的类
     * @param val    更新的值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper incr(R column, Number val) {
        return incr(true, column, val);
    }

    /**
     * 递增 字段
     *
     * @param condition 是否更新该字段
     * @param column    更新的类
     * @param val       更新的值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper incr(boolean condition, R column, Number val);

    /**
     * 递减 字段
     *
     * @param column 更新的类
     * @param val    更新的值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper decr(R column, Number val) {
        return decr(true, column, val);
    }

    /**
     * 递减 字段
     *
     * @param condition 是否更新该字段
     * @param column    更新的类
     * @param val       更新的值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper decr(boolean condition, R column, Number val);


}
