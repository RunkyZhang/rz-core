package com.rz.core.mongo.repository;

import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

import java.util.Map;


/**
 * Created by renjie.zhang on 7/11/2017.
 */
class UpdateMapper {
    public static Bson build(Map<String, Object> values) {
        Updates.combine(Updates.set("asd", Updates.set("dsa", 666)));

        return null;
    }
}
