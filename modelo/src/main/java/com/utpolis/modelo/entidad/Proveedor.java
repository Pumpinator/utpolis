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
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Long id;

    @Column(nullable = false)
    private String nombreProveedor;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String telefono;

}
