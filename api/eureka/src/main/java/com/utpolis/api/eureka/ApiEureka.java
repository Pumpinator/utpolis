package com.utpolis.api.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ApiEureka {

    public static void main(String[] args) {
        SpringApplication.run(ApiEureka.class, args);
    }

}
