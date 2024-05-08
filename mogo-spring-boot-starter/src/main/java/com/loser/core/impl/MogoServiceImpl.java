package com.loser.core.impl;

import com.loser.core.cache.BaseContext;
import com.loser.core.entity.Page;
import com.loser.core.sdk.MogoService;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.utils.ClassUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 默认的服务实现类
 *
 * @author loser
 * @date 2023-02-04  18:53
 */
@SuppressWarnings("all")
public class MogoServiceImpl<I extends Serializable, T> implements MogoService<I, T> {

    /**
     * 服务类对应的mongo实体类
     */
    private final Class<T> targetClass = (Class<T>) ClassUtil.getTClass(this);

    @Resource
    protected MongoTemplate mongoTemplate;

    protected BaseMapper<I, T> baseMapper;

    @PostConstruct
    public void init() {
        this.baseMapper = BaseContext.getMapper(targetClass, mongoTemplate);
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
    public boolean saveBatch(Collection<T> entityList) {
        return baseMapper.saveBatch(entityList);
    }

    @Override
    public boolean removeById(I id) {
        return baseMapper.removeById(id);
    }

    @Override
    public boolean remove(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.remove(queryWrapper);
    }

    @Override
    public boolean updateById(T entity) {
        return baseMapper.updateById(entity);
    }

    @Override
    public boolean update(T entity, LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.update(entity, queryWrapper);
    }

    @Override
    public T getById(I id) {
        return baseMapper.getById(id);
    }

    @Override
    public Collection<T> listByIds(Collection<I> idList) {
        return baseMapper.listByIds(idList);
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
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) {
        return baseMapper.page(queryWrapper, pageNo, pageSize);
    }

    @Override
    public boolean exist(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.exist(queryWrapper);
    }

}
