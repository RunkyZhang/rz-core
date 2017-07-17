package com.rz.core.mongo.test;

import java.util.List;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class ConfigApplicationPo extends PoBase {
    private String id;
    private List<ConfigApplicationVersionPo> versions;

    public List<ConfigApplicationVersionPo> getVersions() {
        return versions;
    }

    public void setVersions(List<ConfigApplicationVersionPo> versions) {
        this.versions = versions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
