package com.loser.function.executor.impl;

import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

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
