package org.example.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce_Topic {

    public static final String ACTIVEMQ_URL="tcp://192.168.80.130:61616";
    public static final String TOPIC_NAME="topic01";

    public static void main(String[] args) throws JMSException {

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

        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);

        //6.通过使用消息生产者生产3条消息发送到MQ队列里
        for (int i = 1;i <= 3;i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("TOPIC_NAME---" + i);
            //8.通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }
        //9.关闭资源(顺着申请倒着关闭)
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("****TOPIC_NAME消息发布到MQ完成");
    }
}
