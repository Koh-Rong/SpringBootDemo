package com.gosling.service;

import com.gosling.model.User;

import java.util.List;

public interface UserService {

    User getUserById(Long id);

    int saveUser(User user);

    List<User> listUser();

    int updateUser(User user);

    int removeUserById(Long id);

}
