package com.daji.activitidemo.repository;

import com.daji.activitidemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:11
 */
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "SELECT * from user ,user_role where `user`.id=user_role.user_id and role_id=:roleId",nativeQuery = true)
    List<User> getUserListByRoleId(@Param("roleId")Long roleId);
}
