package io.github.loser.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
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
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * mogo 初始化器
 *
 * @author loser
 * @date 2024/5/18
 */
public class MogoInitializer {

    private final MogoDataSourceProperties mogoDataSourceProperties;
    private final MongoDatabaseFactory mongoDatabaseFactory;
    private final MogoLogicProperties mogoLogicProperties;
    private final Environment environment;

    public MogoInitializer(
            Environment environment,
            MogoLogicProperties mogoLogicProperties,
            MongoDatabaseFactory mongoDatabaseFactory,
            MogoDataSourceProperties mogoDataSourceProperties
    ) {
        this.mogoDataSourceProperties = mogoDataSourceProperties;
        this.mongoDatabaseFactory = mongoDatabaseFactory;
        this.mogoLogicProperties = mogoLogicProperties;
        this.environment = environment;
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
        MogoConfiguration.instance().idGenStrategy(
                AutoStrategy.class,
                SnowStrategy.class,
                ULIDStrategy.class,
                UUIDStrategy.class
        );

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
            MongoTemplate template = buildTemplate(entry.getKey(), entry.getValue());
            MogoConfiguration.instance().template(entry.getKey(), template);
        }

    }

    private void initLogic() {
        if (!MogoEnableCache.logic) {
            return;
        }
        MogoConfiguration.instance().logic(mogoLogicProperties);
    }

    private MongoTemplate buildTemplate(String ds, MongoProperties properties) {

        MongoPropertiesClientSettingsBuilderCustomizer customizer = new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
        List<MongoClientSettingsBuilderCustomizer> builderCustomizers = Collections.singletonList(customizer);
        MongoClientSettings settings = MongoClientSettings.builder().build();
        MongoClientFactory mongoClientFactory = new MongoClientFactory(builderCustomizers);
        MongoClient mongoClient = mongoClientFactory.createMongoClient(settings);
        String db = properties.getDatabase();
        if (StringUtils.isBlank(db)) {
            String uri = properties.getUri();
            if (StringUtils.isNotBlank(uri)) {
                String[] split = uri.split("/");
                if (split.length >= 4) {
                    db = split[3].split("\\?")[0];
                }
            }
        }
        if (StringUtils.isBlank(db)) {
            throw ExceptionUtils.mpe(String.format("dynamic datasource [%s] dataBase is null", ds));
        }
        return new MongoTemplate(mongoClient, db);

    }

}
