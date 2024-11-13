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
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Long id;

    @Column(nullable = false)
    private boolean estatus;

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private String tamanio;

    @Column(nullable = false)
    private String unidad;

    @Column(nullable = false)
    private float precio;

    @Column(nullable = false)
    private int existencia;

    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "categoria_id")
    private Categoria categoria;

}
