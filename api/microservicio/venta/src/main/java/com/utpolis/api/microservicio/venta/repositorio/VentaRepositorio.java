package com.utpolis.api.microservicio.venta.repositorio;

import com.utpolis.modelo.entidad.Venta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepositorio extends CrudRepository<Venta, Long>, PagingAndSortingRepository<Venta, Long> {
}
