/**
 * TenantLineInterceptor.java 代码解读
 * 这段代码是一个Java类，名为TenantLineInterceptor，它实现了一个名为Interceptor的接口。
 * 这个类的主要功能是在数据库操作中自动添加多租户（Tenant）相关的条件，确保数据操作时能够区分不同租户的数据。这是在多租户架构中常见的一种做法，用于保证数据隔离和安全性。
 * <p>
 * 下面是对代码中每个方法的详细解释：
 * <p>
 * 抽象方法:
 * <p>
 * getTenantId(): 返回当前操作的租户ID。
 * getTenantIdColumn(): 返回数据库中表示租户ID的列名。
 * getTenantIdFiled(): 返回实体类中表示租户ID的字段名。
 * unIgnore(Class<?> clazz): 判断给定的类是否需要应用多租户条件。如果返回true，则对该类应用多租户条件；如果返回false，则不应用。
 * 覆写的方法:
 * <p>
 * getOne, save, saveBatch, remove, update, count, list, page, exist: 这些方法都是对Interceptor接口中定义的方法的实现。
 * 它们在执行相应的数据库操作之前，会调用addTenantLineCondition方法来添加多租户条件。
 * 私有方法:
 * <p>
 * addTenantLineCondition(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz): 这个方法用于向查询包装器（LambdaQueryWrapper）中添加租户ID的条件。
 * 如果当前类需要应用多租户条件（即unIgnore返回true），则将租户ID作为条件添加到查询中。
 * 这个类的设计允许开发者通过继承TenantLineInterceptor并实现其抽象方法，来自定义多租户逻辑。这种方式使得在多租户应用中处理数据时，可以自动地、一致地应用租户隔离逻辑，从而提高代码的可维护性和安全性。
 */
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
    public Object[] lambdaUpdate(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        addTenantLineCondition(queryWrapper, clazz);
        return Interceptor.super.lambdaUpdate(queryWrapper, clazz);
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