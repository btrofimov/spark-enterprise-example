package com.example;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongo.db}")
    String mongodb;

    @Value("${mongo.host}")
    String host;

    @Override
    protected String getDatabaseName() {
        return mongodb;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(host);
    }
}
