package com.utpolis.api.microservicio.venta.repositorio;

import com.utpolis.modelo.entidad.Inventario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepositorio extends CrudRepository<Inventario, Long> {

    Optional<Inventario> findByProductoId(Long id);

}
