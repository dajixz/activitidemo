package com.daji.activitidemo.service.impl;

import com.daji.activitidemo.entity.LeaveSlip;
import com.daji.activitidemo.entity.User;
import com.daji.activitidemo.repository.LeaveSlipRepository;
import com.daji.activitidemo.repository.UserRepository;
import com.daji.activitidemo.service.LeaveSlipService;
import com.daji.activitidemo.service.WorkFlowService;
import com.daji.activitidemo.utils.State;
import com.daji.activitidemo.vo.CompleteVo;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import java.util.*;

/**
 * @Author: daji
 * @Date: 2019/4/7 14:51
 */
@Service
public class LeaveSlipServiceImpl implements LeaveSlipService {
    @Autowired
    private LeaveSlipRepository leaveSlipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Override
    @Transactional
    //添加请假单记录，同时开启请假流程，使业务与流程关联
    public LeaveSlip addLeaveSlip(LeaveSlip leaveSlip) {
        LeaveSlip save = leaveSlipRepository.save(leaveSlip);
        workFlowService.startProcessInstance(save);
        return save;
    }

    //根据用户ID 查询该用户的请假单列表
    @Override
    @Transactional
    public List<LeaveSlip> getLeaveSlipListByUserId(Long userId) {
        User user = userRepository.findById(userId).get();
        return leaveSlipRepository.getLeaveSlipsByUser(user);
    }

    @Override
    @Transactional
    //请假单状态 0初始录入,1.审核中,2为审批完成
    public List<LeaveSlip> getLeaveSlipListByUserIdAndStatus(Long userId, Integer status) {
        User user = userRepository.findById(userId).get();
        /*
        * status==0 为个人任务
        * 如果为普通用户 则 查询 自己的初始录入 的请假单列表
        * 如果为审核人员(包含辅导员,院管理员) 则 查询 自己的初始录入 的请假单列表 同时 加上自己要审核的 任务的请假单列表
        * */
        if(status==0){
            List<LeaveSlip> leaveSlipList = new ArrayList<>();
            List<Task> taskList = workFlowService.getTaskListByUserId(userId);
            if(taskList!=null && taskList.size()>0){
                for(Task task:taskList){
                    String taskId = task.getId();
                    LeaveSlip leaveSlip = getLeaveSlipByTaskId(taskId);
                    leaveSlip.setTaskId(taskId);
                    leaveSlip.setTaskName(task.getName());
                    List<String> outcomeListByTaskId = this.getOutcomeListByTaskId(taskId);
                    leaveSlip.setOptions(outcomeListByTaskId);
                    leaveSlipList.add(leaveSlip);
                }
            }
            return leaveSlipList;
        }
        //status==1 审批中 查询 自己的 审核中的请假单列表
        //status==2 审批完成 查询自己的审批完成的请假单列表
        else {
            List<LeaveSlip> leaveSlipList = leaveSlipRepository.getLeaveSlipsByUserAndState(user, status);
            return leaveSlipList;
        }
//        //status==1 审批中 查询 自己的 审核中的请假单列表
//        if(status==1){
//            List<LeaveSlip> leaveSlipList = leaveSlipRepository.getLeaveSlipsByUserAndState(user, status);
//            return leaveSlipList;
//        }
//        //status==2 审批完成 查询自己的审批完成的请假单列表
//        if(status==2){
//            List<LeaveSlip> leaveSlipList = leaveSlipRepository.getLeaveSlipsByUserAndState(user, status);
//            return leaveSlipList;
//        }

    }

    @Override
    @Transactional
    public void completeMyTask(CompleteVo completeVo) {
        //1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
        //使用任务ID，查询任务对象，获取流程流程实例ID
        String taskId = completeVo.getTaskId();
        String outcome = completeVo.getOutcome();
        String msg = completeVo.getMsg();
        Long userId = completeVo.getUserId();
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        String taskName = task.getName();
        LeaveSlip leaveSlipByTaskId = getLeaveSlipByTaskId(taskId);
        claim(taskId,userId);
        if("删除".equals(outcome)){
            leaveSlipRepository.delete(leaveSlipByTaskId);
        }else if("提交".equals(outcome)){
            leaveSlipRepository.updateStateById(State.FUDAOYUAN.getCode(),leaveSlipByTaskId.getId());
        }else if("批准".equals(outcome)){
            if("辅导员审批".equals(taskName)){
                leaveSlipRepository.updateStateById(State.YUGUANLIYUAN.getCode(),leaveSlipByTaskId.getId());
            }else if("院管理员审批".equals(taskName)){
                leaveSlipRepository.updateStateById(State.END.getCode(),leaveSlipByTaskId.getId());
            }
        }else if("驳回".equals(outcome)){
            if("辅导员审批".equals(taskName)){
                leaveSlipRepository.updateStateById(State.BACKFROMFUDAOYUAN.getCode(),leaveSlipByTaskId.getId());
            }else if("院管理员审批".equals(taskName)){
                leaveSlipRepository.updateStateById(State.BACKFROMYUANGUANLIYUAN.getCode(),leaveSlipByTaskId.getId());
            }
        }
        if(msg!=null&& msg!=""){
            //获取流程实例ID
            String processInstanceId = task.getProcessInstanceId();
            /**
             * 注意：添加批注的时候，由于Activiti底层代码是使用：
             * 		String userId = Authentication.getAuthenticatedUserId();
             CommentEntity comment = new CommentEntity();
             comment.setUserId(userId);
             所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，不过不添加审核人，该字段为null
             所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
             * */

            Authentication.setAuthenticatedUserId(String.valueOf(userId));
            taskService.addComment(taskId, processInstanceId, msg);
        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("outcome",outcome);
        taskService.complete(taskId,variables);

    }
    /**拾取任务，将组任务分给个人任务，指定任务的办理人字段*/
    private void claim(String taskId,Long userId){
        //将组任务分配给个人任务
        //分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
        taskService.claim(taskId, String.valueOf(userId));
    }
    @Override
    //根据任务ID 获取 当前任务完成后的连线
    public List<String> getOutcomeListByTaskId(String taskId){
        List<String> options = new ArrayList<>();
        Process process=null;
        //1：使用任务ID，查询任务对象Task
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2 获取流程定义Id
        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 1. 在6.0中org.activiti.engine.impl.pvm整个包被移除，意味着所有ActivitiImpl, ProcessDefinitionImpl, ExecutionImpl, TransitionImpl 都不可用了。
        //所有的流程定义有关的信息都可以通过BpmnModel来获得，
        // 获得 BpmnModel的方式可以通过org.activiti.engine.impl.util.ProcessDefinitionUtil来拿到。
        List<Process> processes = bpmnModel.getProcesses();
        //获取对应key值的process
        for (Process pro : processes) {
          if(pro.getId().equals("LeaveSlip")) {
              process = pro;
          }
        }
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();
        for (FlowElement flowElement : flowElements) {
            //如果是任务节点
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;
                if(userTask.getName().equals(task.getName())){
                    //获取出线信息
                    List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                    for(SequenceFlow sequenceFlow:outgoingFlows){
                        options.add(sequenceFlow.getName());
                    }
                }

            }
        }
        //这是activiti5 的 获取出线的代码
//        //3 根据流程定义Id获取流程定义实体
//        ProcessDefinitionEntity processDefinitionEntity =(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
//        //4 获取流程实例Id
//        String processInstanceId = task.getProcessInstanceId();
//        //5 使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
//        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
//                .processInstanceId(processInstanceId)//使用流程实例ID查询
//                .singleResult();
//        //6 获取当前活动Id
//        String activityId = pi.getActivityId();
//        //7 获取当前活动
//        ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
//		//8：获取当前活动完成之后连线的名称
//		List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
//		if(pvmList!=null && pvmList.size()>0){
//			for(PvmTransition pvm:pvmList){
//				String name = (String) pvm.getProperty("name");
//				if(StringUtils.isNotBlank(name)){
//					list.add(name);
//				}
//				else{
//					list.add("默认提交");
//				}
//			}
//		}
        return options;
    }

    @Override
    public List<Comment> getCommentByLeaveSlipId(Long id) {
        //使用请假单ID，查询请假单对象
        LeaveSlip leaveSlip = leaveSlipRepository.findById(id).get();
        //获取对象的名称
        String objectName = leaveSlip.getClass().getSimpleName();
        //组织流程表中的字段中的值
        String buniness_key = objectName+"."+id;

        /**1:使用历史的流程实例查询，返回历史的流程实例对象，获取流程实例ID*/
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//对应历史的流程实例表
						.processInstanceBusinessKey(buniness_key)//使用BusinessKey字段查询
						.singleResult();
//		//流程实例ID
		String processInstanceId = hpi.getId();
//        /**2:使用历史的流程变量查询，返回历史的流程变量的对象，获取流程实例ID*/
//        HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()//对应历史的流程变量表
//                .variableValueEquals("objId", objId)//使用流程变量的名称和流程变量的值查询
//                .singleResult();
        //流程实例ID
        List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
        return list;
    }

    //根据任务ID 获取 相关联的 请假单 信息
    private LeaveSlip getLeaveSlipByTaskId(String taskId){
        //1：使用任务ID，查询任务对象Task
        Task task = taskService.createTaskQuery()//
                .taskId(taskId)//使用任务ID查询
                .singleResult();
        //2：使用任务对象Task获取流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        //3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        //4：使用流程实例对象获取BUSINESS_KEY
        String buniness_key = pi.getBusinessKey();
        //5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
        String id = "";
        if(StringUtils.isNotBlank(buniness_key)){
            //截取字符串，取buniness_key小数点的第2个值
            id = buniness_key.split("\\.")[1];
        }
        //查询请假单对象
        LeaveSlip leaveSlip = leaveSlipRepository.findById(Long.valueOf(id)).get();
        return leaveSlip;
    }
}
