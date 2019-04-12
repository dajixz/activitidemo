package com.daji.activitidemo.repository;

import com.daji.activitidemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:11
 */
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role getRoleByRoleName(String roleName);
}
