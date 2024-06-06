/**
 * ColumnCompare.java 代码解读
 * 这段代码是一个Java接口定义，名为ColumnCompare，它定义了一系列用于构建数据库查询条件的方法。这个接口是泛型的，接受一个类型参数LambdaQueryWrapper，这意味着实现这个接口的类将能够返回一个特定类型的LambdaQueryWrapper实例。
 * <p>
 * 接口中的方法主要用于构建SQL查询条件，如等于（eq）、不等于（ne）、小于等于（le）、小于（lt）、大于等于（ge）、大于（gt）、介于（between）、包含于（in）和不包含于（notIn）。这些方法允许用户根据列名和值来构建查询条件。
 * <p>
 * 每个条件构建方法都有两个版本：
 * <p>
 * 一个默认方法，它接受列名和值作为参数，并返回一个LambdaQueryWrapper实例。
 * 一个抽象方法，它额外接受一个布尔参数condition，用于指示是否应该使用该条件进行构建。
 * 例如，eq方法有两个版本：
 * <p>
 * default LambdaQueryWrapper eq(String column, Object val)：这个默认方法接受列名和值，并返回一个LambdaQueryWrapper实例。
 * LambdaQueryWrapper eq(boolean condition, String column, Object val)：这个抽象方法接受一个额外的布尔参数condition，以及列名和值，并返回一个LambdaQueryWrapper实例。
 * 这种设计允许实现类提供更灵活的条件构建逻辑，例如，可以根据condition参数的值来决定是否应用某个条件。
 * <p>
 * 整体上，这个接口为构建复杂的数据库查询提供了一个灵活且强大的方式，使得开发者可以以声明式的方式构建查询条件。
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
public interface ColumnCompare<LambdaQueryWrapper> extends Serializable {

    /**
     * eq 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper eq(String column, Object val) {
        return eq(true, column, val);
    }

    /**
     * ne 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper ne(String column, Object val) {
        return ne(true, column, val);
    }

    /**
     * le 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper le(String column, Object val) {
        return le(true, column, val);
    }

    /**
     * lt 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper lt(String column, Object val) {
        return lt(true, column, val);
    }

    /**
     * ge 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper ge(String column, Object val) {
        return ge(true, column, val);
    }

    /**
     * gt 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper gt(String column, Object val) {
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
    default LambdaQueryWrapper between(String column, Object leftV, Object rightV) {
        return between(true, column, leftV, rightV);
    }

    /**
     * in 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper in(String column, Collection val) {
        return in(true, column, val);
    }

    /**
     * notIn 条件构建
     *
     * @param column 参与比较的列
     * @param val    比较值
     * @return 当前条件构建器
     */
    default LambdaQueryWrapper notIn(String column, Collection val) {
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
    LambdaQueryWrapper eq(boolean condition, String column, Object val);

    /**
     * ne 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper ne(boolean condition, String column, Object val);

    /**
     * le 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper le(boolean condition, String column, Object val);

    /**
     * lt 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper lt(boolean condition, String column, Object val);

    /**
     * ge 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper ge(boolean condition, String column, Object val);

    /**
     * gt 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper gt(boolean condition, String column, Object val);

    /**
     * between 条件构建 左右均包括
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param leftV     左边比较值
     * @param rightV    右边比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper between(boolean condition, String column, Object leftV, Object rightV);

    /**
     * in 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper in(boolean condition, String column, Collection val);

    /**
     * notIn 条件构建
     *
     * @param condition 是否使用该条件进行构建
     * @param column    参与比较的列
     * @param val       比较值
     * @return 当前条件构建器
     */
    LambdaQueryWrapper notIn(boolean condition, String column, Collection val);

}
