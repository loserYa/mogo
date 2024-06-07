package io.github.loserya.module.logic.interceptor;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.module.logic.entity.LogicDeleteResult;

import java.util.Objects;

/**
 * 逻辑删除拦截器
 *
 * @author loser
 * @since 1.0.0
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
    public Object[] lambdaUpdate(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper);
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
    public Object[] page(LambdaQueryWrapper<?> queryWrapper, Long pageNo, Long pageSize, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper, pageNo, pageNo);
    }

    @Override
    public Object[] exist(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return appendLogicCondition(clazz, 0, queryWrapper);
    }

    private Object[] appendLogicCondition(Class<?> clazz, int index, Object... args) {

        if (CollectionLogicDeleteCache.isClose()) {
            return args;
        }
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return args;
        }
        LambdaQueryWrapper<?> queryWrapper = (LambdaQueryWrapper<?>) args[index];
        queryWrapper.eq(result.getColumn(), result.getLogicNotDeleteValue());
        return args;

    }

}
