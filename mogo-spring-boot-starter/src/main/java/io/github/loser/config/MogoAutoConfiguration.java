package io.github.loser.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import io.github.loser.properties.MogoDataSourceProperties;
import io.github.loser.properties.MogoLogicProperties;
import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.module.fill.MetaObjectInterceptor;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;
import org.bson.Document;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoPropertiesClientSettingsBuilderCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MogoAutoConfiguration {

    private final MogoDataSourceProperties mogoDataSourceProperties;
    private final MogoLogicProperties mogoLogicProperties;
    private final Environment environment;

    public MogoAutoConfiguration(
            Environment environment,
            MogoLogicProperties mogoLogicProperties,
            MogoDataSourceProperties mogoDataSourceProperties
    ) {
        this.mogoDataSourceProperties = mogoDataSourceProperties;
        this.mogoLogicProperties = mogoLogicProperties;
        this.environment = environment;
        initLogic();
        initDynamicDatasource();
        initMetaFill();
    }

    private void initMetaFill() {
        MogoConfiguration.instance().interceptor(MetaObjectInterceptor.class);
    }

    private void initDynamicDatasource() {

        for (Map.Entry<String, MongoProperties> entry : mogoDataSourceProperties.getDatasource().entrySet()) {
            MongoTemplate template = buildTemplate(entry.getKey(), entry.getValue());
            MogoConfiguration.instance().template(entry.getKey(), template);
        }

    }

    private void initLogic() {
        MogoConfiguration.instance().logic(mogoLogicProperties);
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
