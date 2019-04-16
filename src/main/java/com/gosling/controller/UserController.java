package com.gosling.controller;

import com.gosling.model.User;
import com.gosling.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Api(value = "UserController", description = "用户相关操作")
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户列表", notes = "查询所有信息")
    @RequestMapping(value = "/listUser", method = {RequestMethod.POST})
    public List<User> listUser(){
        return userService.listUser();
    }

    @ApiOperation(value = "根据用户ID查询", notes = "查询用户信息")
    @RequestMapping(value = "/getUserById/{id}", method = {RequestMethod.POST})
    public User getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @ApiOperation(value = "新建用户", notes = "新建用户")
    @RequestMapping(value = "/saveUser", method = {RequestMethod.POST})
    public void saveUser(@RequestBody User user){
        userService.saveUser(user);
    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
    @RequestMapping(value = "/updateUser", method = {RequestMethod.POST})
    public void updateUser(@RequestBody User user){
        userService.updateUser(user);
    }

    @ApiOperation(value = "根据ID删除用户信息", notes = "根据ID删除用户信息")
    @RequestMapping(value = "/removeUserById/{id}", method = {RequestMethod.POST})
    public void removeUserById(@PathVariable("id") Long id){
        userService.removeUserById(id);
    }


}
