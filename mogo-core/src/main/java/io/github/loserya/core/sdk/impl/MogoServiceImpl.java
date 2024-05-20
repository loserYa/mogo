package io.github.loserya.core.sdk.impl;

import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.entity.Page;
import io.github.loserya.core.sdk.MogoService;
import io.github.loserya.core.sdk.mapper.BaseMapper;
import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.global.BaseMapperContext;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.module.datasource.ServiceDataSourceProxy;
import io.github.loserya.module.transaction.MogoTransaction;
import io.github.loserya.module.transaction.ServiceTransactionProxy;
import io.github.loserya.utils.AnnotationUtil;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.QueryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;

/**
 * 默认的服务实现类
 *
 * @author loser
 * @since 1.0.0
 */
@SuppressWarnings("all")
public abstract class MogoServiceImpl<I extends Serializable, T> implements MogoService<I, T>, FactoryBean {

    public MogoServiceImpl() {
        this.baseMapper = BaseMapperContext.getMapper(targetClass);
    }

    /**
     * 服务类对应的mongo实体类
     */
    private final Class<T> targetClass = (Class<T>) ClassUtil.getTClass(this);

    protected BaseMapper<I, T> baseMapper;

    @Resource
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
    public boolean saveBatch(Collection<T> entityList) {
        return baseMapper.saveBatch(entityList);
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
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) {
        return baseMapper.page(queryWrapper, pageNo, pageSize);
    }

    @Override
    public boolean exist(LambdaQueryWrapper<T> queryWrapper) {
        return baseMapper.exist(queryWrapper);
    }

    @Override
    public Object getObject() {

        if (!MogoEnableCache.base) {
            return this;
        }
        Class<?> aClass = getObjectType();
        MogoService service = this;
        boolean exist = false;
        if (MogoEnableCache.transaction) {
            exist = AnnotationUtil.isExistMethodAndFunction(aClass, MogoTransaction.class);
            if (exist) {
                service = (MogoService) Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), new ServiceTransactionProxy(service));
            }
        }
        if (MogoEnableCache.dynamicDs) {
            exist = AnnotationUtil.isExistMethodAndFunction(aClass, MongoDs.class);
            if (exist) {
                service = (MogoService) Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), new ServiceDataSourceProxy(service));
            }
        }
        return service;

    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

}
