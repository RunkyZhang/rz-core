package com.rz.core.queuing.rabbitmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BlockedListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.impl.recovery.AutorecoveringConnection;
import com.rz.core.queuing.ReliabilityLevel;
import com.rz.core.queuing.configuration.QueueBrokerElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractRabbitMqConnectionKeeper implements Closeable {
    private QueueBrokerElement queueBrokerElement;

    protected boolean isClosed;
    protected ReliabilityLevel reliabilityLevel;
    protected ConnectionFactory connectionFactory;
    protected boolean isConnectionBlocked;
    protected Connection connection;
    protected Channel channel;
    protected Object lock;

    public QueueBrokerElement getQueueBrokerElement() {
        return this.queueBrokerElement;
    }

    public Channel getChannel() throws IOException {
        if (true == this.isClosed) {
            throw new RuntimeException("The object(" + this.getClass().getName() + ") had closed.");
        }

        if (null == this.channel || false == this.channel.isOpen()) {
            synchronized (this.lock) {
                if (null == this.channel) {
                    this.channel = this.connection.createChannel();
                    if (ReliabilityLevel.HIGH == this.reliabilityLevel) {
                        this.channel.confirmSelect();
                    }
                } else {
                    if (false == this.channel.isOpen()) {
                        log.debug("The channel({this._channel.ChannelNumber}) is closed, so dispose it and try to create one.");

                        Channel channel = null;
                        try {
                            channel = this.connection.createChannel();
                        } catch (Exception e) {
                            log.error("Faield to try creating a new channcel.", e);
                        }

                        if (null != channel) {
                            // RZHelper.safeClose(this._channel);
                            this.channel = channel;
                            if (this.reliabilityLevel == ReliabilityLevel.HIGH) {
                                this.channel.confirmSelect();
                            }
                        }
                    }
                }
            }
        }

        if (true == this.isConnectionBlocked) {
            throw new RuntimeException("The connection was blocked.");
        } else {
            return this.channel;
        }
    }

    public Connection getConnection() {
        if (true == this.isClosed) {
            throw new RuntimeException("The object(" + this.getClass().getName() + ") had closed.");
        }

        return this.connection;
    }

    public AbstractRabbitMqConnectionKeeper(QueueBrokerElement queueBrokerElement, ReliabilityLevel reliabilityLevel) throws IOException, TimeoutException {
        if (null == queueBrokerElement) {
            throw new IllegalArgumentException("queueBrokerElement");
        }
        if (0 > queueBrokerElement.getPublishTimeoutMilliseconds()) {
            throw new IllegalArgumentException("[queueBrokerElement.PublishTimeoutMilliseconds] is must greater than 0 or equal 0.");
        }

        this.isClosed = false;
        this.lock = new Object();
        this.isConnectionBlocked = false;
        this.queueBrokerElement = queueBrokerElement;
        this.reliabilityLevel = reliabilityLevel;
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setAutomaticRecoveryEnabled(true);
        this.connectionFactory.setNetworkRecoveryInterval(2 * 1000);
        this.connectionFactory.setUsername(this.queueBrokerElement.getUserName());
        this.connectionFactory.setPassword(this.queueBrokerElement.getPassword());
        this.connectionFactory.setHost(this.queueBrokerElement.getAddress());
        this.connectionFactory.setPort(this.queueBrokerElement.getPort());
        this.connectionFactory.setVirtualHost(this.queueBrokerElement.getVirtualHost());
        this.connectionFactory.setConnectionTimeout(5000);
        // this.connectionFactory.setSocketWriteTimeout ( -1);

        this.connection = this.CreateConnection();
        this.connection.addBlockedListener(new BlockedListener() {
            public void handleBlocked(String reason) throws IOException {

            }

            public void handleUnblocked() throws IOException {

            }
        });
        this.connection.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {

            }
        });
        ((AutorecoveringConnection) this.connection).addRecoveryListener(new RecoveryListener() {
            public void handleRecovery(Recoverable recoverable) {

            }

            public void handleRecoveryStarted(Recoverable recoverable) {

            }
        });
    }

    protected Connection CreateConnection() throws IOException, TimeoutException {
        return this.connectionFactory.newConnection();
    }

    // protected void Connection_ConnectionBlocked(Object sender,
    // RabbitMQ.Client.Events.ConnectionBlockedEventArgs e) {
    // log.debug("The connection({sender.GetHashCode().ToString()}) is
    // blocked.");
    //
    // this._isConnectionBlocked = true;
    // }
    //
    // protected void Connection_ConnectionUnblocked(Object sender, EventArgs e)
    // {
    // log.debug("The connection({sender.GetHashCode().ToString()}) is
    // unblocked.");
    //
    // this._isConnectionBlocked = false;
    // }
    //
    // protected void Connection_Recovery(Object sender, EventArgs e) {
    // log.debug("The connection({sender.GetHashCode().ToString()}) is
    // recovered.");
    // }
    //
    // protected void Connection_ConnectionShutdown(Object sender,
    // ShutdownEventArgs e) {
    // log.debug("The connection({sender.GetHashCode().ToString()}) is
    // shutdown.");
    // }
}
