package com.daji.activitidemo.service.impl;

import com.daji.activitidemo.entity.LeaveSlip;
import com.daji.activitidemo.entity.User;
import com.daji.activitidemo.repository.LeaveSlipRepository;
import com.daji.activitidemo.service.WorkFlowService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: daji
 * @Date: 2019/4/7 18:39
 */
@Service
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private LeaveSlipRepository leaveSlipRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;

    /**部署流程定义*/
    @Override
    public void saveNewDeploye() {
        try {
            Deployment deployment = repositoryService.createDeployment()//创建一个部署对象
            .name("请假流程部署")//添加部署的名称
            .addClasspathResource("processes/leaveSlip.bpmn")
            .addClasspathResource("processes/leaveSlip.png")//从classpath的资源中加载，一次只能加载一个文件
            .deploy();//完成部署
            System.out.println("部署ID："+deployment.getId());//1
            System.out.println("部署名称："+deployment.getName());
            System.out.println("部署key："+deployment.getKey());
            System.out.println("部署Category："+deployment.getCategory());
            System.out.println("部署TenantId："+deployment.getTenantId());
            System.out.println("部署DeploymentTime："+deployment.getDeploymentTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**查询部署对象信息，对应表（act_re_deployment）*/
    @Override
    public List<Deployment> getDeploymentList() {
        List<Deployment> list = repositoryService.createDeploymentQuery()//创建部署对象查询
                .orderByDeploymenTime().asc()//
                .list();
        return list;
    }

    /**查询流程定义的信息，对应表（act_re_procdef）*/
    @Override
    public List<ProcessDefinition> getProcessDefinitionList() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
                .orderByProcessDefinitionVersion().desc()//
                .list();
        return list;
    }
    //启动流程
    public ProcessInstance startProcessInstance(LeaveSlip leaveSlip){
        //流程定义的key
        String processDefinitionKey = leaveSlip.getClass().getSimpleName();//LeaveSlip
        //使用正在执行对象表中的一个字段BUSINESS_KEY（Activiti提供的一个字段），让启动的流程（流程实例）关联业务
        String businessKey = processDefinitionKey+"."+leaveSlip.getId();
        //流程变量
        Map<String, Object> variables = new HashMap<>();
        User user = leaveSlip.getUser();
        variables.put("user",user);
        //与正在执行的流程实例和执行对象相关的Service
        //使用流程定义的key启动流程实例，key对应leaveSlip.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        //public ProcessInstance startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables)
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey,variables);
        System.out.println("流程实例ID:"+pi.getId());
        System.out.println("流程定义ID:"+pi.getProcessDefinitionId());
        return pi;
    }

    //根据用户 id 获取 当前人的任务列表
    @Override
    public List<Task> getTaskListByUserId(Long userId) {
        List<Task> list = taskService.createTaskQuery()//
//                .taskAssignee()//指定个人任务查询
                .taskCandidateOrAssigned(String.valueOf(userId))
                .orderByTaskCreateTime().desc()//
                .list();
        return list;
    }

    @Override
    public Map getVariables(String taskId) {
        Map<String, Object> variables = taskService.getVariables(taskId);
        System.out.println((User)variables.get("user"));
        return variables;
    }
}
