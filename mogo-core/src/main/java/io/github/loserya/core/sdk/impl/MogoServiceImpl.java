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

import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.entity.Page;
import io.github.loserya.core.sdk.MogoService;
import io.github.loserya.core.sdk.mapper.BaseMapper;
import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.core.wrapper.Wrappers;
import io.github.loserya.global.BaseMapperContext;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 默认的服务实现类
 *
 * @author loser
 * @since 1.0.0
 */
@SuppressWarnings("all")
public abstract class MogoServiceImpl<I extends Serializable, T> implements MogoService<I, T> {

    public MogoServiceImpl() {
        this.baseMapper = BaseMapperContext.getMapper(targetClass);
    }

    /**
     * 服务类对应的mongo实体类
     */
    public final Class<T> targetClass = buildClass();

    public Class<T> buildClass() {
        return (Class<T>) ClassUtil.getTClass(this, 1);
    }

    protected BaseMapper<I, T> baseMapper;

    @Autowired
    protected MogoConfiguration mogoConfiguration;

    @Override
    public MogoConfiguration getConfiguration() {
        return mogoConfiguration;
    }

    @Override
    public BaseMapper<I, T> getMapper() {
        return baseMapper;
    }

    @Override
    public MongoTemplate getTemplate() {
        return baseMapper.getTemplate();
    }

    @Override
    public T getOne(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.getOne(queryWrapper);
    }

    @Override
    public boolean save(T entity) {
        return baseMapper.save(entity);
    }

    @Override
    public boolean saveOrUpdate(T entity) {

        Serializable id = ClassUtil.getId(entity);
        if (Objects.isNull(id)) {
            return save(entity);
        } else {
            T byId = getById((I) id);
            if (Objects.isNull(byId)) {
                return save(entity);
            } else {
                return updateById(entity);
            }
        }

    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {
        return baseMapper.saveBatch(entityList);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        for (T bean : entityList) {
            saveOrUpdate(bean);
        }
        return true;
    }

    @Override
    public boolean removeById(I id) {
        return baseMapper.remove(QueryUtils.buildEq(id, targetClass));
    }

    @Override
    public boolean remove(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.remove(queryWrapper);
    }

    @Override
    public boolean updateById(T entity) {
        return baseMapper.update(entity, QueryUtils.buildEq(entity, targetClass));
    }

    @Override
    public boolean update(T entity, LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.update(entity, queryWrapper);
    }

    @Override
    public boolean update(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.lambdaUpdate(queryWrapper);
    }

    @Override
    public T getById(I id) {
        return baseMapper.getOne(QueryUtils.buildEq(id, targetClass));
    }

    @Override
    public Collection<T> listByIds(Collection<I> idList) {
        return baseMapper.list(QueryUtils.buildIn(idList, targetClass));
    }

    @Override
    public long count(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.count(queryWrapper);
    }

    @Override
    public List<T> list(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.list(queryWrapper);
    }

    @Override
    public List<T> list() {
        return baseMapper.list(Wrappers.empty());
    }

    @Override
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) {
        return baseMapper.page(queryWrapper, pageNo, pageSize);
    }

    @Override
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize) {
        return baseMapper.page(queryWrapper, pageNo, pageSize);
    }

    @Override
    public boolean exist(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.exist(queryWrapper);
    }

    @Override
    public boolean existById(I id) {
        return baseMapper.exist(QueryUtils.buildEq(id, targetClass));
    }

    @Override
    public Class<T> getTargetClass() {
        return targetClass;
    }
}
