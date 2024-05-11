package io.github.loserya.module.logic.replacer;


import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.replacer.Replacer;
import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;
import io.github.loserya.module.logic.entity.LogicDeleteResult;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.func.BoolFunction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 逻辑删除替换器
 *
 * @author loser
 * @since 1.0.0
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
        Field field = ClassUtil.getField(entity.getClass(), result.getFiled());
        field.setAccessible(true);
        field.set(entity, result.getLogicDeleteValue());
        return newMethod.invoke(target, build(entity, args[0]));

    }

    @Override
    public BoolFunction supplier() {
        return (proxy, target, method, args) -> MogoEnableCache.logic && method.getName().equals(ExecuteMethodEnum.REMOVE.getMethod());
    }

}
