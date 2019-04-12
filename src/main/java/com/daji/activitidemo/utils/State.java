package com.daji.activitidemo.utils;

import lombok.Data;

/**
 * @Author: daji
 * @Date: 2019/4/9 13:30
 */
public enum  State {
    START(0,"初始录入"),
    FUDAOYUAN(1,"辅导员审批中"),
    YUGUANLIYUAN(2,"院管理员审批中"),
    BACKFROMFUDAOYUAN(3,"辅导员驳回"),
    BACKFROMYUANGUANLIYUAN(4,"院管理员驳回"),
    END(5,"审批完成");

    private int code;
    private String name;
    State(int code , String name){
        this.code = code ;
        this.name = name ;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}
