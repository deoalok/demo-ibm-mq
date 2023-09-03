package com.example.demo_ibm_mq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.jms.MQQueueConnectionFactory;

import com.ibm.msg.client.jms.JmsConnectionFactory;

import com.ibm.msg.client.wmq.WMQConstants;
// import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;

@Configuration
public class MQConfiguration {
	@Value("${project.mq.host}")
    private String host;
    @Value("${project.mq.port}")
    private Integer port;
    @Value("${project.mq.queue-manager}")
    private String queueManager;
    @Value("${project.mq.channel}")
    private String channel;
    @Value("${project.mq.receive-timeout}")
    private long receiveTimeout;
    @Value("${project.mq.clientId}")
    private String clientId;
    @Value("${project.mq.TOPIC_NAME}")
    private String topic;
    @Value("${project.mq.username}")
    private String user;
    
    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        mqQueueConnectionFactory.setHostName(host);
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            //mqQueueConnectionFactory.setCCSID(1208);
            mqQueueConnectionFactory.setChannel(channel);
            mqQueueConnectionFactory.setPort(port);
            mqQueueConnectionFactory.setQueueManager(queueManager);
            mqQueueConnectionFactory.setClientID(clientId);
            //mqQueueConnectionFactory.setStringProperty("ObjectString",topic);
            mqQueueConnectionFactory.setStringProperty("user",user);
            mqQueueConnectionFactory.setStringProperty("password","");
            mqQueueConnectionFactory.setStringProperty("SubName","changereview_mahesh_test");
            mqQueueConnectionFactory.setStringProperty("SubLevel","1");
           // mqQueueConnectionFactory.create


        } catch (Exception e) {
            System.out.println("^^^^^^^^^^^^^^^^^"+e.getMessage());
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }



    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(MQQueueConnectionFactory mqQueueConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(mqQueueConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(500);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }
    
    @Bean
    public JmsOperations jmsOperations(CachingConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(receiveTimeout);

        return jmsTemplate;
    }


        @Bean
        public JmsListenerContainerFactory<?> topicFactory(ConnectionFactory connectionFactory,
                                                           DefaultJmsListenerContainerFactoryConfigurer configurer) {
            DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
            // This provides all boot's default to this factory, including the message converter
            configurer.configure(factory, connectionFactory);
            // You could still override some of Boot's default if necessary.
          //  factory.setConnectionFactory(connectionFactory());
            factory.setPubSubDomain(true);
            factory.setSubscriptionDurable(false);
            factory.setClientId(clientId);
            //factory.createListenerContainer()
           // factory.setDestinationResolver(destinationResolver());


            return factory;
        }

}
