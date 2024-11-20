package com.utpolis.api.microservicio.usuario.servicio;

import com.utpolis.modelo.dto.UsuarioDto;
import com.utpolis.modelo.entidad.Roles;
import com.utpolis.modelo.entidad.Usuario;
import com.utpolis.api.microservicio.usuario.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.usuario.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PersonaRepositorio personaRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDto obtener(String username) { // Obtener usuario por username
        return construirDto( // Construir DTO de usuario
                usuarioRepositorio.findByUsername(username) // Metodo de repositorio para obtener usuario por username
                        .orElseThrow(() -> // Si no se encuentra el usuario lanzar excepción
                                new RuntimeException(String.format("Usuario \'%s\' no encontrado", username))
                        )
        );
    }

    public UsuarioDto obtener(Long id) { // Obtener usuario por id
        return construirDto(
                usuarioRepositorio.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(String.format("Usuario con id \'%d\' no encontrado", id))
                        )
        );
    }

    public Iterable<UsuarioDto> obtener(Roles rol) { // Obtener usuario por rol
        return ((List<Usuario>) usuarioRepositorio.findAllByRol(rol.name())).stream().map(this::construirDto).toList();
    }

    public UsuarioDto crear(UsuarioDto usuario) { // Crear usuario
        validar(usuario); // Validar usuario
        return construirDto( // Construir DTO de usuario
                usuarioRepositorio.save(Usuario.builder() // Con el usuario guardado en el repositorio
                        .correo(usuario.getCorreo())
                        .username(usuario.getUsername())
                        .password(passwordEncoder.encode(usuario.getPassword()))
                        .rol(usuario.getRol())
                        .persona(personaRepositorio.findById(usuario.getPersonaId()).orElseThrow(() -> new RuntimeException(String.format("Persona con id \'%d\' no encontrada", usuario.getPersonaId()))))
                        .activo(usuario.getActivo())
                        .build()) // Construir usuario con Builder [2] https://refactoring.guru/design-patterns/builder
        );
    }

    public UsuarioDto modificar(UsuarioDto usuario) {
        validar(usuario);
        Usuario bdUsuario = usuarioRepositorio.findById(usuario.getId())
                .orElseThrow(() ->
                        new RuntimeException(String.format("Usuario con id \'%d\' no encontrado", usuario.getId()))
                );

        if (usuario.getPassword() != null)
            bdUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getCorreo() != null)
            bdUsuario.setCorreo(usuario.getCorreo());

        if (usuario.getUsername() != null)
            bdUsuario.setUsername(usuario.getUsername());

        if (usuario.getRol() != null)
            bdUsuario.setRol(usuario.getRol());

        return construirDto(usuarioRepositorio.save(bdUsuario));
    }

    public UsuarioDto eliminar(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(String.format("Usuario con id \'%d\' no encontrado", id))
                );
        usuario.setActivo(false);
        return construirDto(usuarioRepositorio.save(usuario));
    }

    public Iterable<UsuarioDto> listar() {
        return ((List<Usuario>) usuarioRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    public Page<UsuarioDto> paginar(Pageable pageable) {
        return usuarioRepositorio.findAll(pageable).map(this::construirDto);
    }

    public UsuarioDto construirDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .password("********")
                .rol(usuario.getRol())
                .personaId(usuario.getPersona().getId())
                .activo(usuario.getActivo())
                .build();
    }

    public void validar(UsuarioDto usuario) {
        if (esNuevo(usuario)) {
            if (usuario.getPassword() == null)
                throw new RuntimeException("Contraseña requerida");

            if (usuario.getPersonaId() == null)
                throw new RuntimeException("Persona requerida");

            if (!personaRepositorio.existsById(usuario.getPersonaId()))
                throw new RuntimeException(String.format("Persona con id \'%d\' no encontrada", usuario.getPersonaId()));

            if (usuario.getRol() == null)
                throw new RuntimeException("Rol requerido");

            if (usuario.getCorreo() == null)
                throw new RuntimeException("Correo requerido");

            if (usuario.getUsername() == null)
                throw new RuntimeException("Username requerido");

        } else {
            if (usuario.getId() == null)
                throw new RuntimeException("Id requerido");

            if (usuario.getActivo() != null)
                throw new RuntimeException(String.format("No se puede modificar el estado de activo del usuario con id \'%d\' porque ha sido eliminado", usuario.getId()));
        }

        if (usuario.getCorreo() != null && usuarioRepositorio.existsByCorreo(usuario.getCorreo()))
            throw new RuntimeException(String.format("Correo \'%s\' ya existe", usuario.getCorreo()));

        if (usuario.getUsername() != null && usuarioRepositorio.existsByUsername(usuario.getUsername()))
            throw new RuntimeException(String.format("Username \'%s\' ya existe", usuario.getUsername()));

        if (usuario.getRol() != null && !(usuario.getRol().equals(Roles.ADMINISTRADOR.name()) || usuario.getRol().equals(Roles.EMPLEADO.name()) || usuario.getRol().equals(Roles.CLIENTE.name())))
            throw new RuntimeException(String.format("Rol \'%s\' inválido"));
    }

    private boolean esNuevo(UsuarioDto usuario) {
        return isNull(usuario.getId());
    }

    private boolean esAdmin(String username) {
        return usuarioRepositorio.findByUsername(username).orElseThrow(() -> new RuntimeException(String.format("Usuario \'%s\' no encontrado", username))).getRol().equals(Roles.ADMINISTRADOR.name());
    }

}
