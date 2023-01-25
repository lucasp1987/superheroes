package com.example.superheroes;

import com.example.superheroes.controller.SuperheroController;
import com.example.superheroes.dto.SuperheroDto;
import com.example.superheroes.exception.SuperheroNotFoundException;
import com.example.superheroes.model.Superhero;
import com.example.superheroes.service.SuperheroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SuperheroController.class)
@WithMockUser(authorities = {"ADMIN", "USER", "ROLE_ADMIN_PRIVILEGE_WRITE"})
public class SuperheroesMvcTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SuperheroService superheroService;

    @Captor
    ArgumentCaptor<SuperheroDto> superheroDtoCaptor;

    @Test
    void shouldAddSuperheroToDatabase() throws Exception {
        // Given
        SuperheroDto superheroDto = new SuperheroDto("Bruce Wayne", "Dark", "Knight", "Bat Punch", "Batman");

        // When
        when(superheroService.addSuperhero(superheroDtoCaptor.capture())).thenReturn(1L);

        // Then
        this.mockMvc.perform(post("/api/v1/superheroes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(superheroDto))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority(("ROLE_ADMIN_PRIVILEGE_WRITE")))))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/v1/superheroes/1"));

        assertThat(superheroDtoCaptor.getValue().name(), is("Bruce Wayne"));
        assertThat(superheroDtoCaptor.getValue().prefix(), is("Dark"));
        assertThat(superheroDtoCaptor.getValue().suffix(), is("Knight"));
        assertThat(superheroDtoCaptor.getValue().power(), is("Bat Punch"));
        assertThat(superheroDtoCaptor.getValue().descriptor(), is("Batman"));
    }

    @Test
    void shouldReturnAllSuperheroes() throws Exception {
        // Given
        List<Superhero> superheroes = List.of(
                new Superhero(1L, "Clark Kent", "Man", "Steel", "Heat Vision", "Superman"),
                new Superhero(2L, "Tony Stark", "The", "Scientist", "Superhuman Strength", "Iron Man"),
                new Superhero(3L, "James Howlett", "Weapon", "X", "Adamantium Claws", "Wolverine"));

        // When
        when(superheroService.getAllSuperheroes()).thenReturn(superheroes);

        // Then
        this.mockMvc.perform(get("/api/v1/superheroes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Clark Kent")))
                .andExpect(jsonPath("$[0].prefix", is("Man")))
                .andExpect(jsonPath("$[0].suffix", is("Steel")))
                .andExpect(jsonPath("$[0].power", is("Heat Vision")))
                .andExpect(jsonPath("$[0].descriptor", is("Superman")));
    }


    @Test
    void shouldReturnASuperhero() throws Exception {
        // Given
        Superhero superheroe = new Superhero(1L, "Bruce Banner", "The", "Incredible", "Superhuman Speed", "Hulk");

        // When
        when(superheroService.getSuperheroById(1L)).thenReturn(superheroe);

        // Then
        this.mockMvc.perform(get("/api/v1/superheroes/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority(("ROLE_ADMIN_PRIVILEGE_WRITE")))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Bruce Banner")))
                .andExpect(jsonPath("$.prefix", is("The")))
                .andExpect(jsonPath("$.suffix", is("Incredible")))
                .andExpect(jsonPath("$.power", is("Superhuman Speed")))
                .andExpect(jsonPath("$.descriptor", is("Hulk")));
    }

    @Test
    void shouldReturnSuperheroNotFound() throws Exception {
        // Given
        Long id = 23L;

        // When
        when(superheroService.getSuperheroById(id)).thenThrow(new SuperheroNotFoundException(String.format("Superhero with id '%s' not found", id)));

        // Then
        this.mockMvc.perform(get("/api/v1/superheroes/23"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateASuperhero() throws Exception {
        // Given
        SuperheroDto superheroDto = new SuperheroDto("Bruce Wayne", "Dark", "Knight", "Bat Punch", "Batman");

        // When
        when(superheroService.updateSuperhero(eq(1L), superheroDtoCaptor.capture())).thenReturn(new Superhero(1L, "Bruce Wayne", "Dark", "Knight", "Bat Punch", "Batman"));

        // Then
        this.mockMvc.perform(put("/api/v1/superheroes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(superheroDto))
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority(("ROLE_ADMIN_PRIVILEGE_WRITE")))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Bruce Wayne")))
                .andExpect(jsonPath("$.prefix", is("Dark")))
                .andExpect(jsonPath("$.suffix", is("Knight")))
                .andExpect(jsonPath("$.power", is("Bat Punch")))
                .andExpect(jsonPath("$.descriptor", is("Batman")));

        assertThat(superheroDtoCaptor.getValue().name(), is("Bruce Wayne"));
        assertThat(superheroDtoCaptor.getValue().prefix(), is("Dark"));
        assertThat(superheroDtoCaptor.getValue().suffix(), is("Knight"));
        assertThat(superheroDtoCaptor.getValue().power(), is("Bat Punch"));
        assertThat(superheroDtoCaptor.getValue().descriptor(), is("Batman"));

    }

    @Test
    void shouldDeleteASuperheroById() throws Exception {
        // Given
        Long id = 1L;

        // When
        when(superheroService.deleteSuperheroById(id)).thenReturn("SUCCESS");

        // Then
        this.mockMvc.perform(delete("/api/v1/superheroes/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority(("ROLE_ADMIN_PRIVILEGE_WRITE")))))
                .andExpect(status().isAccepted());

    }

}
