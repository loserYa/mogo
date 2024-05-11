package io.github.loserya.module.logic.replacer;


import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.core.wrapper.Wrappers;
import io.github.loserya.function.replacer.Replacer;
import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.logic.entity.LogicDeleteResult;
import io.github.loserya.utils.func.BoolFunction;

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
