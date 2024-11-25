package com.utpolis.api.microservicio.persona.servicio;

import com.utpolis.api.microservicio.persona.repositorio.PersonaRepositorio;

import com.utpolis.modelo.dto.PersonaDto;
import com.utpolis.modelo.entidad.Persona;
import com.utpolis.modelo.entidad.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class PersonaServicio {

    private final PersonaRepositorio personaRepositorio;

    public PersonaDto crear(PersonaDto persona) {
        validar(persona);
        char[] ilegal = {' ', '(', ')', '[', ']', '{', '}', '=', '*', '+', '?', '.', '^', '$', '|', '\\'};
        String telefono = persona.getTelefono();
        for (char c : ilegal) {
            if (telefono.contains(String.valueOf(c))) {
                throw new RuntimeException("El campo telefono no puede contener espacios ni caracteres especiales");
            }
        }
        return construirDto(personaRepositorio.save(Persona.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .telefono(persona.getTelefono())
                .fechaNacimiento(persona.getFechaNacimiento())
                .build()));
    }

    public PersonaDto obtener(Long id) {
        return construirDto(personaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("No existe la persona con el id = " + id)));
    }


    public Iterable<PersonaDto> listar() {
        return ((List<Persona>) personaRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    public Page<PersonaDto> paginar(Pageable pageable) {
        return personaRepositorio.findAll(pageable).map(this::construirDto);
    }

    public PersonaDto modificar(PersonaDto persona) {
        validar(persona);
        Persona bdPersona = personaRepositorio.findById(persona.getId())
                .orElseThrow(() ->
                        new RuntimeException(String.format("Persona con id \'%d\' no encontrada", persona.getId()))
                );

        if (persona.getNombres() != null) bdPersona.setNombres(persona.getNombres());
        if (persona.getApellidos() != null) bdPersona.setApellidos(persona.getApellidos());
        if (persona.getTelefono() != null) bdPersona.setTelefono(persona.getTelefono());
        if (persona.getFechaNacimiento() != null) bdPersona.setFechaNacimiento(persona.getFechaNacimiento());

        return construirDto(personaRepositorio.save(bdPersona));
    }

    private PersonaDto construirDto(Persona persona) {
        return PersonaDto.builder()
                .id(persona.getId())
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .telefono(persona.getTelefono())
                .fechaNacimiento(persona.getFechaNacimiento())
                .build();

    }

    private void validar(PersonaDto persona) {
        if (esNueva(persona)) {
            if (persona.getNombres() == null || persona.getNombres().isBlank()) {
                throw new RuntimeException("El campo nombres es obligatorio");
            }
            if (persona.getApellidos() == null || persona.getApellidos().isBlank()) {
                throw new RuntimeException("El campo apellidos es obligatorio");
            }
            if (persona.getTelefono() == null || persona.getTelefono().isBlank()) {
                throw new RuntimeException("El campo telefono es obligatorio");
            }
            if (isNull(persona.getFechaNacimiento())) {
                throw new RuntimeException("El campo fechaNacimiento es obligatorio");
            }
        }
        if (personaRepositorio.findByTelefono(persona.getTelefono()).isPresent()) {
            throw new RuntimeException(String.format("Ya existe una persona con el telefono '%s'", persona.getTelefono()));
        }
    }

    private boolean esNueva(PersonaDto persona) {
        return isNull(persona.getId());
    }
}
