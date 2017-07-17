package com.rz.core.mongo.test;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class ConfigApplicationVersionPo extends PoBase {
    private String id;
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
