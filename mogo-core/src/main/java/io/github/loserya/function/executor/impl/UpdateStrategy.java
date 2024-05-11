package io.github.loserya.function.executor.impl;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.executor.MethodExecutorStrategy;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;

public class UpdateStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.UPDATE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.update(args[0], (LambdaQueryWrapper<?>) args[1], clazz));
    }

}
