package com.bridgelabz.lmsadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LmsAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LmsAdminApplication.class, args);
    }

}
