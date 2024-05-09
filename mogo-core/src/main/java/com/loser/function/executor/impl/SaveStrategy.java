package com.loser.function.executor.impl;

import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

public class SaveStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.SAVE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.save(args[0], clazz));
    }

}
