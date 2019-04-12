package com.daji.activitidemo.service;

import com.daji.activitidemo.entity.LeaveSlip;
import com.daji.activitidemo.vo.CompleteVo;
import org.activiti.engine.task.Comment;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 14:50
 */
public interface LeaveSlipService {

    LeaveSlip addLeaveSlip(LeaveSlip leaveSlip);

    List<LeaveSlip> getLeaveSlipListByUserId(Long userId);

    List<LeaveSlip> getLeaveSlipListByUserIdAndStatus(Long userId, Integer state);

    void completeMyTask(CompleteVo completeVo);
    List<String> getOutcomeListByTaskId(String taskId);

    List<Comment> getCommentByLeaveSlipId(Long id);
}
