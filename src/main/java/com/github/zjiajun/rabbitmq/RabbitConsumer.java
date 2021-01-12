package com.github.zjiajun.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhujiajun
 * @version 1.0
 * @since 2020/6/25 09:35
 */
public class RabbitConsumer {

    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address address = new Address(IP_ADDRESS, PORT);
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        Connection connection = connectionFactory.newConnection(Collections.singletonList(address));
        Channel channel = connection.createChannel();
        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("recv message: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUEUE_NAME,consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();

    }
}
