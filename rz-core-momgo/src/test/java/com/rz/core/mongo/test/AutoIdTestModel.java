package com.rz.core.mongo.test;

import org.bson.types.ObjectId;

/**
 * Created by renjie.zhang on 7/24/2017.
 */
public class AutoIdTestModel extends TestModelBase {
    private ObjectId _id;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }
}
