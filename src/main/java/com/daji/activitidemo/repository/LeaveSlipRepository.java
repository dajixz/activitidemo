package com.daji.activitidemo.repository;

import com.daji.activitidemo.entity.LeaveSlip;
import com.daji.activitidemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 14:52
 */
public interface LeaveSlipRepository extends JpaRepository<LeaveSlip,Long> {
    List<LeaveSlip> getLeaveSlipsByUser(User user);
    List<LeaveSlip> getLeaveSlipsByUserAndState(User user,Integer state);
    @Modifying
    @Query(value = "update leave_slip set state=:state where id=:id",nativeQuery = true)
    int updateStateById(@Param("state")int state,@Param("id") Long id);
}
