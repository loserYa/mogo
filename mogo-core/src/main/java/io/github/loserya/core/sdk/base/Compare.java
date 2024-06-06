/**
 * Compare.java 代码解读
 * 这段代码定义了一个名为 Compare 的接口，它用于封装数据库查询中的各种比较条件。这个接口是泛型的，接受两个类型参数：LambdaQueryWrapper 和 R。
 * LambdaQueryWrapper 通常用于构建和执行数据库查询，而 R 代表参与比较的列的类型。
 * <p>
 * 接口中的方法分为两组：
 * <p>
 * 默认方法：这些方法提供了构建常见比较条件的快捷方式。例如，eq方法用于构建等于（equal）条件，ne 用于不等于（not equal）条件，lt用于小于（less than）条件，等等。
 * 这些方法都接受一个列和一个值作为参数，并返回一个LambdaQueryWrapper` 实例，允许链式调用以构建更复杂的查询。
 * <p>
 * 抽象方法：这些方法提供了更灵活的条件构建方式，允许调用者指定是否应用某个条件。每个方法都有一个额外的 boolean 参数 condition，当 condition 为 true 时，将应用相应的比较条件。
 * <p>
 * 以下是接口中定义的方法及其用途：
 * <p>
 * eq：等于条件。
 * ne：不等于条件。
 * le：小于等于条件。
 * lt：小于条件。
 * ge：大于等于条件。
 * gt：大于条件。
 * between：介于两个值之间的条件。
 * in：在给定集合中的条件。
 * notIn：不在给定集合中的条件。
 * 每个方法都返回 LambdaQueryWrapper 类型的对象，这允许在构建查询时进行链式调用。例如，可以使用 eq 方法来添加一个等于条件，然后紧接着使用 lt 方法来添加一个小于条件，从而构建一个复合查询条件。
 * <p>
 * 这种设计模式提高了代码的可读性和易用性，使得构建复杂的数据库查询变得更加直观和简洁。
 */
package io.github.loserya.core.sdk.base;

import java.io.Serializable;
import java.util.Collection;

/**
 * 查询条件封装
 *
 * @author loser
 * @since 1.0.0
 */
public interface Compare<LambdaQueryWrapper, R> extends Serializable {

    /**
     * eq 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper eq(R column, Object val) {
        return eq(true, column, val);
    }

    /**
     * ne 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper ne(R column, Object val) {
        return ne(true, column, val);
    }

    /**
     * le 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper le(R column, Object val) {
        return le(true, column, val);
    }

    /**
     * lt 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper lt(R column, Object val) {
        return lt(true, column, val);
    }

    /**
     * ge 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper ge(R column, Object val) {
        return ge(true, column, val);
    }

    /**
     * gt 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper gt(R column, Object val) {
        return gt(true, column, val);
    }

    /**
     * between 条件构建 左右均包括
     *
     * @param column 参与比较的列
     * @param leftV  左边比较值
     * @param rightV 右边比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper between(R column, Object leftV, Object rightV) {
        return between(true, column, leftV, rightV);
    }

    /**
     * in 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper in(R column, Collection val) {
        return in(true, column, val);
    }

    /**
     * notIn 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper notIn(R column, Collection val) {
        return notIn(true, column, val);
    }

    /**
     * in 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper eq(boolean condition, R column, Object val);

    /**
     * ne 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper ne(boolean condition, R column, Object val);

    /**
     * le 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper le(boolean condition, R column, Object val);

    /**
     * lt 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper lt(boolean condition, R column, Object val);

    /**
     * ge 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper ge(boolean condition, R column, Object val);

    /**
     * gt 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper gt(boolean condition, R column, Object val);

    /**
     * between 条件构建 左右均包括
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param leftV     左边比较值
     * @param rightV    右边比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper between(boolean condition, R column, Object leftV, Object rightV);

    /**
     * in 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper in(boolean condition, R column, Collection val);

    /**
     * notIn 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper notIn(boolean condition, R column, Collection val);

}
