package io.github.loserya.function.executor.impl;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.executor.MethodExecutorStrategy;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;

public class PageStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.PAGE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.page((LambdaQueryWrapper<?>) args[0], (Long) args[1], (Long) args[2], clazz));
    }

}
