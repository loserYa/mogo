package com.loser.function.executor.impl;

import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

public class RemoveStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.REMOVE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.remove((LambdaQueryWrapper<?>) args[0], clazz));
    }

}
