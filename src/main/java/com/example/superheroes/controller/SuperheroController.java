package com.example.superheroes.controller;

import com.example.superheroes.annotation.Timer;
import com.example.superheroes.dto.SuperheroDto;
import com.example.superheroes.model.Superhero;
import com.example.superheroes.service.SuperheroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/superheroes")
public class SuperheroController {

    @Autowired
    private SuperheroService superheroService;

    @PostMapping
    @Timer
    @Operation(summary = "Add a superhero to the database", security = {@SecurityRequirement(name = "bearer-key")})
    @ApiResponse(responseCode = "201", description = "superhero created")
    public ResponseEntity<SuperheroDto> addSuperhero(@Valid @RequestBody SuperheroDto superheroDto, UriComponentsBuilder uriComponentsBuilder) {
        Long id = superheroService.addSuperhero(superheroDto);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/superheroes/{id}").buildAndExpand(id);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping
    @Timer
    @Operation(summary = "Get all superheroes from the database")
    public ResponseEntity<List<Superhero>> getAllSuperheroes(@RequestParam(name = "descriptorContain", required = false) String descriptorPart) {
        return (descriptorPart != null) ?
                ResponseEntity.ok(superheroService.getSuperheroByDescriptorPart(descriptorPart))
                :
                ResponseEntity.ok(superheroService.getAllSuperheroes());
    }

    @GetMapping("/{id}")
    @Timer
    @Operation(summary = "Get a superhero by id from the database", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Superhero> getSuperheroById(@PathVariable Long id) {
        return ResponseEntity.ok(superheroService.getSuperheroById(id));
    }

    @PutMapping("/{id}")
    @Timer
    @Operation(summary = "Update a superhero from the database", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<Superhero> updateSuperhero(@PathVariable("id") Long id, @Valid @RequestBody SuperheroDto superheroDto) {
        return ResponseEntity.ok(superheroService.updateSuperhero(id, superheroDto));
    }

    @DeleteMapping("/{id}")
    @Timer
    @Operation(summary = "Delete a superhero from the database", security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        superheroService.deleteSuperheroById(id);
        return ResponseEntity.accepted().build();
    }
}
