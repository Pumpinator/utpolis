package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.Venta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepositorio extends CrudRepository<Venta, Long> {
}
