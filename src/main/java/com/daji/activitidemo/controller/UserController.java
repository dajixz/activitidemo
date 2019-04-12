package com.daji.activitidemo.controller;

import com.daji.activitidemo.entity.User;
import com.daji.activitidemo.service.UserService;
import com.daji.activitidemo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 14:00
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public R addUser(User user){
        User user1 = userService.addUser(user);
        return R.ok(user1);
    }

    @PostMapping("/edit")
    public R editUser(User user){
        User user1 = userService.editUser(user);
        return R.ok(user1);
    }
    @GetMapping("/getOne/{id}")
    public R getUserById(@PathVariable("id") Long id){
        User userById = userService.getUserById(id);
        return R.ok(userById);
    }
//
    @GetMapping("/getList/{roleId}")
    public R getUserListByRoleId(@PathVariable("roleId") Long roleId){
        List<User> userListByRoleId = userService.getUserListByRoleId(roleId);
        return R.ok(userListByRoleId);
    }
}
