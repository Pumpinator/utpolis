package com.utpolis.api.microservicio.usuario.servicio;

import com.utpolis.modelo.dto.UsuarioDto;
import com.utpolis.api.microservicio.usuario.filtro.JwtFiltro;
import com.utpolis.modelo.entidad.Usuario;
import com.utpolis.api.microservicio.usuario.repositorio.PersonaRepositorio;
import com.utpolis.api.microservicio.usuario.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PersonaRepositorio personaRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioDto obtener(String username) {
        return construirDto(usuarioRepositorio
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format("Usuario \'%s\' no encontrado", username))));
    }

    public UsuarioDto obtener(Long id) {
        return construirDto(usuarioRepositorio
                .findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Usuario con id \'%d\' no encontrado", id))));
    }

    public UsuarioDto crear(UsuarioDto usuario) {
        return construirDto(usuarioRepositorio.save(Usuario.builder()
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .password(passwordEncoder.encode(usuario.getPassword()))
                .rol(usuario.getRol())
                .persona(personaRepositorio.findById(usuario.getPersonaId()).orElseThrow(() -> new RuntimeException(String.format("Persona con id \'%d\' no encontrada", usuario.getPersonaId()))))
                .activo(usuario.isActivo())
                .build()));
    }

    public Iterable<UsuarioDto> paginar() {
        return ((List<Usuario>) usuarioRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    public Page<UsuarioDto> paginar(Pageable pageable) {
        return usuarioRepositorio.findAll(pageable).map(this::construirDto);
    }

    private UsuarioDto construirDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .personaId(usuario.getPersona().getId())
                .activo(usuario.isActivo())
                .build();
    }

}
