package com.utpolis.api.microservicio.usuario.filtro;

import com.utpolis.api.microservicio.usuario.servicio.UsuarioDetallesServicioImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SeguridadFiltro {

    private final UsuarioDetallesServicioImpl userDetailsService; // Inyección de dependencia para el servicio de detalles de usuario
    private final PasswordEncoder passwordEncoder; // Inyección de dependencia para encriptar contraseñas

    @Value("${jwt.secret}")
    private String JWT_SECRET; // Información sensible que se encuentra en el archivo application.properties

    @Value("${jwt.header}")
    private String JWT_HEADER;

    @Value("${jwt.prefix}")
    private String JWT_PREFIX;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    @Value("${path.login}")
    private String PATH_LOGIN;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception { // Configuración de seguridad
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        AutenticacionFiltro autenticacionFiltro = new AutenticacionFiltro(
                authenticationManager,
                JWT_SECRET,
                JWT_EXPIRATION
        );
        autenticacionFiltro.setFilterProcessesUrl(PATH_LOGIN);

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(HttpMethod.POST, "/registro")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .addFilter(new AutorizacionFiltro(
                        authenticationManager,
                        JWT_HEADER,
                        JWT_PREFIX,
                        JWT_SECRET
                ))
                .addFilter(autenticacionFiltro)
                .authenticationManager(authenticationManager)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .headers(headers -> {
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                .build();
    }

}
