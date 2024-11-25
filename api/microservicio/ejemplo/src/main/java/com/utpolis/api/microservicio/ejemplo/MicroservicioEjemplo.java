package com.utpolis.api.microservicio.ejemplo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = {"com.utpolis.*"})
@EnableDiscoveryClient(autoRegister = true)
@EntityScan(basePackages = {"com.utpolis.*"})
@ComponentScan(basePackages = {"com.utpolis.*"})
public class MicroservicioEjemplo {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioEjemplo.class, args);
    }

}
