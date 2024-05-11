package com.loser.module.logic.replacer;


import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.core.wrapper.Wrappers;
import com.loser.function.replacer.Replacer;
import com.loser.global.cache.CollectionLogicDeleteCache;
import com.loser.global.cache.MogoEnableCache;
import com.loser.hardcode.constant.ExecuteMethodEnum;
import com.loser.hardcode.constant.MogoConstant;
import com.loser.module.logic.entity.LogicDeleteResult;
import com.loser.utils.ClassUtil;
import com.loser.utils.func.BoolFunction;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 逻辑删除替换器
 *
 * @author loser
 * @date 2024/4/30
 */
public class LogicUpdateByIdReplacer implements Replacer {

    @Override
    public Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return method.invoke(target, args);
        }
        Method newMethod = target.getClass().getDeclaredMethod(ExecuteMethodEnum.UPDATE.getMethod(), Object.class, LambdaQueryWrapper.class);
        LambdaQueryWrapper<Object> query = Wrappers.lambdaQuery().eq(MogoConstant.ID, ClassUtil.getId(args[0])).eq(result.getColumn(), result.getLogicNotDeleteValue());
        return newMethod.invoke(target, build(args[0], query));

    }

    @Override
    public BoolFunction supplier() {
        return (proxy, target, method, args) -> MogoEnableCache.logic && method.getName().equals(ExecuteMethodEnum.UPDATE_BY_ID.getMethod());
    }

}
