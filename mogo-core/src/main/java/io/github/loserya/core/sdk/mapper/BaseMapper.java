package io.github.loserya.core.sdk.mapper;

import io.github.loserya.core.entity.Page;
import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseMapper<I extends Serializable, T> {

    /**
     * 查询单条数据
     *
     * @param queryWrapper 条件构造器
     * @return 查询到的集合数据
     */
    T getOne(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 保存新的数据
     *
     * @param entity 需要保存的实体
     * @return 是否保存成功
     */
    boolean save(T entity);

    /**
     * 批量保存新的数据 内部递归调用单个保存
     *
     * @param entityList 需要保存的数据列表
     * @return 是否保存成功
     */
    boolean saveBatch(Collection<T> entityList);

    /**
     * 通过条件构建器删除数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否删除成功
     */
    boolean remove(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构造器更新数据 只有存在数据的字段才会更新
     *
     * @param entity       需要更新的数据
     * @param queryWrapper 条件构建起
     * @return 是否更新成功
     */
    boolean update(T entity, LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起统计数据量
     *
     * @param queryWrapper 条件构建起
     * @return 数据两
     */
    long count(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起查询列表
     *
     * @param queryWrapper 条件构建器
     * @return 数据集合
     */
    List<T> list(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建起进行分页查询
     *
     * @param queryWrapper 条件构建器
     * @param pageNo       页面
     * @param pageSize     页面大小
     * @return 分页对象
     */
    Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize);

    /**
     * 通过条件构建起进行分页查询
     *
     * @param queryWrapper 条件构建器
     * @param pageNo       页面
     * @param pageSize     页面大小
     * @return 分页对象
     */
    Page<T> page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize);

    /**
     * 通过条件构建器判断是否存在数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否存在数据
     */
    boolean exist(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 获取 template
     *
     * @return template
     */
    MongoTemplate getTemplate();

    /**
     * 获取文档操作对象
     *
     * @return 文档操作对象
     */
    Class<T> getTragetClass();

}
