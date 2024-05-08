package com.loser.core.strategy.executor.impl;

import com.loser.core.constant.ExecuteMethodEnum;
import com.loser.core.interceptor.Interceptor;
import com.loser.core.strategy.executor.MethodExecutorStrategy;

import java.util.Collection;

public class ListByIdsStrategy implements MethodExecutorStrategy {

    @Override
    public ExecuteMethodEnum method() {
        return ExecuteMethodEnum.LIST_BY_IDS;
    }

    @Override
    public void invoke(Class<?> clazz, Interceptor interceptor, Object[] args) {
        replace(args, interceptor.listByIds((Collection) args[0], clazz));
    }

}
