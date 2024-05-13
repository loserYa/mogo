package io.github.loserya.module.interceptor.datapermission;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;

import java.util.Collection;

/**
 * 数据权限拦截器
 *
 * @author loser
 * @date 2024/5/13
 */
public abstract class DataPermissionInterceptor implements Interceptor {

    /**
     * 获取权限条件
     *
     * @return 权限条件对象
     */
    public abstract LambdaQueryWrapper<?> conditions();

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
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.getOne(queryWrapper, clazz);
    }

    @Override
    public final Object[] save(Object entity, Class<?> clazz) {
        return Interceptor.super.save(entity, clazz);
    }

    @Override
    public final Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {
        return Interceptor.super.saveBatch(entityList, clazz);
    }

    @Override
    public final Object[] remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.remove(queryWrapper, clazz);
    }

    @Override
    public final Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.update(entity, queryWrapper, clazz);
    }

    @Override
    public final Object[] count(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.count(queryWrapper, clazz);
    }

    @Override
    public final Object[] list(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.list(queryWrapper, clazz);
    }

    @Override
    public final Object[] page(LambdaQueryWrapper<?> queryWrapper, int pageNo, int pageSize, Class<?> clazz) {
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.page(queryWrapper, pageNo, pageSize, clazz);
    }

    @Override
    public final Object[] exist(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        appendCondition(queryWrapper, clazz);
        return Interceptor.super.exist(queryWrapper, clazz);
    }

    private void appendCondition(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        if (unIgnore(clazz)) {
            queryWrapper.appendCondition(conditions());
        }
    }

}
