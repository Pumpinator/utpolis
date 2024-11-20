package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.DetalleVenta;
import org.springframework.data.repository.CrudRepository;

public interface DetalleVentaRepositorio extends CrudRepository<DetalleVenta, Long> {

    Iterable<DetalleVenta> findByVentaId(Long ventaId);

}
