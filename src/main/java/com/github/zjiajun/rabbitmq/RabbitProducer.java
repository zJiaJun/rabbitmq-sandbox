package com.github.zjiajun.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhujiajun
 * @version 1.0
 * @since 2020/6/24 21:13
 */
public class RabbitProducer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //创建一个type="direct"、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        //创建一个持久化的、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false,false, null);
        //将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String message = "Hello world!";
        //发送一条持久化的消息
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes());
        //关闭资源
        channel.close();
        connection.close();
    }



}
