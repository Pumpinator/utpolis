package com.utpolis.modelo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UsuarioDto {

    private Long id;

    @Email
    @NotBlank(message = "El campo correo es obligatorio")
    @Size(max = 100, message = "El campo correo debe tener un máximo de 100 caracteres")
    private String correo;

    @NotBlank(message = "El campo nombre de usuario es obligatorio")
    @Size(max = 50, message = "El campo nombre de usuario debe tener un máximo de 50 caracteres")
    private String username;

    @NotBlank(message = "El campo contraseña es obligatorio")
    @Size(max = 100, message = "El campo contraseña debe tener un máximo de 100 caracteres")
    private String password;

    @NotBlank(message = "El campo rol es obligatorio")
    @Size(max = 50, message = "El campo rol debe tener un máximo de 50 caracteres")
    private String rol;

    @NotNull(message = "El campo id de persona es obligatorio")
    private Long personaId;

    private boolean activo;

}