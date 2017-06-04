package com.rz.core.queuing;

import java.io.Closeable;

public interface QueueClient extends Closeable {
    void Publish(String producerId, String message);

    void Publish(String producerId, String message, String messageId);

    void Publish(String producerId, String message, String messageId, String messageType);

    void Publish(String producerId, byte[] message);

    void Publish(String producerId, byte[] message, String messageId);

    void Publish(String producerId, byte[] message, String messageId, String messageType);
}
