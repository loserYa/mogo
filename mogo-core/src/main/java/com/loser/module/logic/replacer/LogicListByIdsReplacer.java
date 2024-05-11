package com.loser.module.logic.replacer;


import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.core.wrapper.Wrappers;
import com.loser.function.replacer.Replacer;
import com.loser.global.cache.CollectionLogicDeleteCache;
import com.loser.global.cache.MogoEnableCache;
import com.loser.hardcode.constant.ExecuteMethodEnum;
import com.loser.hardcode.constant.MogoConstant;
import com.loser.module.logic.entity.LogicDeleteResult;
import com.loser.utils.func.BoolFunction;

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
        LambdaQueryWrapper<Object> query = Wrappers.lambdaQuery().in(MogoConstant.ID, (Collection<Object>) args[0]).eq(result.getColumn(), result.getLogicNotDeleteValue());
        return newMethod.invoke(target, build(query));

    }

    @Override
    public BoolFunction supplier() {
        return (proxy, target, method, args) -> MogoEnableCache.logic && method.getName().equals(ExecuteMethodEnum.LIST_BY_IDS.getMethod());
    }

}
