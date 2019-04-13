package com.github.chandanv89.rabbitmqdemo;

/**
 * The type Rabbit mq demo.
 */
public class RabbitMqDemo {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // create a few producers
        new Producer("p1", "first", "Msg_1", 10, 500);

        new Producer("p2", "second", "Msg_2", 5, 1000);

        new Producer("p3", "third", "Msg_3", 10, 0);

        // Create a few consumers
        Consumer c1 = new Consumer("c1", "first");
        Consumer c2 = new Consumer("c2", "first");
        Consumer c3 = new Consumer("c3", "first");
        Consumer c4 = new Consumer("c4", "second");
        Consumer c5 = new Consumer("c5", "third");
        Consumer c6 = new Consumer("c6", "third");
        Consumer c7 = new Consumer("c7", "third");

        c1.start();
        c2.start();
        c3.start();
        c4.start();
        c5.start();
        c6.start();
        c7.start();
    }

}
