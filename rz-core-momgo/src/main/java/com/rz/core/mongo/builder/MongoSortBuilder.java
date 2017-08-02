package com.rz.core.mongo.builder;

import com.rz.core.Assert;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

/**
 * Created by renjie.zhang on 7/13/2017.
 */
public class MongoSortBuilder {
    private Map<String, Bson> sorts;

    private MongoSortBuilder() {
        this.sorts = new HashMap<>();
    }

    public MongoSortBuilder append(MongoSort mongoSort) {
        Assert.isNotNull(mongoSort, "mongoSort");

        if (mongoSort.isAscending()) {
            return MongoSortBuilder.create(this, mongoSort.getFieldName(), true);
        } else {
            return MongoSortBuilder.create(this, mongoSort.getFieldName(), false);
        }
    }

    public MongoSortBuilder descending(String fieldName) {
        return MongoSortBuilder.create(this, fieldName, false);
    }

    public MongoSortBuilder ascending(String fieldName) {
        return MongoSortBuilder.create(this, fieldName, true);
    }

    public List<Bson> build() {
        return new ArrayList<>(this.sorts.values());
    }

    public static MongoSortBuilder createByAppend(MongoSort mongoSort) {
        Assert.isNotNull(mongoSort, "mongoSort");

        MongoSortBuilder mongoSortBuilder = new MongoSortBuilder();
        if (mongoSort.isAscending()) {
            return MongoSortBuilder.create(mongoSortBuilder, mongoSort.getFieldName(), true);
        } else {
            return MongoSortBuilder.create(mongoSortBuilder, mongoSort.getFieldName(), false);
        }
    }

    public static MongoSortBuilder createByDescending(String fieldName) {
        MongoSortBuilder mongoSortBuilder = new MongoSortBuilder();

        return MongoSortBuilder.create(mongoSortBuilder, fieldName, false);
    }

    public static MongoSortBuilder createByAscending(String fieldName) {
        MongoSortBuilder mongoSortBuilder = new MongoSortBuilder();

        return MongoSortBuilder.create(mongoSortBuilder, fieldName, true);
    }

    private static MongoSortBuilder create(MongoSortBuilder sortBuilder, String fieldName, boolean isAscending) {
        Assert.isNotNull(sortBuilder, "sortBuilder");
        Assert.isNotBlank(fieldName, "fieldName");

        Document sort = new Document();
        if (isAscending) {
            sort.put(fieldName, 1);
        } else {
            sort.put(fieldName, -1);
        }
        sortBuilder.sorts.put(fieldName, sort);

        return sortBuilder;
    }
}
