package io.github.loserya.module.logic.interceptor;


import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.module.logic.entity.LogicDeleteResult;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

/**
 * 逻辑删除默认字段拦截器(初始化逻辑未删除字段、建议方案：使用数据库默认字段 > 其次是手动设置 > 配置框架提供拦截器 > 自定义拦截器）)
 *
 * @author loser
 * @since 1.0.0
 */
public class LogicAutoFillInterceptor implements Interceptor {

    @Override
    public Object[] save(Object entity, Class<?> clazz) {

        if (CollectionLogicDeleteCache.isClose()) {
            return build(entity);
        }
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(entity);
        }
        try {
            Field field = ClassUtil.getFieldWitchCache(entity.getClass(), result.getFiled());
            field.set(entity, result.getLogicNotDeleteValue());
        } catch (Exception e) {
            throw ExceptionUtils.mpe(e);
        }
        return build(entity);

    }

    @Override
    public Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {

        if (CollectionLogicDeleteCache.isClose()) {
            return build(entityList);
        }
        LogicDeleteResult result = CollectionLogicDeleteCache.getRes(clazz);
        if (Objects.isNull(result)) {
            return build(entityList);
        }
        Field field = null;
        for (Object entity : entityList) {
            try {
                if (Objects.isNull(field)) {
                    field = ClassUtil.getFieldWitchCache(entity.getClass(), result.getFiled());
                }
                field.set(entity, result.getLogicNotDeleteValue());
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        return build(entityList);

    }

}
