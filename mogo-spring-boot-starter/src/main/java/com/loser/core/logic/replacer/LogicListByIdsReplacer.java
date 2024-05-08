package com.loser.core.logic.replacer;


import com.loser.core.cache.global.CollectionLogicDeleteCache;
import com.loser.core.constant.ExecuteMethodEnum;
import com.loser.core.logic.entity.LogicDeleteResult;
import com.loser.core.replacer.Replacer;
import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.core.wrapper.Wrappers;
import com.loser.utils.BoolFunction;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

/**
 * 逻辑删除替换器
 *
 * @author loser
 * @date 2024/4/30
 */
public class LogicListByIdsReplacer implements Replacer {

    @Override
    public Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return method.invoke(target, args);
        }
        Method newMethod = target.getClass().getDeclaredMethod(ExecuteMethodEnum.LIST.getMethod(), LambdaQueryWrapper.class);
        LambdaQueryWrapper<Object> query = Wrappers.lambdaQuery().in("_id", (Collection<Object>) args[0]).eq(result.getColumn(), result.getLogicNotDeleteValue());
        return newMethod.invoke(target, build(query));

    }

    @Override
    public BoolFunction supplier() {
        return (proxy, target, method, args) -> CollectionLogicDeleteCache.open && method.getName().equals(ExecuteMethodEnum.LIST_BY_IDS.getMethod());
    }

}
