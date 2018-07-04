package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class TestActiveMq {
    //queue
    //Producer  按照JMS接口协议
    @Test
    public void testQueueProducer()throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test1-queue");
        MessageProducer producer = session.createProducer(queue);
        /*TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("hello my first test1-queue");*/
        TextMessage textMessage = session.createTextMessage("hello my second test1-queue");
        producer.send(textMessage);
        producer.close();
        session.close();
        connection.close();
    }
    @Test
    public void testQueueConsumer()throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("test1-queue");
        MessageConsumer consumer =session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try{
                    if(message instanceof TextMessage){
                        TextMessage textMessage = (TextMessage)message;
                        String text = textMessage.getText();
                        System.out.println(text);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        System.in.read();
        consumer.close();
        session.close();
        connection.close();
    }
    @Test
    public  void testTopicProducer()throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test1-topic");
        MessageProducer producer = session.createProducer(topic);
        TextMessage textMessage = new ActiveMQTextMessage();
        textMessage.setText("hello this is my first topic poroducer");
        producer.send(textMessage);

        producer.close();
        session.close();
        connection.close();
    }
    @Test
    public void testTopicConsumer()throws Exception{
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test1-topic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    // 取消息的内容
                    text = textMessage.getText();
                    // 第八步：打印消息。
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("topic的消费端01。。。。。");
        // 等待键盘输入
        System.in.read();
        // 第九步：关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}

