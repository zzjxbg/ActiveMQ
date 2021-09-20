package org.example.activemq.topic;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_Topic {

    public static final String ACTIVEMQ_URL="tcp://192.168.80.130:61616";
    public static final String TOPIC_NAME="topic01";

    public static void main(String[] args) throws JMSException, IOException {
        //1.创建连接工厂,按照给定的url地址,采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        //2.通过连接工厂获得连接connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数,第一个事务,第二个签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地(具体是队列还是主题)
        Topic topic = (Topic) session.createTopic(TOPIC_NAME);  //Collection collection = new ArrayList

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        //通过监听的方式来消费消息(异步非阻塞式监听器)
        messageConsumer.setMessageListener((message) -> {
            if (message != null && message instanceof TextMessage) {
                TextMessage textMessage =  (TextMessage) message;
                try {
                    System.out.println("*****消费者接收到消息:" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //写数据可能太快了，而后台没有收到，资源就已经关闭了
        //需要写一个system.in.read();需要抛出异常
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
