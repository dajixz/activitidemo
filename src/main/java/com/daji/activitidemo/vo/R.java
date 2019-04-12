package com.daji.activitidemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: daji
 * @Date: 2019/4/7 13:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class R {
    private int code;
    private String msg;
    private Object data;

    public static R ok(){
        return new R(200,"操作成功",null);
    }
    public static R ok(Object data){
        return new R(200,"操作成功",data);
    }
    public static R error(){
        return new R(400,"操作成功",null);
    }
}
