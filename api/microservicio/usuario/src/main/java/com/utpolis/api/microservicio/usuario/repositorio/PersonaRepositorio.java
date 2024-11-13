package com.utpolis.api.microservicio.usuario.repositorio;

import com.utpolis.modelo.entidad.Persona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepositorio extends CrudRepository<Persona, Long> {

    Optional<Persona> findByTelefono(String telefono);

}
