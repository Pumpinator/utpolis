package com.utpolis.api.microservicio.venta.repositorio;

import com.utpolis.modelo.entidad.DetalleVenta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepositorio extends CrudRepository<DetalleVenta, Long> {

    Iterable<DetalleVenta> findAllByVentaId(Long ventaId);

}
