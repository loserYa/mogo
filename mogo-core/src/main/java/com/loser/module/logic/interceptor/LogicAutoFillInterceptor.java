package com.loser.module.logic.interceptor;


import com.loser.function.interceptor.Interceptor;
import com.loser.global.cache.CollectionLogicDeleteCache;
import com.loser.module.logic.entity.LogicDeleteResult;
import com.loser.utils.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

/**
 * 逻辑删除默认字段拦截器(初始化逻辑未删除字段、建议方案：使用数据库默认字段 > 其次是手动设置 > 配置框架提供拦截器 > 自定义拦截器）)
 *
 * @author loser
 * @date 2024/4/30
 */
public class LogicAutoFillInterceptor implements Interceptor {

    @Override
    public Object[] save(Object entity, Class<?> clazz) {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(entity);
        }
        try {
            Field field;
            try {
                field = entity.getClass().getDeclaredField(result.getFiled());
            } catch (Exception ignore) {
                field = entity.getClass().getSuperclass().getDeclaredField(result.getFiled());
            }
            field.setAccessible(true);
            field.set(entity, result.getLogicNotDeleteValue());
        } catch (Exception e) {
            throw ExceptionUtils.mpe(e);
        }
        return build(entity);

    }

    @Override
    public Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {

        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(entityList);
        }
        Field field = null;
        for (Object entity : entityList) {
            try {
                if (Objects.isNull(field)) {
                    try {
                        field = entity.getClass().getDeclaredField(result.getFiled());
                    } catch (Exception ignore) {
                        field = entity.getClass().getSuperclass().getDeclaredField(result.getFiled());
                    }
                    field.setAccessible(true);
                }
                field.set(entity, result.getLogicNotDeleteValue());
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        return build(entityList);

    }

}
