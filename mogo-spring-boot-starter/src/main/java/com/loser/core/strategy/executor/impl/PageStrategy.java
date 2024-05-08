package com.loser.core.strategy.executor.impl;

import com.loser.core.constant.ExecuteMethodEnum;
import com.loser.core.interceptor.Interceptor;
import com.loser.core.strategy.executor.MethodExecutorStrategy;
import com.loser.core.wrapper.LambdaQueryWrapper;

public class PageStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.PAGE;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.page((LambdaQueryWrapper) args[0], (Integer) args[1], (Integer) args[2], clazz));
    }

}
