package com.utpolis.api.microservicio.boleto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.utpolis.modelo.entidad"})
public class MicroservicioBoleto {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioBoleto.class, args);
    }

}
