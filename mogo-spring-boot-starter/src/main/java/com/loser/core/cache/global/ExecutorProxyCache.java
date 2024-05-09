package com.loser.core.cache.global;


import com.loser.core.constant.ExecuteMethodEnum;
import com.loser.core.strategy.executor.MethodExecutorStrategy;
import com.loser.core.strategy.executor.impl.CountStrategy;
import com.loser.core.strategy.executor.impl.ExistStrategy;
import com.loser.core.strategy.executor.impl.GetByIdStrategy;
import com.loser.core.strategy.executor.impl.GetOneStrategy;
import com.loser.core.strategy.executor.impl.ListByIdsStrategy;
import com.loser.core.strategy.executor.impl.ListStrategy;
import com.loser.core.strategy.executor.impl.PageStrategy;
import com.loser.core.strategy.executor.impl.RemoveByIdStrategy;
import com.loser.core.strategy.executor.impl.RemoveStrategy;
import com.loser.core.strategy.executor.impl.SaveBatchStrategy;
import com.loser.core.strategy.executor.impl.SaveStrategy;
import com.loser.core.strategy.executor.impl.UpdateByIdStrategy;
import com.loser.core.strategy.executor.impl.UpdateStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法执行器缓存
 *
 * @author loser
 * @date 2024/4/28
 */
public class ExecutorProxyCache {

    public static final Map<String, MethodExecutorStrategy> EXECUTOR_MAP = new HashMap<>(ExecuteMethodEnum.values().length);

    static {
        EXECUTOR_MAP.put(ExecuteMethodEnum.GET_ONE.getMethod(), new GetOneStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.SAVE.getMethod(), new SaveStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.SAVE_BATCH.getMethod(), new SaveBatchStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.REMOVE_BY_ID.getMethod(), new RemoveByIdStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.REMOVE.getMethod(), new RemoveStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.UPDATE_BY_ID.getMethod(), new UpdateByIdStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.UPDATE.getMethod(), new UpdateStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.GET_BY_ID.getMethod(), new GetByIdStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.LIST_BY_IDS.getMethod(), new ListByIdsStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.COUNT.getMethod(), new CountStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.LIST.getMethod(), new ListStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.PAGE.getMethod(), new PageStrategy());
        EXECUTOR_MAP.put(ExecuteMethodEnum.EXIST.getMethod(), new ExistStrategy());
    }

}
