package com.rz.core.mongo.test;

import com.rz.core.mongo.annotation.MongoIndex;

import java.util.Date;
import java.util.List;

/**
 * Created by renjie.zhang on 7/24/2017.
 */
public class TestModelBase {
    private Date createdTime;
    private int age;
    @MongoIndex
    private String name;
    private TestVersion defaultVersion;
    private List<TestVersion> versions;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestVersion getDefaultVersion() {
        return defaultVersion;
    }

    public void setDefaultVersion(TestVersion defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    public List<TestVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<TestVersion> versions) {
        this.versions = versions;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
