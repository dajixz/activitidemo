package com.daji.activitidemo.controller;

import com.daji.activitidemo.service.WorkFlowService;
import com.daji.activitidemo.vo.R;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: daji
 * @Date: 2019/4/7 18:37
 */
@RestController
@RequestMapping("/activiti")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;
    //部署
    @GetMapping("/newdeploy")
    public R newdeploy(){
        workFlowService.saveNewDeploye();
        return R.ok();
    }

    @GetMapping("/deployList")
    public R getDeployList(){
        List<Deployment> deploymentList = workFlowService.getDeploymentList();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map;
        if(deploymentList!=null&& deploymentList.size()>0){
            for(Deployment deployment:deploymentList){
                map= new HashMap<>();
                map.put("deploymentId",deployment.getId());
                map.put("deploymentName",deployment.getName());
                map.put("DeploymentTime",deployment.getDeploymentTime());
                System.out.println("部署ID："+deployment.getId());
                System.out.println("部署名称："+deployment.getName());
                System.out.println("部署DeploymentTime："+deployment.getDeploymentTime());
                list.add(map);
            }
        }
        return R.ok(list);
    }
    @GetMapping("/processDefinitionList")
    public R getProcessDefinitionList(){
        List<ProcessDefinition> processDefinitionList = workFlowService.getProcessDefinitionList();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map;
        if(processDefinitionList!=null&& processDefinitionList.size()>0){
            for(ProcessDefinition pd:processDefinitionList){
                map= new HashMap<>();
                map.put("processDefinitionId",pd.getId());
                map.put("processDefinitionName",pd.getName());
                map.put("processDefinitionKey",pd.getKey());
                map.put("processDefinitionVersion",pd.getVersion());
                map.put("processDefinitionBpmn",pd.getResourceName());
                map.put("processDefinitionPng",pd.getDiagramResourceName());
                map.put("deploymentId",pd.getDeploymentId());
                System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name属性值
                System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id属性值
                System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件:"+pd.getResourceName());
                System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
                System.out.println("部署对象ID："+pd.getDeploymentId());
                list.add(map);
            }
        }
        return R.ok(list);
    }

    //根据用户名 查询正在执行的个人任务
    @GetMapping("/taskList")
    public R getTaskListByUserId(Long userId){
        List<Task> taskList = workFlowService.getTaskListByUserId(userId);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String,Object> map;
        if(taskList!=null&& taskList.size()>0){
            for(Task task:taskList){
                map= new HashMap<>();
                map.put("taskId",task.getId());
                map.put("taskName",task.getName());
                map.put("assignee",task.getAssignee());
                list.add(map);
            }
        }
        return R.ok(list);
    }

    //获取流程变量
    @GetMapping("/variables")
    public R getVariables(String taskId){
        Map variables = workFlowService.getVariables(taskId);
        return R.ok(variables);
    }

}
