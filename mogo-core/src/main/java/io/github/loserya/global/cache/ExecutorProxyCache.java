package io.github.loserya.global.cache;


import io.github.loserya.function.executor.MethodExecutorStrategy;
import io.github.loserya.function.executor.impl.CountStrategy;
import io.github.loserya.function.executor.impl.ExistStrategy;
import io.github.loserya.function.executor.impl.GetByIdStrategy;
import io.github.loserya.function.executor.impl.GetOneStrategy;
import io.github.loserya.function.executor.impl.ListByIdsStrategy;
import io.github.loserya.function.executor.impl.ListStrategy;
import io.github.loserya.function.executor.impl.PageStrategy;
import io.github.loserya.function.executor.impl.RemoveByIdStrategy;
import io.github.loserya.function.executor.impl.RemoveStrategy;
import io.github.loserya.function.executor.impl.SaveBatchStrategy;
import io.github.loserya.function.executor.impl.SaveStrategy;
import io.github.loserya.function.executor.impl.UpdateByIdStrategy;
import io.github.loserya.function.executor.impl.UpdateStrategy;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法执行器缓存
 *
 * @author loser
 * @since 1.0.0
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
