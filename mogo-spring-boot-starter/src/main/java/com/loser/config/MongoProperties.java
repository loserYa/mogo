package com.loser.config;

import com.mongodb.ConnectionString;
import org.bson.UuidRepresentation;

public class MongoProperties {
    public static final int DEFAULT_PORT = 27017;
    public static final String DEFAULT_URI = "mongodb://localhost/test";
    private String host;
    private Integer port = null;
    private String uri;
    private String database;
    private String authenticationDatabase;
    private final Gridfs gridfs = new Gridfs();
    private String username;
    private char[] password;
    private String replicaSetName;
    private Class<?> fieldNamingStrategy;
    private UuidRepresentation uuidRepresentation;
    private Boolean autoIndexCreation;

    public MongoProperties() {
        this.uuidRepresentation = UuidRepresentation.JAVA_LEGACY;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getAuthenticationDatabase() {
        return this.authenticationDatabase;
    }

    public void setAuthenticationDatabase(String authenticationDatabase) {
        this.authenticationDatabase = authenticationDatabase;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getReplicaSetName() {
        return this.replicaSetName;
    }

    public void setReplicaSetName(String replicaSetName) {
        this.replicaSetName = replicaSetName;
    }

    public Class<?> getFieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public void setFieldNamingStrategy(Class<?> fieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy;
    }

    public UuidRepresentation getUuidRepresentation() {
        return this.uuidRepresentation;
    }

    public void setUuidRepresentation(UuidRepresentation uuidRepresentation) {
        this.uuidRepresentation = uuidRepresentation;
    }

    public String getUri() {
        return this.uri;
    }

    public String determineUri() {
        return this.uri != null ? this.uri : "mongodb://localhost/test";
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Gridfs getGridfs() {
        return this.gridfs;
    }

    public String getMongoClientDatabase() {
        return this.database != null ? this.database : (new ConnectionString(this.determineUri())).getDatabase();
    }

    public Boolean isAutoIndexCreation() {
        return this.autoIndexCreation;
    }

    public void setAutoIndexCreation(Boolean autoIndexCreation) {
        this.autoIndexCreation = autoIndexCreation;
    }

    public static class Gridfs {
        private String database;
        private String bucket;

        public Gridfs() {
        }

        public String getDatabase() {
            return this.database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getBucket() {
            return this.bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }
    }
}
