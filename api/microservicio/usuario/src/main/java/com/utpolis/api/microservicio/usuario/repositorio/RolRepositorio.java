package com.utpolis.api.microservicio.usuario.repositorio;

import com.utpolis.modelo.entidad.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepositorio extends CrudRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);

}
