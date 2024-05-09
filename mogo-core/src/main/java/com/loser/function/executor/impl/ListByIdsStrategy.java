package com.loser.function.executor.impl;

import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.interceptor.Interceptor;
import com.loser.hardcode.constant.ExecuteMethodEnum;

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
