package com.example.superheroes.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SuperheroDto(@NotEmpty @Size(min = 2, max = 50) String name, @Size(max = 25) String prefix,
                           @Size(max = 25) String suffix,
                           @NotEmpty String power, @NotEmpty @Size(max = 50) String descriptor) {
}
