package io.github.loserya.function.executor.impl;

import io.github.loserya.function.executor.MethodExecutorStrategy;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;

public class GetByIdStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.GET_BY_ID;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.getById(args[0], clazz));
    }

}
