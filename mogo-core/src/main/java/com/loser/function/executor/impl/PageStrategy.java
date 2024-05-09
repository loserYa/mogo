package com.loser.function.executor.impl;

import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

public class PageStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.PAGE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.page((LambdaQueryWrapper<?>) args[0], (Integer) args[1], (Integer) args[2], clazz));
    }

}
