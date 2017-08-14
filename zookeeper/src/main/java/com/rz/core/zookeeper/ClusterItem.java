package com.rz.core.zookeeper;

/**
 * Created by renjie.zhang on 8/14/2017.
 */
public class ClusterItem {
    private String path;
    private String ip;
    private long createdTime;

    public String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }


    public String getIp() {
        return ip;
    }

    void setIp(String ip) {
        this.ip = ip;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
