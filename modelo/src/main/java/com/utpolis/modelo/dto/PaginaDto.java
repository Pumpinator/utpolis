package com.utpolis.modelo.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaginaDto<T> {

    List<T> contenido;
    int numeroPagina;
    int tamanioPagina;
    long totalElementos;

}
