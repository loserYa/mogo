/**
 * MogoInitializer.java 代码解读
 * 这段代码是Java编写的，属于一个名为MogoInitializer的类，主要用于初始化与MongoDB相关的配置和功能。下面是对代码的详细解释：
 * <p>
 * 类定义：MogoInitializer是一个单例类，用于初始化MongoDB的配置和功能。
 * <p>
 * 成员变量：
 * <p>
 * mogoDataSourceProperties: 存储MongoDB数据源的属性。
 * mongoDatabaseFactory: 用于创建MongoDB数据库实例的工厂。
 * mogoLogicProperties: 存储MongoDB逻辑层的属性。
 * mongoConverter: 用于MongoDB数据转换的转换器。
 * 初始化方法 (init):
 * <p>
 * 这个静态方法用于初始化MogoInitializer实例。如果实例已经存在，则不执行任何操作。
 * 构造函数：
 * <p>
 * 私有构造函数，用于初始化类的成员变量，并调用一系列初始化方法。
 * 初始化方法：
 * <p>
 * initLogic(): 初始化逻辑删除功能。
 * initDynamicDatasource(): 初始化动态数据源。
 * initMetaFill(): 初始化自定义填充功能。
 * initIdGenStrategy(): 初始化ID生成策略。
 * initBanFullTable(): 初始化禁止全表更新和删除的功能。
 * initTransaction(): 初始化事务处理。
 * 辅助方法：
 * <p>
 * buildTemplate(): 根据给定的数据源属性构建MongoTemplate实例。
 * UrlJoint内部类: 用于拼接MongoDB的URL。
 * 功能实现：
 * <p>
 * 类通过配置和初始化不同的MongoDB相关功能，如动态数据源、ID生成策略、事务处理等，以支持更复杂的应用场景。
 * 异常处理：
 * <p>
 * 在构建数据源URL时，如果数据库名称为空，则抛出异常。
 * 依赖注入：
 * <p>
 * 该类依赖于Spring框架的依赖注入，通过构造函数注入所需的依赖。
 * 单例模式：
 * <p>
 * 使用单例模式确保全局只有一个MogoInitializer实例。
 * 总的来说，这个类是MongoDB在Spring Boot应用中的一个初始化器，负责配置和初始化与MongoDB相关的各种功能和属性。
 */
package io.github.loser.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.loser.properties.MogoDataSourceProperties;
import io.github.loser.properties.MogoLogicProperties;
import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.global.BaseMapperContext;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.fill.MetaObjectInterceptor;
import io.github.loserya.module.idgen.strategy.impl.AutoStrategy;
import io.github.loserya.module.idgen.strategy.impl.SnowStrategy;
import io.github.loserya.module.idgen.strategy.impl.ULIDStrategy;
import io.github.loserya.module.idgen.strategy.impl.UUIDStrategy;
import io.github.loserya.module.interceptor.fulltable.FullTableInterceptor;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.util.Map;
import java.util.Objects;

/**
 * mogo 初始化器
 *
 * @author loser
 * @date 2024/5/18
 */
public class MogoInitializer implements BeanPostProcessor {

    private static final Log LOGGER = LogFactory.getLog(MogoInitializer.class);
    private static boolean isInit = false;
    private final MogoDataSourceProperties mogoDataSourceProperties;
    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final MogoLogicProperties mogoLogicProperties;
    private final ConfigurableListableBeanFactory beanFactory;

    public MogoInitializer(
            MogoDataSourceProperties mogoDataSourceProperties,
            MongoDatabaseFactory mongoDatabaseFactory,
            MogoLogicProperties mogoLogicProperties,
            ConfigurableListableBeanFactory beanFactory
    ) {
        this.mogoDataSourceProperties = mogoDataSourceProperties;
        this.mongoDatabaseFactory = mongoDatabaseFactory;
        this.mogoLogicProperties = mogoLogicProperties;
        this.beanFactory = beanFactory;
    }

    private void initTransaction() {

        if (!MogoEnableCache.transaction) {
            return;
        }
        MogoConfiguration.instance().factory(MogoConstant.MASTER_DS, mongoDatabaseFactory);
        for (Map.Entry<String, MongoTemplate> entry : MongoTemplateCache.CACHE.entrySet()) {
            if (!Objects.equals(entry.getKey(), MogoConstant.MASTER_DS)) {
                MongoDatabaseFactory factory = entry.getValue().getMongoDatabaseFactory();
                MogoConfiguration.instance().factory(entry.getKey(), factory);
            }
        }

    }

    private void initBanFullTable() {
        if (!MogoEnableCache.banFullTable) {
            return;
        }
        MogoConfiguration.instance().interceptor(FullTableInterceptor.class);
    }

    private void initIdGenStrategy() {

        if (!MogoEnableCache.base) {
            return;
        }
        MogoConfiguration.instance().idGenStrategy(AutoStrategy.class, SnowStrategy.class, ULIDStrategy.class, UUIDStrategy.class);

    }

    private void initMetaFill() {
        if (!MogoEnableCache.base) {
            return;
        }
        MogoConfiguration.instance().interceptor(MetaObjectInterceptor.class);
    }

    private void initDynamicDatasource() {

        if (!MogoEnableCache.dynamicDs) {
            return;
        }
        for (Map.Entry<String, MongoProperties> entry : mogoDataSourceProperties.getDatasource().entrySet()) {
            MongoTemplate template = buildTemplate(entry.getKey(), entry.getValue(), null);
            MogoConfiguration.instance().template(entry.getKey(), template);
        }
        applySpringManageMongoTemplate();

    }

    private void applySpringManageMongoTemplate() {

        for (Map.Entry<String, MongoTemplate> entry : MongoTemplateCache.CACHE.entrySet()) {
            String key = entry.getKey();
            if (!key.equals(MogoConstant.MASTER_DS)) {
                String beanName = StringUtils.firstToLowerCase(key + MogoConstant.MONGO_TEMPLATE);
                if (!beanFactory.containsBean(beanName)) {
                    beanFactory.registerSingleton(beanName, entry.getValue());
                }
            }
        }

    }

    private void initLogic() {
        if (!MogoEnableCache.logic) {
            return;
        }
        MogoConfiguration.instance().logic(mogoLogicProperties);
        for (Class<?> clazz : BaseMapperContext.getMapper().keySet()) {
            MogoConfiguration.instance().mapperLogic(clazz);
        }
    }

    private MongoTemplate buildTemplate(String ds, MongoProperties properties, MongoConverter converter) {

        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder().applyConnectionString(new ConnectionString(new UrlJoint(properties).jointMongoUrl())).build());
        String database = properties.getDatabase();
        if (StringUtils.isBlank(database)) {
            String uri = properties.getUri();
            if (StringUtils.isNotBlank(uri)) {
                String[] split = uri.split("/");
                if (split.length >= 4) {
                    database = split[3].split("\\?")[0];
                }
            }
        }
        if (StringUtils.isBlank(database)) {
            throw ExceptionUtils.mpe(String.format("dynamic datasource [%s] dataBase is null", ds));
        }
        SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient, database);
        return new MongoTemplate(factory, converter);

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (isInit) {
            return bean;
        }
        synchronized (MogoInitializer.class) {
            if (isInit) {
                return bean;
            }
            long start = System.currentTimeMillis();
            // 01 初始化逻辑删除
            initLogic();
            // 02 初始化动态数据源
            initDynamicDatasource();
            // 03 初始化自定填充
            initMetaFill();
            // 04 初始化ID生成
            initIdGenStrategy();
            // 05 初始化禁止全表跟新及删除
            initBanFullTable();
            // 06 初始化事务
            initTransaction();
            long end = System.currentTimeMillis();
            isInit = true;
            LOGGER.info(MogoConstant.LOG_PRE + "mogo init finish cost: " + (end - start));
        }
        return bean;

    }

    public static class UrlJoint {

        private final MongoProperties mongoProperties;

        public UrlJoint(MongoProperties mongoProperties) {
            this.mongoProperties = mongoProperties;
        }

        public String jointMongoUrl() {
            if (StringUtils.isNotBlank(mongoProperties.getUri())) {
                return mongoProperties.getUri();
            }
            StringBuilder urlBuilder = new StringBuilder("mongodb://");

            // 如果有用户名和密码，则添加认证信息
            if (mongoProperties.getUsername() != null && !mongoProperties.getUsername().isEmpty()) {
                urlBuilder.append(mongoProperties.getUsername());
                if (mongoProperties.getPassword() != null) {
                    urlBuilder.append(':').append(new String(mongoProperties.getPassword()));
                }
                urlBuilder.append('@');
            }

            // 添加主机和端口
            urlBuilder.append(mongoProperties.getHost());
            if (mongoProperties.getPort() != null) {
                urlBuilder.append(':').append(mongoProperties.getPort());
            }

            // 添加数据库名称
            if (mongoProperties.getDatabase() != null && !mongoProperties.getDatabase().isEmpty()) {
                urlBuilder.append('/').append(mongoProperties.getDatabase());
            }

            // 返回构建的 URL
            return urlBuilder.toString();
        }

    }
}
