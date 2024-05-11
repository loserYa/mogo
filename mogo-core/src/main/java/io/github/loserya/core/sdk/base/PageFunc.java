package io.github.loserya.core.sdk.base;

import io.github.loserya.utils.func.SFunction;

import java.io.Serializable;

/**
 * 功能接口
 *
 * @author loser
 * @date 2023/2/4 21:09
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

}
