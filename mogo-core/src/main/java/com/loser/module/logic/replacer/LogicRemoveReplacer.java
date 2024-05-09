package com.loser.module.logic.replacer;


import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.function.replacer.Replacer;
import com.loser.global.cache.CollectionLogicDeleteCache;
import com.loser.hardcode.constant.ExecuteMethodEnum;
import com.loser.module.logic.entity.LogicDeleteResult;
import com.loser.utils.func.BoolFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 逻辑删除替换器
 *
 * @author loser
 * @date 2024/4/30
 */
public class LogicRemoveReplacer implements Replacer {

    @Override
    public Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return method.invoke(target, args);
        }
        Method newMethod = target.getClass().getDeclaredMethod(ExecuteMethodEnum.UPDATE.getMethod(), Object.class, LambdaQueryWrapper.class);
        Object entity = clazz.newInstance();
        Field field = entity.getClass().getDeclaredField(result.getFiled());
        field.setAccessible(true);
        field.set(entity, result.getLogicDeleteValue());
        return newMethod.invoke(target, build(entity, args[0]));

    }

    @Override
    public BoolFunction supplier() {
        return (proxy, target, method, args) -> CollectionLogicDeleteCache.open && method.getName().equals(ExecuteMethodEnum.REMOVE.getMethod());
    }

}
