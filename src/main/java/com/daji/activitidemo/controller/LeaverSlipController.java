package com.daji.activitidemo.controller;

import com.daji.activitidemo.entity.LeaveSlip;
import com.daji.activitidemo.service.LeaveSlipService;
import com.daji.activitidemo.vo.CompleteVo;
import com.daji.activitidemo.vo.R;
import org.activiti.engine.task.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: daji
 * @Date: 2019/4/7 14:32
 */
@RestController
@RequestMapping("/leave")
public class LeaverSlipController {
    @Autowired
    private LeaveSlipService leaveSlipService;

    @PostMapping("/add")
    public R addLeaveSlip(LeaveSlip leaveSlip){
        LeaveSlip leaveSlip1 = leaveSlipService.addLeaveSlip(leaveSlip);
        return R.ok(leaveSlip1);
    }

    @GetMapping("/get/{userId}")
    public R getLeaveSlipListByUserId(@PathVariable("userId")Long userId){
        List<LeaveSlip> leaveSlipListByUserId = leaveSlipService.getLeaveSlipListByUserId(userId);
        return R.ok(leaveSlipListByUserId);
    }
    //根据 用户Id 请假单状态 查询用户的请假单列表
    @GetMapping("/get")
    public R getLeaveSlipListByUserIdAndState(Long userId,Integer status){
        List<LeaveSlip> leaveSlipListByUserIdAndState = leaveSlipService.getLeaveSlipListByUserIdAndStatus(userId, status);
        return R.ok(leaveSlipListByUserIdAndState);
    }

    //根据 taskId 完成 任务
    @PostMapping("/complete")
    public R completeMyTask(CompleteVo completeVo){
        leaveSlipService.completeMyTask(completeVo);
        return R.ok();
    }

    //根据 请假单Id 查询 历史批注消息
    @GetMapping("/comment")
    public R getCommentByLeaveSlipId(Long id){
        List<Comment> commentByLeaveSlipId = leaveSlipService.getCommentByLeaveSlipId(id);
        return R.ok(commentByLeaveSlipId);
    }
}
