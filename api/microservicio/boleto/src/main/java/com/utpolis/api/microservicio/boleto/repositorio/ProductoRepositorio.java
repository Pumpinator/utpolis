package com.utpolis.api.microservicio.boleto.repositorio;

import com.utpolis.modelo.entidad.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorio extends CrudRepository<Producto, Long> {
}
