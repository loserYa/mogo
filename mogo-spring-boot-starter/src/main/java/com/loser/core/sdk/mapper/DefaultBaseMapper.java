package com.loser.core.sdk.mapper;

import com.loser.core.cache.global.MongoTemplateCache;
import com.loser.core.constant.MogoConstant;
import com.loser.core.entity.Page;
import com.loser.core.wrapper.LambdaQueryWrapper;
import com.loser.utils.ClassUtil;
import com.loser.utils.QueryBuildUtils;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DefaultBaseMapper<I extends Serializable, T> implements BaseMapper<I, T> {

    private final Class<T> targetClass;

    public DefaultBaseMapper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public T getOne(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return getTemplate().findOne(query, targetClass);

    }

    @Override
    public boolean save(T entity) {
        getTemplate().save(entity);
        return true;
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {

        entityList.forEach(getTemplate()::save);
        return true;

    }

    @Override
    public boolean removeById(I id) {

        Criteria criteria = Criteria.where(MogoConstant.ID).is(id);
        Query query = new Query(criteria);
        DeleteResult deleteResult = getTemplate().remove(query, targetClass);
        return deleteResult.getDeletedCount() > 0;

    }

    @Override
    public boolean remove(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        DeleteResult remove = getTemplate().remove(query, targetClass);
        return remove.getDeletedCount() > 0;

    }

    @Override
    public boolean updateById(T entity) {

        Criteria criteria = Criteria.where(MogoConstant.ID).is(ClassUtil.getId(entity));
        Query query = new Query(criteria);
        Update update = getUpdate(entity);
        UpdateResult updateResult = getTemplate().updateFirst(query, update, targetClass);
        return updateResult.getModifiedCount() > 0;

    }

    /**
     * 通过反射获取需要更新的字段
     */
    private Update getUpdate(T entity) {

        Update update = new Update();
        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object result = field.get(entity);
                if (Objects.nonNull(result)) {
                    update.set(field.getName(), result);
                }
            } catch (Exception ignore) {
            }
        }
        return update;

    }

    @Override
    public boolean update(T entity, LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Update update = getUpdate(entity);
        UpdateResult updateResult = getTemplate().updateFirst(query, update, targetClass);
        return updateResult.getModifiedCount() > 0;

    }

    @Override
    public T getById(I id) {

        Criteria criteria = Criteria.where(MogoConstant.ID).is(id);
        Query query = new Query(criteria);
        return getTemplate().findOne(query, targetClass);

    }

    @Override
    public Collection<T> listByIds(Collection<I> idList) {

        Criteria criteria = Criteria.where(MogoConstant.ID).in(idList);
        Query query = new Query(criteria);
        return getTemplate().find(query, targetClass);

    }

    @Override
    public long count(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return getTemplate().count(query, targetClass);

    }

    @Override
    public List<T> list(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return getTemplate().find(query, targetClass);

    }

    @Override
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, int pageNo, int pageSize) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Page<T> page = new Page<>();
        page.setPageSize(pageSize);
        page.setPageNum(pageNo);
        long total = getTemplate().count(query, targetClass);
        page.setTotal(total);
        if (total <= 0) {
            return page;
        }
        query.skip((long) (pageNo - 1) * pageSize).limit(pageSize);
        List<T> list = getTemplate().find(query, targetClass);
        page.setRecords(list);
        return page;

    }

    @Override
    public boolean exist(LambdaQueryWrapper<T> queryWrapper) {
        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        return getTemplate().exists(query, targetClass);
    }

    @Override
    public MongoTemplate getTemplate() {
        return MongoTemplateCache.CACHE.get(MongoTemplateCache.getDataSource());
    }

    @Override
    public Class<T> getTragetClass() {
        return targetClass;
    }

}
