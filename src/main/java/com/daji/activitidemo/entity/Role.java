package com.daji.activitidemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author: daji
 * @Date: 2019/4/7 12:53
 */
@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleName;

    @Transient
    private Long parentId;

    /**子组织*/
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="parentId")
    private Set<Role> children;

}
