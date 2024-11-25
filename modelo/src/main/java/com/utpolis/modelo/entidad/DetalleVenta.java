package com.utpolis.modelo.entidad;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "detalle_venta")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_venta_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venta_id", referencedColumnName = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "funcion_id", referencedColumnName = "funcion_id")
    private Funcion funcion;

    @OneToMany
    @JoinTable(
            name = "detalle_venta_asiento",
            joinColumns = @JoinColumn(name = "detalle_venta_id"),
            inverseJoinColumns = @JoinColumn(name = "asiento_id")
    )
    private Collection<Asiento> asientos;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private float subtotalDetalle;

}
