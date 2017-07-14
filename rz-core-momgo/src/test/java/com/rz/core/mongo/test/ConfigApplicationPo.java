package com.rz.core.mongo.test;

import java.util.List;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class ConfigApplicationPo extends PoBase {
    private List<ConfigApplicationVersionPo> versions;

    public List<ConfigApplicationVersionPo> getVersions() {
        return versions;
    }

    public void setVersions(List<ConfigApplicationVersionPo> versions) {
        this.versions = versions;
    }
}
