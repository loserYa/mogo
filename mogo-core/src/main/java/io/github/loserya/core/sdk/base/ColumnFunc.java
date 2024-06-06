/**
 * ColumnFunc.java 代码解读
 * 这段代码是一个Java接口定义，用于实现数据库查询时的列操作功能。接口名为ColumnFunc，它定义了一系列方法，用于在数据库查询中对列进行排序和选择。下面是对每个方法的详细解释：
 * <p>
 * orderByAsc(String column):
 * <p>
 * 这是一个默认方法，用于对指定的列进行升序排序。
 * 参数column表示需要排序的列名。
 * 方法返回LambdaQueryWrapper对象，这是一个构建器模式中的对象，用于链式调用其他方法。
 * orderByDesc(String column):
 * <p>
 * 类似于orderByAsc，但这个方法用于对指定的列进行降序排序。
 * 参数column同样表示需要排序的列名。
 * 返回值也是LambdaQueryWrapper对象。
 * orderByAsc(boolean condition, String column):
 * <p>
 * 这个方法提供了更多的灵活性，允许用户指定是否对列进行排序。
 * 参数condition是一个布尔值，指示是否进行排序。
 * column参数表示需要排序的列名。
 * 返回LambdaQueryWrapper对象。
 * orderByDesc(boolean condition, String column):
 * <p>
 * 与orderByAsc类似，但用于降序排序。
 * condition参数控制是否进行排序。
 * column参数指定排序的列名。
 * 返回LambdaQueryWrapper对象。
 * select(String... columns):
 * <p>
 * 这个方法用于指定查询中需要包含的列。
 * 参数columns是一个字符串数组，表示需要查询的列名。
 * 如果不指定任何列，则默认查询所有列。
 * 返回LambdaQueryWrapper对象。
 * 整体来看，这个接口提供了一种灵活的方式来构建数据库查询，特别是在排序和选择特定列方面。通过这种方式，可以轻松地构建复杂的查询语句，同时保持代码的可读性和简洁性。
 */
package io.github.loserya.core.sdk.base;

import java.io.Serializable;

/**
 * 功能接口
 *
 * @author loser
 * @since 1.0.0
 */
public interface ColumnFunc<T, LambdaQueryWrapper> extends Serializable {

    /**
     * 升序 排序接口
     *
     * @param column 参与排序的列
     * @return 当前构建器
     */
    default LambdaQueryWrapper orderByAsc(String column) {
        return orderByAsc(true, column);
    }

    /**
     * 降序 排序接口
     *
     * @param column 参与排序的列
     * @return 当前构建器
     */
    default LambdaQueryWrapper orderByDesc(String column) {
        return orderByDesc(true, column);
    }

    /**
     * 升序 排序接口
     *
     * @param condition 是否参与排序
     * @param column    参与排序的列
     * @return 当前构建器
     */
    LambdaQueryWrapper orderByAsc(boolean condition, String column);

    /**
     * 降序 排序接口
     *
     * @param condition 是否参与排序
     * @param column    参与排序的列
     * @return 当前构建器
     */
    LambdaQueryWrapper orderByDesc(boolean condition, String column);

    /**
     * 需要查询的列,不设置默认查询全部
     *
     * @param columns 查询的列
     * @return 当前构建器
     */
    LambdaQueryWrapper select(String... columns);

}
