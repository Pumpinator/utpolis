package com.utpolis.api.microservicio.usuario.componente;

import com.utpolis.api.microservicio.usuario.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.usuario.repositorio.UsuarioRepositorio;
import com.utpolis.modelo.entidad.Persona;
import com.utpolis.modelo.entidad.Usuario;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataComponente {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PersonaRepositorio personaRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @EventListener
    public void crearDatos(ApplicationReadyEvent event) {
        Persona alejandro = crearPersona(Persona.builder()
                .nombres("Alejandro")
                .apellidos("Delgado Cardona")
                .telefono("4772285062")
                .fechaNacimiento("2003-04-23")
                .build()
        );

        Persona brandon =  crearPersona(Persona.builder()
                .nombres("Brandon")
                .apellidos("Montoya Ortiz")
                .telefono("4776994512")
                .fechaNacimiento("2001-03-09")
                .build()
        );

        crearUsuario(Usuario.builder()
                .correo("alejandro@gmail.com")
                .username("alejandro")
                .password(passwordEncoder.encode("alejandro"))
                .rol("ADMIN")
                .persona(alejandro)
                .activo(true)
                .build());

        crearUsuario(Usuario.builder()
                .correo("brandon@gmail.com")
                .username("brandon")
                .password(passwordEncoder.encode("brandon"))
                .rol("CLIENTE")
                .persona(brandon)
                .activo(true)
                .build());
    }

    public Persona crearPersona(Persona persona) {
        Persona bdPersona = personaRepositorio.findByTelefono(persona.getTelefono()).orElse(null);
        if (bdPersona == null)
            bdPersona = personaRepositorio.save(persona);
        return bdPersona;
    }

    public Usuario crearUsuario(Usuario usuario) {
        Usuario bdUsuario = usuarioRepositorio.findByUsername(usuario.getUsername()).orElse(null);
        if (bdUsuario == null)
            bdUsuario = usuarioRepositorio.save(usuario);
        return bdUsuario;
    }

}
