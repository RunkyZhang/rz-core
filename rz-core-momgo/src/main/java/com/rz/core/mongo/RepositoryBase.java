package com.rz.core.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneOptions;
import com.mongodb.util.JSON;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.CollectibleCodec;
import org.bson.conversions.Bson;

import java.io.Serializable;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public abstract class RepositoryBase<T extends Serializable> {
    protected String rawTableName;
    protected int dataSourceNumber;
    protected MongoClientURI mongoClientUri;
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;
    protected MongoCollection mongoCollection;

    public String getRawTableName() {
        return rawTableName;
    }

    public RepositoryBase(String connectionString, String rawTableName) {
        this.rawTableName = rawTableName;
        this.mongoClientUri = new MongoClientURI(connectionString);
        this.mongoClient = new MongoClient(mongoClientUri);
        this.mongoDatabase = this.mongoClient.getDatabase(this.mongoClientUri.getDatabase());
        this.mongoCollection = this.mongoDatabase.getCollection(rawTableName);
    }

    public void insert(T po) {
        if (null == po) {
            return;
        }

        BsonDocument bsonDocument = BsonDocument.parse(JSON.serialize(po));
        Object id = bsonDocument.get("_id");
        id = bsonDocument.get("id");

        InsertOneOptions insertOneOptions = new InsertOneOptions();
        insertOneOptions.bypassDocumentValidation(false);
        this.mongoCollection.insertOne(po, insertOneOptions);
    }

    public  void update(T po){
        Filters.eq("");
        //this.mongoCollection.updateOne()
    }

    public Object getIdValue(Object po){
        BsonDocument bsonDocument = BsonDocumentWrapper.asBsonDocument(po, this.mongoClient.getMongoClientOptions().getCodecRegistry());
        Object id = bsonDocument.get("_id");
        id = bsonDocument.get("id");

        return id;
    }
}

