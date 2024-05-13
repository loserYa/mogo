package io.github.loserya.module.interceptor.fulltable;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.utils.ExceptionUtils;

public final class FullTableInterceptor implements Interceptor {

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Object[] remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        if (queryWrapper.conditionsSize() == 0) {
            throw ExceptionUtils.mpe("remove full table refuse!!!");
        }
        return build(queryWrapper);
    }

    @Override
    public Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        if (queryWrapper.conditionsSize() == 0) {
            throw ExceptionUtils.mpe("update full table refuse!!!");
        }
        return build(entity, queryWrapper);
    }

}