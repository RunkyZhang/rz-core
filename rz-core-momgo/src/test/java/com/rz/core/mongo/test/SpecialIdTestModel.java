package com.rz.core.mongo.test;

import com.rz.core.mongo.annotation.MongoId;
import org.bson.types.ObjectId;

/**
 * Created by renjie.zhang on 7/24/2017.
 */
public class SpecialIdTestModel extends TestModelBase {
    @MongoId
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
