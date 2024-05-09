package com.loser.function.executor.impl;

import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

public class ExistStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.EXIST;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.exist((LambdaQueryWrapper<?>) args[0], clazz));
    }

}
