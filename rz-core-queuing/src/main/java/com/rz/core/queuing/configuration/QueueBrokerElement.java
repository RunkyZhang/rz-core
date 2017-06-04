package com.rz.core.queuing.configuration;

import lombok.Data;

@Data
public class QueueBrokerElement {
    private String id;
    private String address;
    private int port;
    private String virtualHost;
    private String userName;
    private String password;
    private int producerConnectionCount;
    private int producerChannelCount;
    private int publishTimeoutMilliseconds;
}
