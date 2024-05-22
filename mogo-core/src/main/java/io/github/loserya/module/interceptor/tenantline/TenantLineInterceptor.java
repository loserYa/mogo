package io.github.loserya.module.interceptor.tenantline;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

public abstract class TenantLineInterceptor implements Interceptor {

    /**
     * 获取租户 ID 值表达式，只支持单个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    public abstract Object getTenantId();

    /**
     * 获取租户字段名
     * <p>
     *
     * @return 租户字段名
     */
    public abstract String getTenantIdColumn();

    /**
     * 获取租户字段名
     * <p>
     *
     * @return 租户实体字段
     */
    public abstract String getTenantIdFiled();

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param clazz 文档对象
     * @return 是否忽略, true:需要解析并拼接多租户条件，false:标识忽略
     */
    public abstract boolean unIgnore(Class<?> clazz);

    @Override
    public final Object[] getOne(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.getOne(queryWrapper, clazz);
    }

    @Override
    public final Object[] save(Object entity, Class<?> clazz) {
        if (unIgnore(clazz)) {
            try {
                Field field = ClassUtil.getFieldWitchCache(clazz, getTenantIdFiled());
                field.set(entity, getTenantId());
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        return Interceptor.super.save(entity, clazz);
    }

    @Override
    public final Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {
        if (unIgnore(clazz)) {
            try {
                Field field = ClassUtil.getFieldWitchCache(clazz, getTenantIdFiled());
                for (Object entity : entityList) {
                    field.set(entity, getTenantId());
                }
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }
        return Interceptor.super.saveBatch(entityList, clazz);
    }

    @Override
    public final Object[] remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.remove(queryWrapper, clazz);
    }

    @Override
    public final Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.update(entity, queryWrapper, clazz);
    }

    @Override
    public final Object[] count(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.count(queryWrapper, clazz);
    }

    private void addTenantLineCondition(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        if (unIgnore(clazz)) {
            queryWrapper.eq(getTenantIdColumn(), getTenantId());
        }
    }

    @Override
    public final Object[] list(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.list(queryWrapper, clazz);
    }

    @Override
    public final Object[] page(LambdaQueryWrapper<?> queryWrapper, Long pageNo, Long pageSize, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.page(queryWrapper, pageNo, pageSize, clazz);
    }

    @Override
    public final Object[] exist(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.exist(queryWrapper, clazz);
    }

}