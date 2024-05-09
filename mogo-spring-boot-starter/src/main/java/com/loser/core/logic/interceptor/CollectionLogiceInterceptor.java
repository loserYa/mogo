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
    public Object[] getOne(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper);
    }

    @Override
    public Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 1, entity, queryWrapper);
    }

    @Override
    public Object[] count(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper);
    }

    @Override
    public Object[] list(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper);
    }

    @Override
    public Object[] page(LambdaQueryWrapper<?> queryWrapper, int pageNo, int pageSize, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper, pageNo, pageNo);
    }

    @Override
    public Object[] exist(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper);
    }

    private Object[] appendLogicCondition(Class<?> clazz, int index, Object... args) {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(args);
        }
        LambdaQueryWrapper<?> queryWrapper = (LambdaQueryWrapper<?>) args[index];
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return build(args);

    }

}
