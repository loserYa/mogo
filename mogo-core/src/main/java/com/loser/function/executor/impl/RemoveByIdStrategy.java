package com.loser.function.executor.impl;

import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

public class RemoveByIdStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.REMOVE_BY_ID;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.removeById(args[0], clazz));
    }

}
