package com.utpolis.modelo.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Collection;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private Long id;

    @Column(nullable = false)
    private Instant fecha;

    @Column(nullable = false)
    private String estatus;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    private Usuario usuario;

}
