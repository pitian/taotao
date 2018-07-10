package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemAddMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if(null!=message&& message instanceof TextMessage){
            TextMessage textMessage =(TextMessage)message;
            try {
                String itemId = textMessage.getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
