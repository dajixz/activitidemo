package com.daji.activitidemo.controller;

import com.daji.activitidemo.entity.Role;
import com.daji.activitidemo.service.RoleService;
import com.daji.activitidemo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:10
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @PostMapping("/add")
    public R addRole(Role role){
        return R.ok(roleService.addRole(role));
    }
    @GetMapping("/get/{roleId}")
    public R getRoleByRoleId(@PathVariable("roleId")Long roleId){
        Role roleByRoleId = roleService.getRoleByRoleId(roleId);
        if (roleByRoleId!=null){
            return R.ok(roleByRoleId);
        }else {
            return R.error();
        }
    }
    @GetMapping("/list")
    public R getRoleList(){
        List<Role> roleList = roleService.getRoleList();
        if(roleList!=null){
            return R.ok(roleList);
        }else {
            return R.error();
        }
    }

//    @GetMapping("/list/{roleId}")
//    public R getRoleListByRoleId(@PathVariable("roleId")Long roleId){
//        Role role = roleService.getRoleListByRoleId(roleId);
//        if(role!=null){
//            return R.ok(role);
//        }else {
//            return R.error();
//        }
//    }
}
