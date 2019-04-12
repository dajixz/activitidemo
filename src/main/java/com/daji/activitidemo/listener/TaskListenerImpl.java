package com.daji.activitidemo.listener;

import com.daji.activitidemo.entity.Role;
import com.daji.activitidemo.entity.User;
import com.daji.activitidemo.repository.RoleRepository;
import com.daji.activitidemo.repository.UserRepository;
import com.daji.activitidemo.service.RoleService;
import com.daji.activitidemo.service.UserService;
import com.daji.activitidemo.utils.SpringUtil;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author: daji
 * @Date: 2019/4/7 16:29
 */

public class TaskListenerImpl implements TaskListener {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void notify(DelegateTask delegateTask) {
        if(roleRepository==null){
            roleRepository = (RoleRepository)SpringUtil.getBean("roleRepository");
        }
        if(userRepository==null){
            userRepository = (UserRepository)SpringUtil.getBean("userRepository");
        }
        //任务名称
        String name = delegateTask.getName();
        User user = delegateTask.getVariable("user", User.class);
        if("提交申请".equals(name)){
            delegateTask.setAssignee(String.valueOf(user.getId()));
        }else if ("辅导员审批".equals(name)){
            String unitName=user.getUnitName();
            List<User> userList = setCandidateUser(unitName+"辅导员");
            if(userList!=null&&userList.size()>0){
                for (User u:userList) {
                    delegateTask.addCandidateUser(String.valueOf(u.getId()));
                }
            }
        }else if("院管理员审批".equals(name)){
            String unitName=user.getUnitName();
            List<User> userList = setCandidateUser(unitName);
            if(userList!=null&&userList.size()>0){
                for (User u:userList) {
                    delegateTask.addCandidateUser(String.valueOf(u.getId()));
                }
            }
        }
    }
    private List<User> setCandidateUser(String roleName){
        Role role = roleRepository.getRoleByRoleName(roleName);
        Long roleId = role.getRoleId();
        List<User> userList = userRepository.getUserListByRoleId(roleId);
        return userList;
    }
}
