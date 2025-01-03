package com.utpolis.api.microservicio.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@SpringBootApplication(scanBasePackages = {"com.utpolis.*"})
@EnableDiscoveryClient(autoRegister = true)
@EntityScan(basePackages = {"com.utpolis.*"})
@ComponentScan(basePackages = {"com.utpolis.*"})
public class MicroservicioUsuario {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioUsuario.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
