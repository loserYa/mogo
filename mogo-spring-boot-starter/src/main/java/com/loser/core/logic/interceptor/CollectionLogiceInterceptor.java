package com.loser.core.logic.interceptor;

import com.loser.core.cache.global.CollectionLogicDeleteCache;
import com.loser.core.interceptor.Interceptor;
import com.loser.core.logic.entity.LogicDeleteResult;
import com.loser.core.wrapper.LambdaQueryWrapper;

import java.util.Objects;

/**
 * 逻辑删除拦截器
 *
 * @author loser
 * @date 2024/4/30
 */
public class CollectionLogiceInterceptor implements Interceptor {


    @Override
    public Object[] getOne(LambdaQueryWrapper queryWrapper, Class<?> clazz) {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(queryWrapper);
        }
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(queryWrapper);

    }

    @Override
    public Object[] update(Object entity, LambdaQueryWrapper queryWrapper, Class<?> clazz) {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(queryWrapper);
        }
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(queryWrapper);

    }

    @Override
    public Object[] count(LambdaQueryWrapper queryWrapper, Class<?> clazz) {
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(queryWrapper);
        }
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(queryWrapper);
    }

    @Override
    public Object[] list(LambdaQueryWrapper queryWrapper, Class<?> clazz) {
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(queryWrapper);
        }
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(queryWrapper);
    }

    @Override
    public Object[] page(LambdaQueryWrapper queryWrapper, int pageNo, int pageSize, Class<?> clazz) {
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(queryWrapper, pageNo, pageSize);
        }
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(queryWrapper, pageNo, pageSize);
    }

    @Override
    public Object[] exist(LambdaQueryWrapper queryWrapper, Class<?> clazz) {
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(queryWrapper);
        }
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(queryWrapper);
    }

}
