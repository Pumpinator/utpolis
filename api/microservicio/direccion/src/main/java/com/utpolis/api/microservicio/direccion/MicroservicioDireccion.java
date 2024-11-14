package com.utpolis.api.microservicio.direccion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.utpolis.modelo.entidad"})
public class MicroservicioDireccion {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioDireccion.class, args);
    }

}
