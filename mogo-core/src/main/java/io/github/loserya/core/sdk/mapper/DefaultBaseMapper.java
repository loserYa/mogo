/**
 * DefaultBaseMapper.java 代码解读
 * 这段代码是一个Java类，名为DefaultBaseMapper，它实现了一个名为BaseMapper的接口。这个类的主要功能是提供对MongoDB数据库的基本操作，如查询、保存、删除和更新数据。下面是对这个类的主要方法和功能的详细解释：
 * <p>
 * 构造函数:
 * <p>
 * public DefaultBaseMapper(Class<T> targetClass): 这个构造函数接受一个泛型参数T的类对象。这个类对象用于指定数据库操作的目标实体类型。
 * 基本数据库操作方法:
 * <p>
 * getOne(LambdaQueryWrapper<T> queryWrapper): 根据提供的查询条件返回单个实体。
 * save(T entity): 保存一个实体到数据库。
 * saveBatch(Collection<T> entityList): 批量保存多个实体到数据库。
 * remove(LambdaQueryWrapper<T> queryWrapper): 根据提供的查询条件删除实体。
 * update(T entity, LambdaQueryWrapper<T> queryWrapper): 根据提供的查询条件和实体更新数据库中的记录。
 * count(LambdaQueryWrapper<T> queryWrapper): 计算符合查询条件的记录数。
 * list(LambdaQueryWrapper<T> queryWrapper): 返回符合查询条件的所有记录。
 * page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize): 返回分页后的记录。
 * exist(LambdaQueryWrapper<T> queryWrapper): 检查符合查询条件的记录是否存在。
 * 辅助方法: - getUpdate(T entity): 使用反射获取实体中非空字段，并构建一个更新操作。
 * <p>
 * bind(T entity, Update update, Class<?> aClass): 递归地绑定实体的字段到更新操作中。
 * getTemplate(): 获取MongoDB操作的模板对象。
 * getTragetClass(): 返回目标实体类的Class对象。
 * 其他:
 * <p>
 * 类使用了泛型，允许对不同类型的实体进行操作。
 * 使用了LambdaQueryWrapper来构建查询条件，这提供了一种灵活的方式来构建复杂的查询。
 * 使用了Spring Data MongoDB的MongoTemplate来执行实际的数据库操作。
 * 使用了反射来动态处理实体类的字段，这在更新操作中特别有用。
 * 总的来说，这个类提供了一个通用的、基于Spring Data MongoDB的数据访问层实现，它抽象了对MongoDB的常见操作，使得开发者可以更方便地进行数据库操作。
 */
package io.github.loserya.core.sdk.mapper;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.github.loserya.core.entity.Page;
import io.github.loserya.core.entity.UpdateField;
import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.utils.CollectionUtils;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.QueryBuildUtils;
import io.github.loserya.utils.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    public boolean remove(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        DeleteResult remove = getTemplate().remove(query, targetClass);
        return remove.getDeletedCount() > 0;

    }


    /**
     * 通过条件构建
     */
    private Update getUpdate(LambdaQueryWrapper<?> queryWrapper) {

        Update update = new Update();
        List<UpdateField> updateFields = queryWrapper.getCondition().getUpdateFields();
        if (CollectionUtils.isEmpty(updateFields)) {
            throw ExceptionUtils.mpe("update field is empty");
        }
        updateFields.forEach(item -> {
            switch (item.getType()) {
                case INCR:
                    update.inc(item.getCol(), (Number) item.getVal());
                    break;
                case DECR:
                    update.inc(item.getCol(), negate((Number) item.getVal()));
                    break;
                default:
                    update.set(item.getCol(), item.getVal());
            }
        });
        return update;

    }

    /**
     * 取反
     */
    private static Number negate(Number number) {
        if (number instanceof Integer) {
            return -number.intValue();
        } else if (number instanceof Double) {
            return -number.doubleValue();
        } else if (number instanceof Float) {
            return -number.floatValue();
        } else if (number instanceof Long) {
            return -number.longValue();
        } else if (number instanceof Short) {
            return -number.shortValue();
        } else if (number instanceof Byte) {
            return -number.byteValue();
        } else {
            throw ExceptionUtils.mpe("Unsupported number type: " + number.getClass());
        }
    }

    /**
     * 通过反射获取需要更新的字段
     */
    private Update getUpdate(T entity) {

        Update update = new Update();
        Class<?> aClass = entity.getClass();
        bind(entity, update, aClass);
        return update;

    }

    private void bind(T entity, Update update, Class<?> aClass) {
        if (Object.class.equals(aClass)) {
            return;
        }
        for (Field field : aClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object result = field.get(entity);
                if (Objects.nonNull(result)) {
                    update.set(field.getName(), result);
                }
            } catch (Exception ignore) {
            }
        }
        bind(entity, update, aClass.getSuperclass());
    }

    @Override
    public boolean update(T entity, LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Update update = getUpdate(entity);
        UpdateResult updateResult = getTemplate().updateMulti(query, update, targetClass);
        return updateResult.getModifiedCount() > 0;

    }

    @Override
    public boolean lambdaUpdate(LambdaQueryWrapper<T> queryWrapper) {

        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Update update = getUpdate(queryWrapper);
        UpdateResult updateResult = getTemplate().updateMulti(query, update, targetClass);
        return updateResult.getModifiedCount() > 0;

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
    public Page<T> page(LambdaQueryWrapper<T> queryWrapper, long pageNo, long pageSize) {
        Query query = QueryBuildUtils.buildQuery(queryWrapper);
        Page<T> page = new Page<>();
        page.setPageSize(pageSize);
        page.setPageNum(pageNo);
        long total = getTemplate().count(query, targetClass);
        page.setTotal(total);
        if (total <= 0) {
            return page;
        }
        query.skip((pageNo - 1) * pageSize).limit((int) pageSize);
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

        if (!MogoEnableCache.dynamicDs) {
            return MongoTemplateCache.getMongoTemplate(MogoConstant.MASTER_DS);
        }
        String ds = MongoTemplateCache.getEntityDs(getTargetClass());
        if (StringUtils.isNotBlank(ds)) {
            return MongoTemplateCache.getMongoTemplate(ds);
        }
        return MongoTemplateCache.getMongoTemplate();

    }

    @Override
    public Class<T> getTargetClass() {
        return targetClass;
    }

}
