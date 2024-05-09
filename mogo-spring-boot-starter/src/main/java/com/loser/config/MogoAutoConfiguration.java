package com.loser.config;

import com.loser.hardcode.constant.MogoConstant;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Map;

@EnableConfigurationProperties({MogoLogicProperties.class, MogoDataSourceProperties.class})
public class MogoAutoConfiguration {

    private final MogoDataSourceProperties mogoDataSourceProperties;
    private final MogoLogicProperties mogoLogicProperties;
    private final MongoTemplate mongoTemplate;

    public MogoAutoConfiguration(
            MongoTemplate mongoTemplate,
            MogoLogicProperties mogoLogicProperties,
            MogoDataSourceProperties mogoDataSourceProperties
    ) {
        this.mogoDataSourceProperties = mogoDataSourceProperties;
        this.mogoLogicProperties = mogoLogicProperties;
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public MogoConfiguration mogoConfiguration() {

        MogoConfiguration.instance().logic(mogoLogicProperties);
        MogoConfiguration.instance().template(MogoConstant.MASTER_DS, mongoTemplate);

        for (Map.Entry<String, MongoProperties> entry : mogoDataSourceProperties.getDatasource().entrySet()) {
            SimpleMongoClientDatabaseFactory databaseFactory = new SimpleMongoClientDatabaseFactory(entry.getValue().getUri());
            MogoConfiguration.instance().template(entry.getKey(), new MongoTemplate(databaseFactory));
        }

        return MogoConfiguration.instance();

    }


}
