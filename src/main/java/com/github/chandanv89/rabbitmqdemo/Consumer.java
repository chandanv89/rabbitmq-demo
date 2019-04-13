package com.github.chandanv89.rabbitmqdemo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

/**
 * The type Consumer.
 */
public class Consumer {
    private static final String QUEUE_NAME = "first-queue";
    private static final String BROKER_URI = "amqp://guest:guest@localhost";

    private String queueName;
    private String consumerName;

    /**
     * Instantiates a new Consumer.
     *
     * @param queueName the queue name
     */
    public Consumer(String consumerName, String queueName) {
        this.consumerName = consumerName;
        this.queueName = queueName;
    }

    /**
     * Start boolean.
     *
     * @return the boolean
     */
    public boolean start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(BROKER_URI);
            factory.setConnectionTimeout(300000);

            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            channel.queueDeclare(this.queueName, true, false, false, null);

            channel.basicConsume(this.queueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    String msg = new String(body, StandardCharsets.UTF_8);
                    System.out.println("[" + System.currentTimeMillis() + "] " + consumerName + " - " + queueName + " - " + msg);
                }
            });
        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws KeyManagementException   the key management exception
     * @throws URISyntaxException       the uri syntax exception
     * @throws IOException              the io exception
     * @throws TimeoutException         the timeout exception
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(BROKER_URI);
        factory.setConnectionTimeout(300000);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Consumer received the message: " + msg);
            }
        });
    }
}
