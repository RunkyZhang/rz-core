package com.rz.core.queuing.configuration;

import com.rz.core.queuing.ReliabilityLevel;

import lombok.Data;

@Data
public class QueueConsumerElement {
    private String id;
    private String brokerId;
    private String queueName ;
    private int parallelCount;
    private String type;
    private ReliabilityLevel reliabilityLevel;
    private String errorQueueName;
}
