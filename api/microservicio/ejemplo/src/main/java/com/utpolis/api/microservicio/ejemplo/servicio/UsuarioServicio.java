package com.utpolis.api.microservicio.ejemplo.servicio;

import com.utpolis.api.microservicio.ejemplo.filtro.JwtFiltro;
import com.utpolis.api.microservicio.ejemplo.repositorio.UsuarioRepositorio;
import com.utpolis.modelo.dto.UsuarioDto;
import com.utpolis.modelo.entidad.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.prefix}")
    private String JWT_PREFIX;

    public UsuarioDto obtener(String username) {
        return construirDto(usuarioRepositorio
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format("Usuario \'%s\' no encontrado", username))));
    }

    public String obtenerSujeto(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }

        String token = authorizationHeader.replace(JWT_PREFIX + " ", "");

        if (JWT_SECRET == null) {
            return null;
        }

        JwtFiltro jwtFiltro = new JwtFiltro(token, JWT_SECRET);

        String subject = jwtFiltro.obtenerSujeto();

        if (subject == null) {
            return null;
        }

        return subject;
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
