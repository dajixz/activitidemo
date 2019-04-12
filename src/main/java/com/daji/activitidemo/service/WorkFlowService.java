package com.daji.activitidemo.service;

import com.daji.activitidemo.entity.LeaveSlip;
import com.daji.activitidemo.vo.R;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

/**
 * @Author: daji
 * @Date: 2019/4/7 18:39
 */
public interface WorkFlowService {
    void saveNewDeploye();
    List<Deployment>  getDeploymentList();
    List<ProcessDefinition> getProcessDefinitionList();
    ProcessInstance startProcessInstance(LeaveSlip leaveSlip);

    List<Task> getTaskListByUserId(Long userId);

    Map getVariables(String taskId);



}
