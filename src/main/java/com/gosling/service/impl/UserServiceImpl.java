package com.gosling.service.impl;

import com.alibaba.fastjson.JSON;
import com.gosling.mapper.UserMapper;
import com.gosling.model.User;
import com.gosling.service.UserService;
import com.gosling.utils.Constants;
import com.gosling.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public User getUserById(Long id) {
        String key = Constants.USER_KEY_PREFIX + id;
        String result = redisUtil.get(key);
        User user;
        if (StringUtils.isNotBlank(result)) {
            user = JSON.toJavaObject(JSON.parseObject(result), User.class);
        } else {
            user = userMapper.getUserById(id);
            redisUtil.set(key, JSON.toJSONString(user));
        }
        return user;
    }

    @Override
    public int saveUser(User user) {
        return userMapper.saveUser(user);
    }

    @Override
    public List<User> listUser() {
        return userMapper.listUser();
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int removeUserById(Long id) {
        return userMapper.removeUserById(id);
    }

}
