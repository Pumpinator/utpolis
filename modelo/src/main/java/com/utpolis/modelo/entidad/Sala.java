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
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sala_id")
    private Long id;

    @Column(nullable = false)
    private short capacidad;

    @Column(nullable = false)
    private byte numero;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private boolean estatus;

}
