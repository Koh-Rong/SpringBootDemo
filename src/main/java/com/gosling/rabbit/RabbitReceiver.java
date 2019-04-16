package com.gosling.rabbit;

import com.alibaba.fastjson.JSON;
import com.gosling.model.User;
import com.gosling.utils.Constants;
import com.gosling.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * Created by gaol on 2017/4/19
 **/
@Component
public class RabbitReceiver {

    private static final Logger log = LogManager.getLogger(RabbitReceiver.class.getName());

    @Autowired
    private RedisUtil redisUtil;

    @RabbitListener(queues = Constants.Canal.CANAL_INSORUPD)
    public void processInsert(String content) {
        User user = JSON.toJavaObject(JSON.parseObject(content), User.class);
        if (user == null) return;
        String key = "user_" + user.getId();
        redisUtil.set(key, content);
        log.info("---->插入缓存 [key]:" + key + ",[content]:" + content);
    }

    @RabbitListener(queues = Constants.Canal.CANAL_DELETE)
    public void processDelete(String content) {
        User user = JSON.toJavaObject(JSON.parseObject(content), User.class);
        if (user == null) return;
        String key = "user_" + user.getId();
        redisUtil.delete(key);
        log.info("---->删除缓存 [key]:" + key);
    }

}
