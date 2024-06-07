/**
 * MogoService.java 代码解读
 * 这段代码定义了一个名为 MogoService 的接口，它提供了一系列操作 MongoDB 数据库的方法。这个接口是泛型的，允许用户定义操作的数据类型。下面是对每个方法的详细解释：
 * <p>
 * getMapper(): 返回一个 BaseMapper 实例，用于执行数据库操作。
 * <p>
 * getTemplate(): 返回一个 `MongoTemplate实例，这是 Spring Data MongoDB 的核心类，用于执行数据库操作。
 * <p>
 * getConfiguration(): 返回一个 MogoConfiguration 实例，这可能是一个自定义的配置类，用于管理数据库配置。
 * <p>
 * getOne(LambdaQueryWrapper<T> queryWrapper): 根据提供的查询条件（LambdaQueryWrapper），查询并返回单个实体。
 * <p>
 * save(T entity): 保存一个新的实体到数据库。
 * <p>
 * saveOrUpdate(T entity): 如果实体已存在则更新，否则保存新实体。
 * <p>
 * saveBatch(Collection<T> entityList): 批量保存一系列实体。
 * <p>
 * removeById(I id): 通过实体的ID删除实体。
 * <p>
 * remove(LambdaQueryWrapper<T> queryWrapper): 根据查询条件删除实体。
 * <p>
 * updateById(T entity): 通过实体的ID更新实体。
 * <p>
 * update(T entity, LambdaQueryWrapper<T> queryWrapper): 根据查询条件更新实体。
 * <p>
 * getById(I id): 通过实体的ID获取实体。
 * <p>
 * listByIds(Collection<I> idList): 通过一组ID获取实体集合。
 * <p>
 * count(LambdaQueryWrapper<T> queryWrapper): 根据查询条件计算实体数量。
 * <p>
 * list(LambdaQueryWrapper<T> queryWrapper): 根据查询条件获取实体列表。
 * <p>
 * list(): 获取所有实体。
 * <p>
 * page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) 和 page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize):
 * 根据查询条件和分页参数获取分页后的实体列表。
 * <p>
 * exist(LambdaQueryWrapper<T> queryWrapper): 根据查询条件判断实体是否存在。
 * <p>
 * existById(I id): 通过实体的ID判断实体是否存在。
 * <p>
 * 这个接口为操作 MongoDB 提供了一个高层次的抽象，使得开发者可以更方便地进行数据库操作。通过使用泛型和 Lambda 表达式，它提供了灵活性和强大的查询能力。
 */
package io.github.loserya.core.sdk;

import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.entity.Page;
import io.github.loserya.core.sdk.mapper.BaseMapper;
import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * mongo 基础方法接口
 *
 * @author loser
 * @since 1.0.0
 */
public interface MogoService<I extends Serializable, T> {

    BaseMapper<I, T> getMapper();

    MongoTemplate getTemplate();

    MogoConfiguration getConfiguration();

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
     * 保存或者更新数据
     *
     * @param entity 需要保存或者更新实体
     * @return 是否保存或更新成功成功
     */
    boolean saveOrUpdate(T entity);

    /**
     * 批量保存新的数据 内部递归调用单个保存
     *
     * @param entityList 需要保存的数据列表
     * @return 是否保存成功
     */
    boolean saveBatch(Collection<T> entityList);

    /**
     * 通过ID删除数据
     *
     * @param id 数据ID
     * @return 是否删除成功
     */
    boolean removeById(I id);

    /**
     * 通过条件构建器删除数据
     *
     * @param queryWrapper 条件构建器
     * @return 是否删除成功
     */
    boolean remove(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过ID更新数据 只有存在数据的字段才会更新
     *
     * @param entity 需要更新的数据
     * @return 是否更新成功
     */
    boolean updateById(T entity);

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
    boolean update(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 通过ID获取数据
     *
     * @param id 数据ID
     * @return 集合中的数据
     */
    T getById(I id);

    /**
     * 通过数据ID集合获取数据集合
     *
     * @param idList 数据ID集合
     * @return 查询到的数据集合
     */
    Collection<T> listByIds(Collection<I> idList);

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
     * 查询列表
     *
     * @return 数据集合
     */
    List<T> list();

    /**
     * 通过条件构建器进行分页查询
     *
     * @param queryWrapper 条件构建器
     * @param pageNo       页面
     * @param pageSize     页面大小
     * @return 分页对象
     */
    Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize);

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
     * 通过ID判断数据是否存在
     *
     * @param id 数据 ID
     * @return 是否存在数据
     */
    boolean existById(I id);

}
