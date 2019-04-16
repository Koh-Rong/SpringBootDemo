package com.gosling.rabbit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by gaol on 2017/4/19
 **/
@Component
public class RabbitSender {

    private static final Logger log = LogManager.getLogger(RabbitSender.class.getName());

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String routingKey, String msg) {
        log.info("发送消息-[routingKey]:" + routingKey + ",[content]:" + msg);
        this.rabbitTemplate.convertAndSend(routingKey, msg);
    }

}
