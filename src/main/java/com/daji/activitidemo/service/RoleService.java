package com.daji.activitidemo.service;

import com.daji.activitidemo.entity.Role;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:12
 */
public interface RoleService {
    Role addRole(Role role);

    Role getRoleByRoleId(Long roleId);

    List<Role> getRoleList();

}
