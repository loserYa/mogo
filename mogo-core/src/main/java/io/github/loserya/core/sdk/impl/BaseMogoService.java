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
 * page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) 和 page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize): 根据查询条件和分页参数获取分页后的实体列表。
 * <p>
 * exist(LambdaQueryWrapper<T> queryWrapper): 根据查询条件判断实体是否存在。
 * <p>
 * existById(I id): 通过实体的ID判断实体是否存在。
 * <p>
 * 这个接口为操作 MongoDB 提供了一个高层次的抽象，使得开发者可以更方便地进行数据库操作。通过使用泛型和 Lambda 表达式，它提供了灵活性和强大的查询能力。
 */
package io.github.loserya.core.sdk.impl;

import io.github.loserya.utils.ClassUtil;

import java.io.Serializable;

/**
 * 兼容 mongo 的语法先把泛型放前边
 *
 * @author loser
 * @since 1.0.0
 */
public abstract class BaseMogoService<T, I extends Serializable> extends MogoServiceImpl<I, T> {

    @Override
    @SuppressWarnings("all")
    public Class<T> buildClass() {
        return (Class<T>) ClassUtil.getTClass(this, 0);
    }

}
