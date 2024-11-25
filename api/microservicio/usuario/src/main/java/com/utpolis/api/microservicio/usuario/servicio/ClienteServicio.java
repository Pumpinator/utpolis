package com.utpolis.api.microservicio.usuario.servicio;

import com.utpolis.api.microservicio.usuario.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.usuario.repositorio.RolRepositorio;
import com.utpolis.api.microservicio.usuario.repositorio.UsuarioRepositorio;
import com.utpolis.modelo.dto.UsuarioDto;
import com.utpolis.modelo.entidad.Roles; // Updated import statement
import com.utpolis.modelo.entidad.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ClienteServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final PersonaRepositorio personaRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDto obtener(Long id) {
        return construirDto(usuarioRepositorio.findById(id).orElseThrow(() -> new RuntimeException(String.format("Usuario con id %s no encontrado", id))));
    }

    public UsuarioDto obtener(String username) {
        return construirDto(usuarioRepositorio.findByUsername(username).orElseThrow(() -> new RuntimeException(String.format("Usuario con nombre %s no encontrado", username))));
    }

    public Iterable<UsuarioDto> listar() {
        return ((List<Usuario>) usuarioRepositorio.findAllByRolNombre(Roles.CLIENTE.name())).stream().map(this::construirDto).collect(Collectors.toList());
    }

    public Page<UsuarioDto> paginar(Pageable pageable) {
        return usuarioRepositorio.findAllByRolNombre(Roles.CLIENTE.name(), pageable).map(this::construirDto);
    }

    public UsuarioDto crear(UsuarioDto usuario) {
        validar(usuario);
        return construirDto( // Construir DTO de usuario
                usuarioRepositorio.save(Usuario.builder() // Con el usuario guardado en el repositorio
                        .correo(usuario.getCorreo())
                        .username(usuario.getUsername())
                        .password(passwordEncoder.encode(usuario.getPassword()))
                        .rol(rolRepositorio.findByNombre(Roles.CLIENTE.name()).orElseThrow(() -> new RuntimeException(String.format("Rol \'%s\' no encontrado", Roles.CLIENTE.name())))
                        )
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
            bdUsuario.setRol(rolRepositorio.findByNombre(usuario.getRol()).orElseThrow(() -> new RuntimeException(String.format("Rol \'%s\' no encontrado", usuario.getRol()))));

        return construirDto(usuarioRepositorio.save(bdUsuario));
    }

    public UsuarioDto eliminar(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(String.format("Usuario con id \'%d\' no encontrado", id))
                );
        if (!usuario.getRol().getNombre().equals(Roles.CLIENTE.name()))
            throw new RuntimeException(String.format("Usuario con id %s no es un '%s'", usuario.getId(), Roles.CLIENTE.name()));
        usuario.setActivo(false);
        return construirDto(usuarioRepositorio.save(usuario));
    }

    public UsuarioDto construirDto(Usuario usuario) {
        if (!usuario.getRol().getNombre().equals(Roles.CLIENTE.name()))
            throw new RuntimeException(String.format("Usuario con id %s no es un '%s'", usuario.getId(), Roles.CLIENTE.name()));
        return UsuarioDto.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .password("********")
                .rol(usuario.getRol().getNombre().charAt(0) + usuario.getRol().getNombre().substring(1).toLowerCase())
                .personaId(usuario.getPersona().getId())
                .activo(usuario.getActivo())
                .build();
    }

    public void validar(UsuarioDto usuario) {
        if (!usuario.getRol().equals(Roles.CLIENTE.name()))
            throw new RuntimeException(String.format("Rol \'%s\' no permitido", usuario.getRol()));
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
}