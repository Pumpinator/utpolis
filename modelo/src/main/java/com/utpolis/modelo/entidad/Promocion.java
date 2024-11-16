package com.utpolis.modelo.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promocion_id")
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private double descuento;

    @Column(nullable = false)
    private Instant fechaInicio;

    @Column(nullable = false)
    private Instant fechaFin;
}