package com.example.superheroes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record LoginDto(
        @Schema(description = "Username", type = "string", example = "pepeargento@gmail.com") String username,
        @Schema(description = "Password", type = "string", example = "Pepe$123") String password) {
}