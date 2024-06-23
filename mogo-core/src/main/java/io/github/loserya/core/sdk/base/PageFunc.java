/**
 * PageFunc.java 代码解读
 * 这段代码定义了一个名为 PageFunc 的接口，它用于实现分页功能。这个接口属于 io.github.loserya.core.sdk.base 包，并且是一个泛型接口，包含三个类型参数：T、LambdaQueryWrapper 和 R。
 * 下面是对这段代码的详细解释：
 * <p>
 * 接口声明：
 * <p>
 * public interface PageFunc<T, LambdaQueryWrapper, R extends SFunction<T, ?>> extends Serializable {
 * PageFunc 是一个泛型接口，有三个类型参数：
 * T：通常代表实体类或数据模型。
 * LambdaQueryWrapper：可能是一个用于构建查询的包装器类，用于实现复杂的查询逻辑。
 * R：继承自 SFunction<T, ?>，这表明 R 是一个函数式接口，用于处理 T 类型的数据。
 * 该接口实现了 Serializable 接口，意味着其实现类可以被序列化。
 * 方法 skip：
 * <p>
 * LambdaQueryWrapper skip(Long skip);
 * 这个方法用于实现分页的跳过（skip）功能。
 * 参数 skip 表示要跳过的记录数。
 * 返回类型为 LambdaQueryWrapper，表明调用此方法后，会返回一个更新后的查询包装器实例。
 * 方法 limit：
 * <p>
 * 这个方法用于实现分页的限制（limit）功能。
 * 参数 limit 表示最多获取的记录数。
 * 同样返回 LambdaQueryWrapper 类型，表示返回更新后的查询包装器实例。
 * 整体来看，这个接口提供了基本的分页功能，允许用户通过 skip 和 limit 方法来控制数据的分页查询。这种设计模式在处理大量数据时非常有用，可以有效地减少内存消耗和提高应用性能。
 */
package io.github.loserya.core.sdk.base;

import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.func.SFunction;

import java.io.Serializable;

/**
 * 功能接口
 *
 * @author loser
 * @since 1.0.0
 */
public interface PageFunc<T, LambdaQueryWrapper, R extends SFunction<T, ?>> extends Serializable {

    /**
     * 分页skip接口
     *
     * @param skip 跳过多少条数据
     * @return 当前构建器
     */
    LambdaQueryWrapper skip(Long skip);

    /**
     * 分页limit接口
     *
     * @param limit 最多取多少条数据
     * @return 当前构建器
     */
    LambdaQueryWrapper limit(Integer limit);

    /**
     * 分页limit接口
     *
     * @param limit 最多取多少条数据 限制不能大于 Integer.MAX_VALUE
     * @return 当前构建器
     */
    default LambdaQueryWrapper limit(Long limit) {
        if (limit > Integer.MAX_VALUE) {
            throw ExceptionUtils.mpe("limit is un gt Integer.MAX_VALUE");
        }
        return limit(limit.intValue());
    }

}
