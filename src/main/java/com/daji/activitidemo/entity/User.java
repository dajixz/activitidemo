package com.daji.activitidemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author: daji
 * @Date: 2019/4/7 12:51
 */
@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String unitName;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roleList ;
}
