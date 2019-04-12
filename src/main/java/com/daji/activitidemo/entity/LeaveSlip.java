package com.daji.activitidemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: daji
 * @Date: 2019/4/7 14:24
 */
@Entity
@Data
public class LeaveSlip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//主键ID
    private Integer days;// 请假天数
    private String content;// 请假内容
    private Date leaveDate = new Date();// 请假时间
    private String remark;// 备注
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;// 请假人
    private Integer state=0;// 请假单状态 0初始录入,1.审核中,2为审批完成

    @Transient
    //对应任务Id
    private String taskId;
    @Transient
    //对应任务名称
    private String taskName;
    //对应任务操作(连线)
    @Transient
    private List<String> options;

}
