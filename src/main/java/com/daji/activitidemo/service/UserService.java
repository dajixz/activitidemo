package com.daji.activitidemo.service;

import com.daji.activitidemo.entity.User;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:12
 */
public interface UserService {
    User addUser(User user);

    User editUser(User user);

    User getUserById(Long id);

    List<User> getUserListByRoleId(Long roleId);
}
