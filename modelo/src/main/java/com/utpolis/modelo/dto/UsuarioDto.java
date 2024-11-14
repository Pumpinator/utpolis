package com.utpolis.modelo.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UsuarioDto {

    private Long id;
    private String correo;
    private String username;
    private String password;
    private String rol;
    private Long personaId;
    private Boolean activo;

}