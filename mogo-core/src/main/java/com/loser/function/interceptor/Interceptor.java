package com.loser.function.interceptor;

import com.loser.core.wrapper.LambdaQueryWrapper;

import java.util.Collection;

public interface Interceptor {

    default int order() {
        return Integer.MAX_VALUE;
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
     * 通过ID删除数据
     *
     * @param id 数据ID
     * @return 是否删除成功
     */
    default Object[] removeById(Object id, Class<?> clazz) {
        return build(id);
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
     * 通过ID更新数据 只有存在数据的字段才会更新
     *
     * @param entity 需要更新的数据
     * @return 是否更新成功
     */
    default Object[] updateById(Object entity, Class<?> clazz) {
        return build(entity);
    }

    /**
     * 通过条件构造器更新数据 只有存在数据的字段才会更新
     *
     * @param entity       需要更新的数据
     * @param queryWrapper 条件构建起
     * @return 是否更新成功
     */
    default Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        return build(entity);
    }

    /**
     * 通过ID获取数据
     *
     * @param id 数据ID
     * @return 集合中的数据
     */
    default Object[] getById(Object id, Class<?> clazz) {
        return build(id);
    }

    /**
     * 通过数据ID集合获取数据集合
     *
     * @param idList 数据ID集合
     * @return 查询到的数据集合
     */
    default Object[] listByIds(Collection<?> idList, Class<?> clazz) {
        return build(idList);
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
    default Object[] page(LambdaQueryWrapper<?> queryWrapper, int pageNo, int pageSize, Class<?> clazz) {
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
