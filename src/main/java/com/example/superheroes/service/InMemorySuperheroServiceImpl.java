package com.example.superheroes.service;

import com.example.superheroes.dto.SuperheroDto;
import com.example.superheroes.exception.SuperheroNotFoundException;
import com.example.superheroes.model.Superhero;
import com.example.superheroes.repository.SuperheroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InMemorySuperheroServiceImpl implements SuperheroService {

    @Autowired
    private SuperheroRepository superheroRepository;

    @Override
    public Long addSuperhero(SuperheroDto superheroDto) {
        Superhero superhero = new Superhero();
        superhero.setName(superheroDto.name());
        superhero.setPrefix(superheroDto.prefix());
        superhero.setSuffix(superheroDto.suffix());
        superhero.setDescriptor(superheroDto.descriptor());
        superhero = superheroRepository.save(superhero);
        return superhero.getId();
    }

    @Override
    public List<Superhero> getAllSuperheroes() {
        return superheroRepository.findAll();
    }

    @Override
    @Cacheable("superheroes")
    public Superhero getSuperheroById(Long id) {
        return superheroRepository.findById(id).orElseThrow(() -> new SuperheroNotFoundException(String.format("Superhero with id '%s' not found", id)));
    }

    @Override
    @Transactional
    @CachePut(value = "superheroes", key = "#superheroDto.id")
    public Superhero updateSuperhero(Long id, SuperheroDto superheroDto) {
        Optional<Superhero> superheroOpt = superheroRepository.findById(id);

        var superhero = superheroOpt.orElseThrow(() -> new SuperheroNotFoundException(String.format("Superhero with id '%s' not found", id)));

        superhero.setName(superheroDto.name());
        superhero.setPrefix(superheroDto.prefix());
        superhero.setSuffix(superheroDto.suffix());
        superhero.setPower(superheroDto.power());
        superhero.setDescriptor(superheroDto.descriptor());
        superhero = superheroRepository.save(superhero);

        return superhero;
    }

    @Override
    @CacheEvict(value = "superheroes", key = "#id")
    public String deleteSuperheroById(Long id) {
        superheroRepository.deleteById(id);
        return "SUCCESS";
    }

    @Override
    public List<Superhero> getSuperheroByDescriptorPart(String descriptorPart) {
        return superheroRepository.findByDescriptorContainsIgnoreCase(descriptorPart);
    }
}
