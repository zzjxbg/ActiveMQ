package org.example.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer {

    public static final String ACTIVEMQ_URL="tcp://1" + "92.168.80.130:61616";
    public static final String QUEUE_NAME="queue01";

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
        Queue queue = session.createQueue(QUEUE_NAME);  //Collection collection = new ArrayList

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);
        while(true) {
            TextMessage textMessage = (TextMessage) messageConsumer.receive();
            if (textMessage != null) {
                System.out.println("*****消费者接收到消息:" + textMessage.getText());
            } else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
