package com.loser.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@ConfigurationProperties("spring.data.mongodb.dynamic")
public class MogoDataSourceProperties {

    private Map<String, MongoProperties> datasource = new LinkedHashMap<>();

    public Map<String, MongoProperties> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, MongoProperties> datasource) {
        this.datasource = datasource;
    }
}
