package com.loser.core.strategy.executor.impl;

import com.loser.core.constant.ExecuteMethodEnum;
import com.loser.core.interceptor.Interceptor;
import com.loser.core.strategy.executor.MethodExecutorStrategy;
import com.loser.core.wrapper.LambdaQueryWrapper;

public class UpdateStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.UPDATE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.update(args[0], (LambdaQueryWrapper) args[1], clazz));
    }

}
