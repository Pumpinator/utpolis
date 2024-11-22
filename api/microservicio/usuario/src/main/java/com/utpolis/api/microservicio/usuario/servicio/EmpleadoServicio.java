package com.utpolis.api.microservicio.usuario.servicio;

import com.utpolis.modelo.dto.PaginaDto;
import com.utpolis.modelo.dto.UsuarioDto;
import com.utpolis.modelo.entidad.Roles; // Updated import statement
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EmpleadoServicio {

    private final UsuarioServicio usuarioServicio;

    public UsuarioDto obtener(Long id) {
        return convertirDto(usuarioServicio.obtener(id));
    }

    public UsuarioDto obtener(String username) {
        return convertirDto(usuarioServicio.obtener(username));
    }

    public Iterable<UsuarioDto> listar() { // Obtener usuario por rol
        Roles empleado = Roles.EMPLEADO;
        return StreamSupport.stream(usuarioServicio.obtener(empleado).spliterator(), false)
                .map(this::convertirDto)
                .toList();
    }

    public PaginaDto<UsuarioDto> paginar(Pageable pageable) {
        Page<UsuarioDto> usuarios = usuarioServicio.paginar(pageable);
        List<UsuarioDto> empleados = construirDto(usuarios.getContent());
        return PaginaDto.<UsuarioDto>builder()
                .contenido(empleados)
                .numeroPagina(usuarios.getNumber())
                .tamanioPagina(usuarios.getSize())
                .totalElementos(empleados.size())
                .build();
    }

    public UsuarioDto crear(UsuarioDto usuario) {
        if(!usuario.getRol().equals(Roles.EMPLEADO.name()))
            throw new RuntimeException(String.format("Rol de usuario debe ser un '%s'", Roles.EMPLEADO.name()));
        return convertirDto(usuarioServicio.crear(usuario));
    }

    public UsuarioDto modificar(UsuarioDto usuario) {
        return convertirDto(usuarioServicio.modificar(usuario));
    }

    public UsuarioDto eliminar(Long id) {
        return convertirDto(usuarioServicio.eliminar(id));
    }

    private List<UsuarioDto> construirDto(List<UsuarioDto> usuarios) {
        return usuarios.stream()
                .filter(usuario -> usuario.getRol().equals(Roles.EMPLEADO.name()))
                .collect(Collectors.toList());
    }

    private UsuarioDto convertirDto(UsuarioDto usuario) {
        if (!usuario.getRol().equals(Roles.EMPLEADO.name()))
            throw new RuntimeException(String.format("Usuario con id %s no es un '%s'", usuario.getId(), Roles.EMPLEADO.name()));
        return UsuarioDto.builder()
                .id(usuario.getId())
                .correo(usuario.getCorreo())
                .username(usuario.getUsername())
                .password("********")
                .rol(usuario.getRol())
                .personaId(usuario.getPersonaId())
                .activo(usuario.getActivo())
                .build();
    }
}