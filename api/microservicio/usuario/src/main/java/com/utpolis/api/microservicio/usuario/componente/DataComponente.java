package com.utpolis.api.microservicio.usuario.componente;

import com.utpolis.api.microservicio.usuario.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.usuario.repositorio.UsuarioRepositorio;
import com.utpolis.modelo.entidad.Persona;
import com.utpolis.modelo.entidad.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataComponente {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PersonaRepositorio personaRepositorio;

    @Transactional
    @EventListener
    public void crearDatos(ApplicationReadyEvent event) {
        Persona persona = crearPersona(Persona.builder()
                .nombres("Alejandro")
                .apellidos("Delgado Cardona")
                .telefono("4772285062")
                .fechaNacimiento("2003-04-23")
                .build()
        );

        crearUsuario(Usuario.builder()
                .correo("alejandro@gmail.com")
                .username("alejandro")
                .password("alejandro")
                .rol("ADMIN")
                .persona(persona)
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
