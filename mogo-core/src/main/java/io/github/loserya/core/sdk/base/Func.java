/**
 * Func.java 代码解读
 * 这段代码定义了一个名为 Func 的接口，它位于 io.github.loserya.core.sdk.base 包中。这个接口是用于构建和执行数据库查询的功能性接口，特别是用于排序和选择特定列。下面是对代码的详细解释：
 * <p>
 * 接口定义:
 * <p>
 * public interface Func<T, LambdaQueryWrapper, R extends SFunction<T, ?>> extends Serializable:
 * 这是一个泛型接口，其中 T 代表实体类型，LambdaQueryWrapper 是查询包装器的类型，R 是扩展自 SFunction<T, ?> 的类型。
 * 接口实现了 Serializable 接口，意味着其实现类可以被序列化。
 * 方法定义:
 * <p>
 * default LambdaQueryWrapper orderByAsc(R column):
 * 这是一个默认方法，用于实现基于指定列的升序排序。
 * 参数 column 指定了参与排序的列。
 * 方法内部调用了 orderByAsc(true, column)，表明默认情况下参与排序。
 * default LambdaQueryWrapper orderByDesc(R column):
 * 类似于 orderByAsc，但这个方法是用于实现基于指定列的降序排序。
 * LambdaQueryWrapper orderByAsc(boolean condition, R column):
 * 这个方法允许用户指定是否参与排序以及排序的列。
 * condition 参数用于控制是否参与排序，column 指定排序的列。
 * LambdaQueryWrapper orderByDesc(boolean condition, R column):
 * 与 orderByAsc 类似，但用于降序排序。
 * LambdaQueryWrapper select(R... columns): - 这个方法用于指定查询的列。
 * 如果不设置，默认查询所有列。
 * 注释:
 * <p>
 * 代码中的注释用中文编写，提供了对方法功能的简要说明。
 * 总的来说，这个接口提供了一种灵活的方式来构建和执行数据库查询，特别是在排序和选择特定列方面。通过使用泛型和默认方法，它为使用者提供了高度的可定制性和易用性。
 */
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
