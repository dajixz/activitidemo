package com.daji.activitidemo.service.impl;

import com.daji.activitidemo.entity.Role;
import com.daji.activitidemo.repository.RoleRepository;
import com.daji.activitidemo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:13
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    @Transactional
    public Role addRole(Role role) {
        Role save = roleRepository.save(role);
        if(role.getParentId()!=null){
            Role role1 = roleRepository.findById(role.getParentId()).get();
            Set<Role> children = role1.getChildren();
            children.add(save);
            roleRepository.save(role1);
        }
        return save;
    }

    @Override
    public Role getRoleByRoleId(Long roleId) {
        return roleRepository.findById(roleId).get();
    }

    @Override
    public List<Role> getRoleList() {
        return roleRepository.findAll();
    }



}
