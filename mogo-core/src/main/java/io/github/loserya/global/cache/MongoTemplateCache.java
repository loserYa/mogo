package io.github.loserya.global.cache;

import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 缓存多个数据源信息
 *
 * @author loser
 * @since 1.0.0
 */
public class MongoTemplateCache {

    private static final Log LOGGER = LogFactory.getLog(MongoTemplateCache.class);
    public static final Map<String, MongoTemplate> CACHE = new HashMap<>();
    public static final Map<Class<?>, String> ENTITY_DS_CACHE = new HashMap<>();
    public static final Map<String, MongoDatabaseFactory> FACTORY = new HashMap<>();
    public static final Map<String, MongoTransactionManager> MANAGER = new HashMap<>();

    private static final ThreadLocal<String> dataSource = new InheritableThreadLocal<>();

    public static void setDataSource(String ds) {
        dataSource.set(ds);
    }

    public static String getDataSource() {

        String ds = dataSource.get();
        if (StringUtils.isBlank(ds)) {
            ds = MogoConstant.MASTER_DS;
        }
        return ds;

    }

    public static void clear() {
        dataSource.remove();
    }

    public static MongoTemplate getMongoTemplate(String ds) {
        if (MogoEnableCache.debugLog) {
            LOGGER.info(MogoConstant.LOG_PRE + String.format("getMongoTemplate [%s]", ds));
        }
        MongoTemplate mongoTemplate = CACHE.get(ds);
        if (Objects.isNull(mongoTemplate)) {
            throw ExceptionUtils.mpe(String.format("ds: %s mongoTemplate un exist", ds));
        }
        return mongoTemplate;
    }

    public static String getEntityDs(Class<?> clazz) {

        String ds = ENTITY_DS_CACHE.get(clazz);
        if (Objects.nonNull(ds)) {
            return ds;
        }
        MongoDs mongoDs = clazz.getAnnotation(MongoDs.class);
        if (Objects.nonNull(mongoDs)) {
            ENTITY_DS_CACHE.put(clazz, mongoDs.value());
        } else {
            ENTITY_DS_CACHE.put(clazz, MogoConstant.EMPTY);
        }
        return ENTITY_DS_CACHE.get(clazz);

    }

    public static MongoTemplate getMongoTemplate() {
        return getMongoTemplate(getDataSource());
    }

    public static MongoTemplate getMaster() {

        MongoTemplate mongoTemplate = CACHE.get(MogoConstant.MASTER_DS);
        if (Objects.isNull(mongoTemplate)) {
            throw ExceptionUtils.mpe(String.format("ds: %s mongoTemplate un exist", MogoConstant.MASTER_DS));
        }
        return mongoTemplate;

    }

    public static MongoDatabaseFactory getFactory() {

        String ds = getDataSource();
        if (MogoEnableCache.debugLog) {
            LOGGER.info(MogoConstant.LOG_PRE + String.format("getFactory [%s]", ds));
        }
        MongoDatabaseFactory factory = FACTORY.get(ds);
        if (Objects.isNull(factory)) {
            throw ExceptionUtils.mpe(String.format("ds: %s factory un exist", ds));
        }
        return factory;

    }

    public static MongoDatabaseFactory getMasterFactory() {

        MongoDatabaseFactory factory = FACTORY.get(MogoConstant.MASTER_DS);
        if (Objects.isNull(factory)) {
            throw ExceptionUtils.mpe(String.format("ds: %s factory un exist", MogoConstant.MASTER_DS));
        }
        return factory;

    }

    public static MongoTransactionManager getManager() {

        String ds = getDataSource();
        MongoTransactionManager manager = MANAGER.get(ds);
        if (Objects.isNull(manager)) {
            throw ExceptionUtils.mpe(String.format("ds: %s manager un exist", ds));
        }
        return manager;

    }

    public static MongoTransactionManager getMasterManager() {

        MongoTransactionManager manager = MANAGER.get(MogoConstant.MASTER_DS);
        if (Objects.isNull(manager)) {
            throw ExceptionUtils.mpe(String.format("ds: %s manager un exist", MogoConstant.MASTER_DS));
        }
        return manager;

    }

}
