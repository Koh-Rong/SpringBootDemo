package com.gosling.test;

import com.alibaba.fastjson.JSON;
import com.gosling.app;
import com.gosling.model.User;
import com.gosling.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = app.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void saveUser(){
        User user = new User();
        user.setName("gosling222");
        user.setAge(18L);
        user.setSex(1L);
        userService.saveUser(user);
        System.out.println("=================>" + JSON.toJSONString(user));
    }

    @Test
    public void updateUser(){
        User user = new User();
        user.setId(1L);
        user.setName("ddd1");
        user.setCreatorDate(new Date());
        int n = userService.updateUser(user);
        System.out.println("=================>" + n);
    }

}
