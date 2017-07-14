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

    public MongoSortBuilder appand(MongoSort mongoSort) {
        Assert.isNotNull(mongoSort, "mongoSort");

        if (mongoSort.isAscending()) {
            return MongoSortBuilder.create(this, mongoSort.getFeildName(), true);
        } else {
            return MongoSortBuilder.create(this, mongoSort.getFeildName(), false);
        }
    }

    public MongoSortBuilder descending(String feildName) {
        return MongoSortBuilder.create(this, feildName, false);
    }

    public MongoSortBuilder ascending(String feildName) {
        return MongoSortBuilder.create(this, feildName, true);
    }

    public List<Bson> build() {
        return new ArrayList(this.sorts.values());
    }

    public static MongoSortBuilder createByAppand(MongoSort mongoSort) {
        Assert.isNotNull(mongoSort, "mongoSort");

        MongoSortBuilder mongoSortBuilder = new MongoSortBuilder();
        if (mongoSort.isAscending()) {
            return MongoSortBuilder.create(mongoSortBuilder, mongoSort.getFeildName(), true);
        } else {
            return MongoSortBuilder.create(mongoSortBuilder, mongoSort.getFeildName(), false);
        }
    }

    public static MongoSortBuilder createByDescending(String feildName) {
        MongoSortBuilder mongoSortBuilder = new MongoSortBuilder();

        return MongoSortBuilder.create(mongoSortBuilder, feildName, false);
    }

    public static MongoSortBuilder createByAscending(String feildName) {
        MongoSortBuilder mongoSortBuilder = new MongoSortBuilder();

        return MongoSortBuilder.create(mongoSortBuilder, feildName, true);
    }

    private static MongoSortBuilder create(MongoSortBuilder sortBuilder, String feildName, boolean isAscending) {
        Assert.isNotNull(sortBuilder, "sortBuilder");
        Assert.isNotBlank(feildName, "feildName");

        Document sort = new Document();
        if (isAscending) {
            sort.put(feildName, 1);
        } else {
            sort.put(feildName, -1);
        }
        sortBuilder.sorts.put(feildName, sort);

        return sortBuilder;
    }
}
