package com.rz.core.queuing.configuration;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.rz.core.utils.StreamUtility;

import lombok.Data;

@Data
public class QueueSettingsSection {
    private String appId;
    private boolean enableLog;
    private List<QueueBrokerElement> queueBrokers;
    private List<QueueProducerElement> queueProducers;
    private List<QueueConsumerElement> queueConsumers;

    public static QueueSettingsSection getQueueSettings() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("/config/queuesettings.json");
        String json = StreamUtility.readFileAllText(url.getFile(), StandardCharsets.UTF_8);

        return JSON.parseObject(json, QueueSettingsSection.class);
    }
}
