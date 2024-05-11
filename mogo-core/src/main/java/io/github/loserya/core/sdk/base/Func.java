package io.github.loserya.core.sdk.base;

import io.github.loserya.utils.func.SFunction;

import java.io.Serializable;

/**
 * 功能接口
 *
 * @author loser
 * @since 1.0.0
 */
public interface Func<T, LambdaQueryWrapper, R extends SFunction<T, ?>> extends Serializable {

    /**
     * 升序 排序接口
     *
     * @param column 参与排序的列
     * @return 当前构建器
     */
    default LambdaQueryWrapper orderByAsc(R column) {
        return orderByAsc(true, column);
    }

    /**
     * 降序 排序接口
     *
     * @param column 参与排序的列
     * @return 当前构建器
     */
    default LambdaQueryWrapper orderByDesc(R column) {
        return orderByDesc(true, column);
    }

    /**
     * 升序 排序接口
     *
     * @param condition 是否参与排序
     * @param column    参与排序的列
     * @return 当前构建器
     */
    LambdaQueryWrapper orderByAsc(boolean condition, R column);

    /**
     * 降序 排序接口
     *
     * @param condition 是否参与排序
     * @param column    参与排序的列
     * @return 当前构建器
     */
    LambdaQueryWrapper orderByDesc(boolean condition, R column);

    /**
     * 需要查询的列,不设置默认查询全部
     *
     * @param columns 查询的列
     * @return 当前构建器
     */
    LambdaQueryWrapper select(R... columns);

}
