/**
 * FullTableInterceptor.java 代码解读
 * 这段代码是Java语言编写的，属于一个名为FullTableInterceptor的类，这个类实现了Interceptor接口。这个类的主要作用是在数据库操作中拦截全表更新或删除操作，以防止这类操作的发生。下面是对代码的详细解释：
 * <p>
 * order方法：
 *
 * @Override public int order() {
 * return Integer.MIN_VALUE;
 * }
 * 这个方法重写了接口中的order方法，返回Integer.MIN_VALUE。这可能表示这个拦截器在拦截链中的优先级是最低的。
 * <p>
 * remove方法：
 * @Override public Object[] remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
 * if (queryWrapper.conditionsSize() == 0) {
 * throw ExceptionUtils.mpe("remove full table refuse!!!");
 * }
 * return build(queryWrapper);
 * }
 * 这个方法用于拦截删除操作。如果queryWrapper中的条件数量为0（即没有指定删除条件），则抛出异常，拒绝执行全表删除操作。否则，调用build方法来构建删除条件。
 * <p>
 * update方法：
 * @Override public Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
 * if (queryWrapper.conditionsSize() == 0) {
 * throw ExceptionUtils.mpe("update full table refuse!!!");
 * }
 * return build(entity, queryWrapper);
 * }
 * 这个方法用于拦截更新操作。同样地，如果queryWrapper中的条件数量为0，则抛出异常，拒绝执行全表更新操作。否则，调用build方法来构建更新操作。
 * <p>
 * 总结来说，FullTableInterceptor类的目的是为了防止在数据库操作中执行没有条件的全表更新或删除操作，这是一种常见的安全措施，以防止误操作导致数据丢失。
 */
package io.github.loserya.module.interceptor.fulltable;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.utils.ExceptionUtils;

public final class FullTableInterceptor implements Interceptor {

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Object[] remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        if (queryWrapper.conditionsSize() == 0) {
            throw ExceptionUtils.mpe("remove full table refuse!!!");
        }
        return build(queryWrapper);
    }

    @Override
    public Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        if (queryWrapper.conditionsSize() == 0) {
            throw ExceptionUtils.mpe("update full table refuse!!!");
        }
        return build(entity, queryWrapper);
    }

}