package com.gosling.rabbit;

import com.gosling.utils.Constants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gaol on 2017/4/19
 **/
@Configuration
public class RabbitConfig {

    @Bean
    public Queue canal_InsertQueue() {
        return new Queue(Constants.Canal.CANAL_INSORUPD);
    }

    @Bean
    public Queue canal_DeleteQueue() {
        return new Queue(Constants.Canal.CANAL_DELETE);
    }

}
