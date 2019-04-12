package com.daji.activitidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.activiti.spring.boot.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitidemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitidemoApplication.class, args);
    }

}
