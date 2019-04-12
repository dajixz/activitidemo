package com.daji.activitidemo.service.impl;

import com.daji.activitidemo.entity.User;
import com.daji.activitidemo.repository.UserRepository;
import com.daji.activitidemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @Author: daji
 * @Date: 2019/4/7 13:13
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User addUser(User user) {
        User save = userRepository.save(user);
        return save;
    }

    @Override
    @Transactional
    public User editUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getUserListByRoleId(Long roleId) {
        return userRepository.getUserListByRoleId(roleId);
    }
}
