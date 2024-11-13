package com.utpolis.modelo.entidad;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pelicula_id")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String sinopsis;

    @Column(nullable = false)
    private String clasificacion;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String duracion;

    @Column(nullable = false)
    private String imagen;

    @Column(nullable = false)
    private String idioma;

}
