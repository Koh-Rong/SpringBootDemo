package com.gosling.mapper;

import com.gosling.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    User getUserById(@Param("id") Long id);

    int saveUser(User user);

    List<User> listUser();

    int updateUser(User user);

    int removeUserById(@Param("id") Long id);

}
