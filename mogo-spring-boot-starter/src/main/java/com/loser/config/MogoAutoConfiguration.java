package com.loser.config;

import com.loser.hardcode.constant.MogoConstant;
import com.loser.properties.MogoDataSourceProperties;
import com.loser.properties.MogoLogicProperties;
import com.loser.utils.ExceptionUtils;
import com.loser.utils.StringUtils;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@EnableConfigurationProperties({MogoLogicProperties.class, MogoDataSourceProperties.class})
public class MogoAutoConfiguration {

    private final MogoDataSourceProperties mogoDataSourceProperties;
    private final MogoLogicProperties mogoLogicProperties;
    private final MongoTemplate mongoTemplate;
    private final Environment environment;

    public MogoAutoConfiguration(
            Environment environment,
            MongoTemplate mongoTemplate,
            MogoLogicProperties mogoLogicProperties,
            MogoDataSourceProperties mogoDataSourceProperties
    ) {
        this.mogoDataSourceProperties = mogoDataSourceProperties;
        this.mogoLogicProperties = mogoLogicProperties;
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public MogoConfiguration mogoConfiguration() {

        MogoConfiguration.instance().logic(mogoLogicProperties);
        MogoConfiguration.instance().template(MogoConstant.MASTER_DS, mongoTemplate);

        for (Map.Entry<String, MongoProperties> entry : mogoDataSourceProperties.getDatasource().entrySet()) {
            MongoTemplate template = buildTemplate(entry.getKey(), entry.getValue());
            MogoConfiguration.instance().template(entry.getKey(), template);
        }

        return MogoConfiguration.instance();

    }

    private MongoTemplate buildTemplate(String ds, MongoProperties properties) {

        MongoPropertiesClientSettingsBuilderCustomizer customizer = new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
        List<MongoClientSettingsBuilderCustomizer> builderCustomizers = Collections.singletonList(customizer);
        MongoClientSettings settings = MongoClientSettings.builder().build();
        MongoClientFactory mongoClientFactory = new MongoClientFactory(builderCustomizers);
        MongoClient mongoClient = mongoClientFactory.createMongoClient(settings);
        ListDatabasesIterable<Document> documents = mongoClient.listDatabases();
        String db = "";
        MongoCursor<Document> iterator = documents.iterator();
        // 只需要一个 使用 if
        if (iterator.hasNext()) {
            Document next = iterator.next();
            db = next.get("name").toString();
        }
        if (StringUtils.isBlank(db)) {
            throw ExceptionUtils.mpe(String.format("dynamic datasource [%s] dataBase is null", ds));
        }
        return new MongoTemplate(mongoClient, db);

    }

}
