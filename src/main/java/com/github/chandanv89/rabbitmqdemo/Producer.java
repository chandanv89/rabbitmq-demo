package com.github.chandanv89.rabbitmqdemo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * The type Producer.
 */
public class Producer implements Runnable {
    private static final String BROKER_URI = "amqp://guest:guest@localhost";

    private String queueName;
    private String message;
    private long msgCount = 5000;
    private long waitTimeInMillis = 2500;
    private String producerName;
    private Thread t;

    /**
     * Instantiates a new Producer.
     */
    public Producer() {
        this.producerName = "default";
        this.queueName = "default";
        this.message = String.valueOf(System.currentTimeMillis());
        t = new Thread(this, this.producerName);
        t.start();
    }

    /**
     * Instantiates a new Producer.
     *
     * @param producerName     the producer name
     * @param queueName        the queue name
     * @param message          the message
     * @param msgCount         the msg count
     * @param waitTimeInMillis the wait time in millis
     */
    public Producer(String producerName, String queueName, String message, long msgCount, long waitTimeInMillis) {
        this.producerName = producerName;
        this.queueName = queueName;
        this.message = message;
        this.msgCount = msgCount;
        this.waitTimeInMillis = waitTimeInMillis;

        t = new Thread(this, this.producerName);
        t.start();
    }

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(BROKER_URI);
            factory.setConnectionTimeout(300000);

            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            channel.queueDeclare(this.queueName, true, false, false, null);

            String msg;

            for (int i = 0; i < msgCount; i++) {
                msg = message + '_' + (i + 1);
                channel.basicPublish("", this.queueName, null, msg.getBytes());
                System.out.println("[" + System.currentTimeMillis() + "] " + this.producerName + " - " + this.queueName + " - " + msg);
                Thread.sleep(waitTimeInMillis);
            }
        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | InterruptedException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
