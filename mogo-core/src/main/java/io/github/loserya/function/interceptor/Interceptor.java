/**
 * Interceptor.java 代码解读
 * 这段代码是一个Java接口定义，名为Interceptor，它定义了一系列的默认方法，这些方法主要用于处理数据库操作，如查询、保存、删除、更新等。
 * 这个接口似乎是为了提供一种灵活的方式来拦截和处理数据库进行参数替换操作。下面是对每个方法的详细解释：
 * <p>
 * order()
 * <p>
 * 返回一个整数，表示拦截器的执行顺序。默认返回1。
 * getOne(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)
 * <p>
 * 用于查询单条数据。接收一个条件构造器（LambdaQueryWrapper）和一个类（Class），返回查询到的数据。
 * save(Object entity, Class<?> clazz)
 * <p>
 * 用于保存新的数据。接收一个实体对象和一个类，返回保存操作的结果。
 * saveBatch(Collection<?> entityList, Class<?> clazz)
 * <p>
 * 用于批量保存数据。接收一个实体列表和一个类，返回批量保存操作的结果。
 * remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)
 * <p>
 * 用于通过条件删除数据。接收一个条件构造器和一个类，返回删除操作的结果。
 * update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)
 * <p>
 * 用于更新数据。接收一个实体对象、一个条件构造器和一个类，返回更新操作的结果。
 * count(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)
 * <p>
 * 用于统计满足条件的数据量。接收一个条件构造器和一个类，返回数据量。
 * list(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)
 * <p>
 * 用于查询数据列表。接收一个条件构造器和一个类，返回数据列表。
 * page(LambdaQueryWrapper<?> queryWrapper, Long pageNo, Long pageSize, Class<?> clazz)
 * <p>
 * 用于进行分页查询。接收一个条件构造器、页码、页面大小和一个类，返回分页对象。
 * exist(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)
 * <p>
 * 用于判断是否存在满足条件的数据。接收一个条件构造器和一个类，返回是否存在的结果。
 * build(Object... args)
 * <p>
 * 一个默认方法，用于构建操作结果。接收任意数量的参数，并原样返回。
 * 这个接口的设计允许实现类通过重写这些方法来定制数据库操作的行为，例如添加日志记录、性能监控、事务管理等功能。
 * 通过使用LambdaQueryWrapper，它提供了一种灵活的方式来构建查询条件，使得数据库操作更加灵活和强大。
 */
package io.github.loserya.function.interceptor;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;

import java.util.Collection;

public interface Interceptor {

    default int order() {
        return 1;
    }

    /**
     * 查询单条数据
     *
     * @param queryWrapper 条件构造器
     * @return 查询到的集合数据
     */
    default Object[] getOne(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(queryWrapper);
    }

    /**
     * 保存新的数据
     *
     * @param entity 需要保存的实体
     * @return 是否保存成功
     */
    default Object[] save(Object entity, Class<?> clazz) {
        return build(entity);
    }

    /**
     * 批量保存新的数据 内部递归调用单个保存
     *
     * @param entityList 需要保存的数据列表
     * @return 是否保存成功
     */
    default Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {
        return build(entityList);
    }

    /**
     * 通过条件构建器删除数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否删除成功
     */
    default Object[] remove(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(queryWrapper);
    }

    /**
     * 通过条件构造器更新数据 只有存在数据的字段才会更新
     *
     * @param entity       需要更新的数据
     * @param queryWrapper 条件构建起
     * @return 是否更新成功
     */
    default Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(entity, queryWrapper);
    }


    /**
     * 通过条件构建起统计数据量
     *
     * @param queryWrapper 条件构建起
     * @return 数据两
     */
    default Object[] count(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(queryWrapper);
    }

    /**
     * 通过条件构建起查询列表
     *
     * @param queryWrapper 条件构建器
     * @return 数据集合
     */
    default Object[] list(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(queryWrapper);
    }

    /**
     * 通过条件构建起进行分页查询
     *
     * @param queryWrapper 条件构建器
     * @param pageNo       页面
     * @param pageSize     页面大小
     * @return 分页对象
     */
    default Object[] page(LambdaQueryWrapper<?> queryWrapper, Long pageNo, Long pageSize, Class<?> clazz) {
        return build(queryWrapper, pageNo, pageSize);
    }

    /**
     * 通过条件构建器判断是否存在数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否存在数据
     */
    default Object[] exist(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(queryWrapper);
    }

    default Object[] build(Object... args) {
        return args;
    }

}
