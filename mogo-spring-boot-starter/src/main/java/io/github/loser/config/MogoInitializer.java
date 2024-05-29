package io.github.loser.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.loser.properties.MogoDataSourceProperties;
import io.github.loser.properties.MogoLogicProperties;
import io.github.loserya.config.MogoConfiguration;
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
public class MogoInitializer {

    private static MogoInitializer initializer;
    private final MogoDataSourceProperties mogoDataSourceProperties;
    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final MogoLogicProperties mogoLogicProperties;
    private final MongoConverter mongoConverter;

    protected static void init(MogoLogicProperties mogoLogicProperties, MongoDatabaseFactory mongoDatabaseFactory, MogoDataSourceProperties mogoDataSourceProperties, MongoConverter mongoConverter) {
        if (Objects.nonNull(initializer)) {
            return;
        }
        synchronized (MogoInitializer.class) {
            if (Objects.nonNull(initializer)) {
                return;
            }
            initializer = new MogoInitializer(mogoLogicProperties, mongoDatabaseFactory, mogoDataSourceProperties, mongoConverter);
        }
    }

    private MogoInitializer(MogoLogicProperties mogoLogicProperties, MongoDatabaseFactory mongoDatabaseFactory, MogoDataSourceProperties mogoDataSourceProperties, MongoConverter mongoConverter) {
        this.mogoDataSourceProperties = mogoDataSourceProperties;
        this.mongoDatabaseFactory = mongoDatabaseFactory;
        this.mogoLogicProperties = mogoLogicProperties;
        this.mongoConverter = mongoConverter;
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
            MongoTemplate template = buildTemplate(entry.getKey(), entry.getValue(), mongoConverter);
            MogoConfiguration.instance().template(entry.getKey(), template);
        }

    }

    private void initLogic() {
        if (!MogoEnableCache.logic) {
            return;
        }
        MogoConfiguration.instance().logic(mogoLogicProperties);
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
