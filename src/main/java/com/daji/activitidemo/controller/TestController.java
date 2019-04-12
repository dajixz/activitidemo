package com.daji.activitidemo.controller;

import com.daji.activitidemo.entity.Role;
import com.daji.activitidemo.repository.RoleRepository;
import com.daji.activitidemo.service.LeaveSlipService;
import com.daji.activitidemo.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:29
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private RoleRepository roleRepository;
@Autowired
private LeaveSlipService leaveSlipService;
    @GetMapping("/one")
    public void testOne(){
        Role r1 = roleRepository.getOne(new Long(1));
        Role r2 = roleRepository.getOne(new Long(2));
        Role r3 = roleRepository.getOne(new Long(3));
        Set<Role> set = new HashSet<>();
        set.add(r2);
        set.add(r3);
        r1.setChildren(set);
        roleRepository.save(r1);
    }

    @GetMapping("/process")
    public R testProcess(String taskId){
        List<String> outcomeListByTaskId = leaveSlipService.getOutcomeListByTaskId(taskId);
        return R.ok(outcomeListByTaskId);
    }
}
