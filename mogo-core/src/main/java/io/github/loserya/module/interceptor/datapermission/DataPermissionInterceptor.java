/**
 * DataPermissionInterceptor.java 代码解读
 * 这段代码是一个Java类，名为DataPermissionInterceptor，它实现了一个名为Interceptor的接口。
 * 这个类的主要作用是作为数据权限拦截器，用于在数据库操作中自动添加权限相关的条件，以确保数据的安全性和访问控制。下面是对代码的详细解释：
 * <p>
 * 类定义：DataPermissionInterceptor是一个抽象类，它继承自Interceptor接口。这意味着这个类提供了Interceptor接口中定义的方法的具体实现，并且可以有自己的抽象方法。
 * <p>
 * 方法 conditions()：这是一个抽象方法，用于获取权限条件。它返回一个LambdaQueryWrapper<?>类型的对象，这个对象包含了权限相关的查询条件。
 * <p>
 * 方法 unIgnore(Class<?> clazz)：这也是一个抽象方法，用于根据传入的类（代表数据库中的表）判断是否应该忽略添加多租户条件。如果返回true，则表示需要解析并拼接多租户条件；如果返回false，则表示忽略。
 * <p>
 * 重写的方法：类中重写了Interceptor接口中的多个方法，如getOne、save、saveBatch、remove、update、count、list、page和exist。这些方法在执行相应的数据库操作前，会先调用appendCondition方法来添加权限条件。
 * <p>
 * 方法 appendCondition(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)：这是一个私有方法，用于向查询包装器（LambdaQueryWrapper）中添加权限条件。它首先检查unIgnore方法的返回值，如果需要添加权限条件，则调用conditions方法获取条件并添加到查询包装器中。
 * <p>
 * 总的来说，这个类通过在数据库操作前自动添加权限条件，帮助实现数据访问的安全性和控制。这对于多租户系统或者需要细粒度访问控制的应用来说非常有用。
 */
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
    public final Object[] page(LambdaQueryWrapper<?> queryWrapper, Long pageNo, Long pageSize, Class<?> clazz) {
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
