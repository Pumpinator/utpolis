package com.utpolis.modelo.entidad;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_venta")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DetalleVenta {

    @EmbeddedId
    private DetalleVentaId detalleVentaId;

    @ManyToOne
    @MapsId("venta")
    @JoinColumn(name = "venta_id", referencedColumnName = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne
    @MapsId("producto")
    @JoinColumn(name = "producto_id", referencedColumnName = "producto_id")
    private Producto producto;

    @ManyToOne
    @MapsId("boleto")
    @JoinColumn(name = "boleto_id", referencedColumnName = "boleto_id")
    private Boleto boleto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private float subtotalDetalle;

    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class DetalleVentaId {

        @ManyToOne
        @JoinColumn(name = "venta_id", referencedColumnName = "venta_id")
        private Venta venta;

        @ManyToOne
        @JoinColumn(name = "producto_id", referencedColumnName = "producto_id")
        private Producto producto;

        @ManyToOne
        @JoinColumn(name = "boleto_id", referencedColumnName = "boleto_id")
        private Boleto boleto;

    }

}
