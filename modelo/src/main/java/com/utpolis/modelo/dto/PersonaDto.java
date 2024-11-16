package com.utpolis.modelo.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PersonaDto {

    private Long id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String fechaNacimiento;

}