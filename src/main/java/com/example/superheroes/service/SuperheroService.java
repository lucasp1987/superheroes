package com.example.superheroes.service;

import com.example.superheroes.dto.SuperheroDto;
import com.example.superheroes.model.Superhero;

import java.util.List;

public interface SuperheroService {
    Long addSuperhero(SuperheroDto superheroDto);

    List<Superhero> getAllSuperheroes();

    Superhero getSuperheroById(Long id);

    Superhero updateSuperhero(Long id, SuperheroDto superheroDto);

    String deleteSuperheroById(Long id);

    List<Superhero> getSuperheroByDescriptorPart(String descriptorPart);
}
