package com.utpolis.api.microservicio.persona.servicio;

import com.utpolis.api.microservicio.persona.repositorio.PersonaRepositorio;

import com.utpolis.modelo.dto.PersonaDto;
import com.utpolis.modelo.entidad.Persona;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaServicio {

    private PersonaRepositorio personaRepositorio;

    public PersonaDto crear(Persona persona){
        try {
            return construirDto(personaRepositorio.save(Persona.builder()
                    .id(persona.getId())
                    .nombres(persona.getNombres())
                    .apellidos(persona.getApellidos())
                    .telefono(persona.getTelefono())
                    .fechaNacimiento(persona.getFechaNacimiento())
                    .build()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Iterable <PersonaDto> listar(){
        return ((List<Persona>) personaRepositorio.findAll()).stream().map(this::construirDto).toList();
    }

    public PersonaDto obtener(Long id){
        return construirDto(personaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("No existe la persona con el id = " + id)));
    }

    public PersonaDto modificar(Persona persona){
        try {
            obtener(persona.getId());
            return construirDto(personaRepositorio.save(persona));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

}
