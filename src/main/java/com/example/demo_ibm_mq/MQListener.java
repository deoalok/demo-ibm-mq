package com.example.demo_ibm_mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MQListener {
	@JmsListener(destination = "${project.mq.TOPIC_NAME}")
	public void receiveMessage(final Message jsonMessage) throws JMSException{
		String messageData = null;
		String messageData1 = null;
		String messageData2 = null;
		String messageData3 = null;
		String messageData4 = null;
		if(jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)jsonMessage;
			messageData = textMessage.getText();
			System.out.println("reached in Listner");
			System.out.println("alok ");
		}
	}
}
