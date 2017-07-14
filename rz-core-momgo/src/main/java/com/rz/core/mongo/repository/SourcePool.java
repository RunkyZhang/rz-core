package com.rz.core.mongo.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rz.core.Assert;
import com.rz.core.Tuple2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renjie.zhang on 7/11/2017.
 */
class SourcePool {
    private static Map<String, Tuple2<MongoClient, Map<String, Tuple2<MongoDatabase, Map<String, MongoCollection>>>>> mongoSourcePool = new HashMap<>();
    private static Map<Class<?>, PoDefinition> poDefinitionPool = new HashMap<>();

    public static MongoClient getMongoClient(String connectionString) {


        return SourcePool.getOrBuildMongoClient(connectionString).getItem1();
    }

    public static MongoDatabase getMongoDatabase(String connectionString, String databaseName) {
        return SourcePool.getOrBuildMongoDatabase(connectionString, databaseName).getItem1();
    }

    public static MongoCollection getMongoCollection(String connectionString, String databaseName, String tableName) {
        Assert.isNotNull(tableName, "tableName");

        Tuple2<MongoDatabase, Map<String, MongoCollection>> tuple = getOrBuildMongoDatabase(connectionString, databaseName);

        MongoDatabase mongoDatabase = tuple.getItem1();
        Map<String, MongoCollection> mongoCollections = tuple.getItem2();

        if (!mongoCollections.containsKey(tableName)) {
            synchronized (mongoCollections) {
                if (!mongoCollections.containsKey(tableName)) {
                    mongoCollections.put(tableName, mongoDatabase.getCollection(tableName));
                }
            }
        }

        return mongoCollections.get(tableName);
    }

    public static PoDefinition getPoDefinition(Class<?> clazz) {
        Assert.isNotNull(clazz, "clazz");

        if (!SourcePool.poDefinitionPool.containsKey(clazz)) {
            synchronized (SourcePool.poDefinitionPool) {
                if (!SourcePool.poDefinitionPool.containsKey(clazz)) {
                    SourcePool.poDefinitionPool.put(clazz, new PoDefinition(clazz));
                }
            }
        }

        return SourcePool.poDefinitionPool.get(clazz);
    }

    private static Tuple2<MongoClient, Map<String, Tuple2<MongoDatabase, Map<String, MongoCollection>>>> getOrBuildMongoClient(String connectionString) {
        Assert.isNotNull(connectionString, "connectionString");

        if (!SourcePool.mongoSourcePool.containsKey(connectionString)) {
            synchronized (SourcePool.mongoSourcePool) {
                if (!SourcePool.mongoSourcePool.containsKey(connectionString)) {

                    SourcePool.mongoSourcePool.put(connectionString, new Tuple2(new MongoClient(new MongoClientURI(connectionString)), new HashMap<>()));
                }
            }
        }

        return SourcePool.mongoSourcePool.get(connectionString);
    }

    private static Tuple2<MongoDatabase, Map<String, MongoCollection>> getOrBuildMongoDatabase(String connectionString, String databaseName) {
        Assert.isNotBlank(databaseName, "databaseName");

        Tuple2<MongoClient, Map<String, Tuple2<MongoDatabase, Map<String, MongoCollection>>>> tuple = SourcePool.getOrBuildMongoClient(connectionString);

        MongoClient mongoClient = tuple.getItem1();
        Map<String, Tuple2<MongoDatabase, Map<String, MongoCollection>>> mongoDatabases = tuple.getItem2();
        if (!mongoDatabases.containsKey(databaseName)) {
            synchronized (mongoDatabases) {
                if (!mongoDatabases.containsKey(databaseName)) {
                    mongoDatabases.put(databaseName, new Tuple2(mongoClient.getDatabase(databaseName), new HashMap<>()));
                }
            }
        }

        return mongoDatabases.get(databaseName);
    }
}
