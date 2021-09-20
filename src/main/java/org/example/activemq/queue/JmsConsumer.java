package org.example.activemq.queue;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer {

    public static final String ACTIVEMQ_URL="tcp://1" + "92.168.80.130:61616";
    public static final String QUEUE_NAME="queue01";

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
        Queue queue = session.createQueue(QUEUE_NAME);  //Collection collection = new ArrayList

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /**
         * 同步阻塞方式(receive())
         * 订阅者或接收者调用MessageConsumer的receive方法来接收消息,
         * receive方法在能够接收到消息之前(或超时之前)将一直阻塞
         */
//        while(true) {
//            TextMessage textMessage = (TextMessage) messageConsumer.receive();
//            if (textMessage != null) {
//                System.out.println("*****消费者接收到消息:" + textMessage.getText());
//            } else {
//                break;
//            }
//        }
//        messageConsumer.close();
//        session.close();
//        connection.close();

        //通过监听的方式来消费消息(异步非阻塞式监听器)
        messageConsumer.setMessageListener(new MessageListener() {
            @SneakyThrows
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage) {
                    TextMessage textMessage =  (TextMessage) message;
                    System.out.println("*****消费者接收到消息:" + textMessage.getText());
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
