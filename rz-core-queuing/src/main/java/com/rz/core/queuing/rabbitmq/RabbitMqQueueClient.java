package com.rz.core.queuing.rabbitmq;

import com.rz.core.queuing.QueueClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class RabbitMqQueueClient implements QueueClient {
    private boolean isClosed;
//    private Map<String, RabbitMqProducerConnectionKeeper> _rabbitMqProducerConnectionKeepers;
//    private Map<String, RabbitMqQueueProducer> _rabbitMqQueueProducers;
    
    @Override
    public void close() {
        if (!this.isClosed)
        {
            this.isClosed = true;
        }
    }

    @Override
    public void Publish(String producerId, String message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Publish(String producerId, String message, String messageId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Publish(String producerId, String message, String messageId, String messageType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Publish(String producerId, byte[] message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Publish(String producerId, byte[] message, String messageId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void Publish(String producerId, byte[] message, String messageId, String messageType) {
        // TODO Auto-generated method stub
        
    }
}
