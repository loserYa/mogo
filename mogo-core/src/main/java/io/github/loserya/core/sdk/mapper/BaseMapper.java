/**
 * BaseMapper.java 代码解读
 * 这段代码是一个Java接口定义，它定义了一个基于MongoDB数据库操作的基础映射器（Mapper）接口。这个接口包含了一系列用于数据操作的方法，适用于任何类型的实体（Entity）。下面是对这个接口中每个方法的详细解释：
 * <p>
 * getOne(LambdaQueryWrapper<T> queryWrapper):
 * <p>
 * 功能：根据提供的查询条件（LambdaQueryWrapper）查询并返回单条数据。
 * 参数：queryWrapper 是一个条件构造器，用于构建查询条件。 - 返回值：查询到的单个实体对象。
 * save(T entity):
 * <p>
 * 功能：保存一个新的实体对象到数据库。
 * 参数：entity 是需要保存的实体对象。
 * 返回值：表示保存操作是否成功的布尔值。
 * saveBatch(Collection<T> entityList): 功能：批量保存多个实体对象到数据库。
 * <p>
 * 参数：entityList 是需要保存的实体对象集合。
 * 返回值：表示批量保存操作是否成功的布尔值。
 * remove(LambdaQueryWrapper<T> queryWrapper):
 * <p>
 * 功能：根据提供的查询条件删除数据。
 * 参数：queryWrapper 是条件构造器，用于构建删除条件。
 * 返回值：表示删除操作是否成功的布尔值。
 * update(T entity, LambdaQueryWrapper<T> queryWrapper):
 * <p>
 * 功能：根据提供的查询条件更新数据。只有存在数据的字段才会被更新。
 * 参数：entity 是需要更新的数据，queryWrapper 是条件构造器。
 * 返回值：表示更新操作是否成功的布尔值。
 * count(LambdaQueryWrapper<T> queryWrapper):
 * <p>
 * 功能：根据提供的查询条件统计数据量。
 * 参数：queryWrapper 是条件构造器。
 * 返回值：满足条件的数据总数。
 * list(LambdaQueryWrapper<T> queryWrapper):
 * <p>
 * 功能：根据提供的查询条件查询数据列表。 - 参数：queryWrapper 是条件构造器。
 * 返回值：满足条件的数据列表。
 * page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize):
 * <p>
 * 功能：根据提供的查询条件进行分页查询。
 * 参数：queryWrapper 是条件构造器，pageNo 是页码，pageSize 是每页大小。
 * 返回值：分页对象，包含分页后的数据。
 * exist(LambdaQueryWrapper<T> queryWrapper):
 * <p>
 * 功能：根据提供的查询条件判断是否存在数据。参数：queryWrapper 是条件构造器。
 * 返回值：表示是否存在满足条件的数据的布尔值。
 * getTemplate():
 * <p>
 * 功能：获取MongoDB的模板对象（MongoTemplate）。
 * 返回值：MongoTemplate 实例。
 * getTargetClass():
 * <p>
 * 功能：获取目标实体类的类型。
 * 返回值：实体类的 Class 对象。
 * 这个接口为基于MongoDB的数据操作提供了一个标准化的方法集合，使得开发者可以更方便地进行数据的增删改查等操作。
 */
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
     * @param queryWrapper 条件构建器
     * @return 是否更新成功
     */
    boolean update(T entity, LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构造器更新数据 只有存在数据的字段才会更新
     *
     * @param queryWrapper 条件构建器 + 更新条件
     * @return 是否更新成功
     */
    boolean lambdaUpdate(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建器统计数据量
     *
     * @param queryWrapper 条件构建器
     * @return 数据两
     */
    long count(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建器查询列表
     *
     * @param queryWrapper 条件构建器
     * @return 数据集合
     */
    List<T> list(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过条件构建器进行分页查询
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
    Class<T> getTargetClass();

}
